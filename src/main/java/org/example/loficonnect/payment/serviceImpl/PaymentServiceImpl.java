package org.example.loficonnect.payment.serviceImpl;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.example.loficonnect.payment.config.PaddleProperties;
import org.example.loficonnect.payment.dto.paddle.PaddleCreateTransactionResponse;
import org.example.loficonnect.payment.dto.request.CheckoutRequest;
import org.example.loficonnect.payment.dto.response.CheckoutResponse;
import org.example.loficonnect.payment.dto.response.PaymentStatusResponse;
import org.example.loficonnect.commons.dto.response.SuccessResponse;
import org.example.loficonnect.payment.exception.PaymentException;
import org.example.loficonnect.payment.model.entity.CheckoutIntentEntity;
import org.example.loficonnect.payment.model.enums.CheckoutIntentStatus;
import org.example.loficonnect.payment.repository.CheckoutIntentRepository;
import org.example.loficonnect.payment.repository.SubscriptionPaymentDetailsRepository;
import org.example.loficonnect.payment.service.PaymentService;
import org.example.loficonnect.subscription.exception.NoActiveSubscriptionException;
import org.example.loficonnect.subscription.model.entity.SubscriptionPlanEntity;
import org.example.loficonnect.subscription.model.entity.TenantSubscriptionEntity;
import org.example.loficonnect.subscription.model.enums.AuditEventType;
import org.example.loficonnect.subscription.model.enums.TenantSubscriptionStatus;
import org.example.loficonnect.subscription.repository.SubscriptionPlanRepository;
import org.example.loficonnect.subscription.repository.TenantSubscriptionRepository;
import org.example.loficonnect.subscription.service.AuditLogService;
import org.example.loficonnect.auth.repository.UserRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class PaymentServiceImpl implements PaymentService {

    private static final List<TenantSubscriptionStatus> BLOCKING_STATUSES =
            List.of(TenantSubscriptionStatus.ACTIVE, TenantSubscriptionStatus.TRIAL,
                    TenantSubscriptionStatus.GRACE_PERIOD, TenantSubscriptionStatus.READ_ONLY,
                    TenantSubscriptionStatus.PROVISIONING_REQUIRED,
                    TenantSubscriptionStatus.PROVISIONING_IN_PROGRESS);

    private static final List<TenantSubscriptionStatus> ACTIVE_STATUSES =
            List.of(TenantSubscriptionStatus.ACTIVE, TenantSubscriptionStatus.TRIAL,
                    TenantSubscriptionStatus.GRACE_PERIOD, TenantSubscriptionStatus.READ_ONLY,
                    TenantSubscriptionStatus.REFUND_REQUESTED);

    private final RestClient paddleRestClient;
    private final PaddleProperties paddleProperties;
    private final TenantSubscriptionRepository tenantSubscriptionRepository;
    private final SubscriptionPlanRepository subscriptionPlanRepository;
    private final SubscriptionPaymentDetailsRepository subscriptionPaymentDetailsRepository;
    private final CheckoutIntentRepository checkoutIntentRepository;
    private final AuditLogService auditLogService;
    private final UserRepository userRepository;

    public PaymentServiceImpl(
            @Qualifier("paddleRestClient") RestClient paddleRestClient,
            PaddleProperties paddleProperties,
            TenantSubscriptionRepository tenantSubscriptionRepository,
            SubscriptionPlanRepository subscriptionPlanRepository,
            SubscriptionPaymentDetailsRepository subscriptionPaymentDetailsRepository,
            CheckoutIntentRepository checkoutIntentRepository,
            AuditLogService auditLogService,
            UserRepository userRepository) {
        this.paddleRestClient = paddleRestClient;
        this.paddleProperties = paddleProperties;
        this.tenantSubscriptionRepository = tenantSubscriptionRepository;
        this.subscriptionPlanRepository = subscriptionPlanRepository;
        this.subscriptionPaymentDetailsRepository = subscriptionPaymentDetailsRepository;
        this.checkoutIntentRepository = checkoutIntentRepository;
        this.auditLogService = auditLogService;
        this.userRepository = userRepository;
    }

    // ─── Checkout ─────────────────────────────────────────────────────────────

    @Override
    public CheckoutResponse createCheckout(Long userId, CheckoutRequest request) {
        SubscriptionPlanEntity plan = subscriptionPlanRepository
                .findByIdAndIsActiveAndIsDeleted(request.getPlanId(), true, false)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Subscription plan not found: " + request.getPlanId()));

        if (plan.getPaddlePriceId() == null || plan.getPaddlePriceId().isBlank()) {
            throw new IllegalArgumentException(
                    "Plan '" + plan.getCode() + "' has no Paddle price configured. "
                    + "Set paddle_price_id in the admin console first.");
        }

        if (tenantSubscriptionRepository.existsByUserIdAndStatusIn(userId, BLOCKING_STATUSES)) {
            throw new IllegalArgumentException("User already has an active subscription. Cancel it before subscribing to a new plan.");
        }

        String userEmail = userRepository.findById(userId)
                .map(u -> u.getUsername())
                .orElse(null);

        Map<String, Object> body = new java.util.LinkedHashMap<>();
        body.put("items", List.of(Map.of(
                "price_id", plan.getPaddlePriceId(),
                "quantity", 1
        )));
        body.put("custom_data", Map.of(
                "user_id", userId.toString(),
                "plan_id", request.getPlanId().toString()
        ));
        body.put("return_url", paddleProperties.getSuccessUrl());
        if (userEmail != null) {
            body.put("customer", Map.of("email", userEmail));
        }

        try {
            PaddleCreateTransactionResponse response = paddleRestClient.post()
                    .uri("/transactions")
                    .body(body)
                    .retrieve()
                    .body(PaddleCreateTransactionResponse.class);

            if (response == null || response.getData() == null || response.getData().getCheckout() == null) {
                throw new PaymentException("Empty or malformed response from Paddle transactions API");
            }

            String checkoutUrl = response.getData().getCheckout().getUrl();
            String transactionId = response.getData().getId();

            // Track checkout intent for reminder emails and expiry handling
            CheckoutIntentEntity intent = new CheckoutIntentEntity();
            intent.setUserId(userId);
            intent.setPlanId(request.getPlanId());
            intent.setPaddleTransactionId(transactionId);
            intent.setStatus(CheckoutIntentStatus.PENDING);
            intent.setExpiresAt(Instant.now().plus(48, ChronoUnit.HOURS));
            checkoutIntentRepository.save(intent);

            auditLogService.logUser(null, userId, AuditEventType.CHECKOUT_STARTED,
                    null, "planId=" + request.getPlanId() + ", txnId=" + transactionId);

            log.info("Paddle checkout created: userId={}, planId={}, txnId={}", userId, request.getPlanId(), transactionId);
            return new CheckoutResponse(checkoutUrl, transactionId);

        } catch (RestClientException ex) {
            log.error("Paddle API error creating checkout for userId={}, planId={}: {}", userId, request.getPlanId(), ex.getMessage());
            throw new PaymentException("Failed to create Paddle checkout session: " + ex.getMessage(), ex);
        }
    }

    // ─── Status ───────────────────────────────────────────────────────────────

    @Override
    public PaymentStatusResponse getPaymentStatus(Long userId) {
        return tenantSubscriptionRepository
                .findFirstByUserIdOrderByIdDesc(userId)
                .map(sub -> new PaymentStatusResponse(
                        sub.getStatus(),
                        sub.getProvisioningStatus(),
                        Boolean.TRUE.equals(sub.getIsActive())))
                .orElse(new PaymentStatusResponse(null, null, false));
    }

    // ─── Upgrade / Downgrade ──────────────────────────────────────────────────

    /**
     * Calls Paddle PATCH /subscriptions/{id} to change the subscription plan.
     * Paddle fires subscription.updated webhook → local plan is synced by the webhook processor.
     * This method does NOT change local state directly; it is driven entirely by Paddle.
     */
    @Override
    public SuccessResponse upgradePlan(Long userId, Long newPlanId) {
        TenantSubscriptionEntity sub = tenantSubscriptionRepository
                .findByUserIdAndStatusIn(userId, ACTIVE_STATUSES)
                .orElseThrow(() -> new NoActiveSubscriptionException(
                        "No active subscription found for user: " + userId));

        SubscriptionPlanEntity newPlan = subscriptionPlanRepository
                .findByIdAndIsActiveAndIsDeleted(newPlanId, true, false)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Subscription plan not found: " + newPlanId));

        if (newPlan.getPaddlePriceId() == null || newPlan.getPaddlePriceId().isBlank()) {
            throw new IllegalArgumentException(
                    "Plan '" + newPlan.getCode() + "' has no Paddle price configured.");
        }

        if (sub.getSubscriptionPlan().getId().equals(newPlanId)) {
            throw new IllegalArgumentException("User is already subscribed to this plan.");
        }

        String paddleSubscriptionId = subscriptionPaymentDetailsRepository
                .findByTenantSubscriptionId(sub.getId())
                .map(details -> details.getPaddleSubscriptionId())
                .orElseThrow(() -> new IllegalStateException(
                        "No Paddle subscription linked to this subscription. "
                        + "Manual plan changes are not supported via this endpoint."));

        if (paddleSubscriptionId == null) {
            throw new IllegalStateException(
                    "No Paddle subscription ID found. Cannot upgrade via Paddle.");
        }

        updatePaddleSubscriptionPlan(paddleSubscriptionId, newPlan.getPaddlePriceId());

        log.info("Plan change requested via Paddle: userId={}, subscriptionId={}, newPlanId={}, paddleSubId={}",
                userId, sub.getId(), newPlanId, paddleSubscriptionId);

        return new SuccessResponse(true, sub.getId());
    }

    private void updatePaddleSubscriptionPlan(String paddleSubscriptionId, String newPriceId) {
        try {
            paddleRestClient.patch()
                    .uri("/subscriptions/{id}", paddleSubscriptionId)
                    .body(Map.of(
                            "items", List.of(Map.of(
                                    "price_id", newPriceId,
                                    "quantity", 1
                            )),
                            "proration_billing_mode", "prorated_immediately"
                    ))
                    .retrieve()
                    .toBodilessEntity();

            log.info("Paddle subscription plan updated: paddleSubId={}, newPriceId={}", paddleSubscriptionId, newPriceId);

        } catch (RestClientException ex) {
            log.error("Paddle API error updating subscription {}: {}", paddleSubscriptionId, ex.getMessage());
            throw new PaymentException("Failed to update Paddle subscription plan: " + ex.getMessage(), ex);
        }
    }

    // ─── Cancel ───────────────────────────────────────────────────────────────

    /**
     * Requests cancellation at end of the current billing period via Paddle.
     * Does NOT update local subscription status — the user retains full access
     * until period end. The Paddle {@code subscription.cancelled} webhook fires
     * at period end and drives the local status to CANCELLED.
     */
    @Override
    public void cancelUserSubscription(Long userId) {
        TenantSubscriptionEntity sub = tenantSubscriptionRepository
                .findByUserIdAndStatusIn(userId, ACTIVE_STATUSES)
                .orElseThrow(() -> new NoActiveSubscriptionException(
                        "No active subscription found for user: " + userId));

        subscriptionPaymentDetailsRepository.findByTenantSubscriptionId(sub.getId())
                .ifPresent(details -> {
                    if (details.getPaddleSubscriptionId() != null) {
                        cancelPaddleSubscription(details.getPaddleSubscriptionId());
                    } else {
                        log.warn("No Paddle subscription ID for subscriptionId={}. "
                                + "Cancellation via Paddle skipped.", sub.getId());
                    }
                });
    }

    private void cancelPaddleSubscription(String paddleSubscriptionId) {
        try {
            paddleRestClient.post()
                    .uri("/subscriptions/{id}/cancel", paddleSubscriptionId)
                    .body(Map.of("effective_from", "next_billing_period"))
                    .retrieve()
                    .toBodilessEntity();

            log.info("Paddle cancellation scheduled at period end: paddleSubId={}", paddleSubscriptionId);

        } catch (RestClientException ex) {
            log.error("Paddle API error while cancelling subscription {}: {}", paddleSubscriptionId, ex.getMessage());
            throw new PaymentException("Failed to cancel Paddle subscription: " + paddleSubscriptionId, ex);
        }
    }
}
