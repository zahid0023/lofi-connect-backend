package org.example.loficonnect.payment.serviceImpl;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.example.loficonnect.payment.config.PaddleProperties;
import org.example.loficonnect.payment.dto.paddle.PaddleCreateTransactionResponse;
import org.example.loficonnect.payment.dto.request.CheckoutRequest;
import org.example.loficonnect.payment.dto.response.CheckoutResponse;
import org.example.loficonnect.payment.dto.response.PaymentStatusResponse;
import org.example.loficonnect.payment.exception.PaymentException;
import org.example.loficonnect.payment.repository.SubscriptionPaymentDetailsRepository;
import org.example.loficonnect.payment.service.PaymentService;
import org.example.loficonnect.subscription.exception.NoActiveSubscriptionException;
import org.example.loficonnect.subscription.model.entity.SubscriptionPlanEntity;
import org.example.loficonnect.subscription.model.entity.TenantSubscriptionEntity;
import org.example.loficonnect.subscription.model.enums.TenantSubscriptionStatus;
import org.example.loficonnect.subscription.repository.SubscriptionPlanRepository;
import org.example.loficonnect.subscription.repository.TenantSubscriptionRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class PaymentServiceImpl implements PaymentService {

    private static final List<TenantSubscriptionStatus> ACTIVE_STATUSES =
            List.of(TenantSubscriptionStatus.ACTIVE, TenantSubscriptionStatus.TRIAL);

    private final RestClient paddleRestClient;
    private final PaddleProperties paddleProperties;
    private final TenantSubscriptionRepository tenantSubscriptionRepository;
    private final SubscriptionPlanRepository subscriptionPlanRepository;
    private final SubscriptionPaymentDetailsRepository subscriptionPaymentDetailsRepository;

    public PaymentServiceImpl(
            @Qualifier("paddleRestClient") RestClient paddleRestClient,
            PaddleProperties paddleProperties,
            TenantSubscriptionRepository tenantSubscriptionRepository,
            SubscriptionPlanRepository subscriptionPlanRepository,
            SubscriptionPaymentDetailsRepository subscriptionPaymentDetailsRepository) {
        this.paddleRestClient = paddleRestClient;
        this.paddleProperties = paddleProperties;
        this.tenantSubscriptionRepository = tenantSubscriptionRepository;
        this.subscriptionPlanRepository = subscriptionPlanRepository;
        this.subscriptionPaymentDetailsRepository = subscriptionPaymentDetailsRepository;
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

        if (tenantSubscriptionRepository.existsByUserIdAndStatusIn(userId, ACTIVE_STATUSES)) {
            throw new IllegalArgumentException("User already has an active subscription. Cancel it before subscribing to a new plan.");
        }

        Map<String, Object> body = Map.of(
                "items", List.of(Map.of(
                        "price_id", plan.getPaddlePriceId(),
                        "quantity", 1
                )),
                "custom_data", Map.of(
                        "user_id", userId.toString(),
                        "plan_id", request.getPlanId().toString()
                )
        );

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

    // ─── Cancel ───────────────────────────────────────────────────────────────

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

            log.info("Paddle subscription cancellation requested: paddleSubId={}", paddleSubscriptionId);

        } catch (RestClientException ex) {
            log.error("Paddle API error while cancelling subscription {}: {}", paddleSubscriptionId, ex.getMessage());
            throw new PaymentException("Failed to cancel Paddle subscription: " + paddleSubscriptionId, ex);
        }
    }
}
