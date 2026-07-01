package org.example.loficonnect.payment.webhook;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.example.loficonnect.auth.repository.UserRepository;
import org.example.loficonnect.payment.dto.webhook.PaddleCustomData;
import org.example.loficonnect.payment.dto.webhook.PaddleSubscriptionEventData;
import org.example.loficonnect.payment.dto.webhook.PaddleTransactionEventData;
import org.example.loficonnect.payment.dto.webhook.PaddleWebhookPayload;
import org.example.loficonnect.payment.model.entity.PaymentEventEntity;
import org.example.loficonnect.payment.model.entity.SubscriptionPaymentDetailsEntity;
import org.example.loficonnect.payment.model.enums.PaymentProvider;
import org.example.loficonnect.payment.model.enums.ProductType;
import org.example.loficonnect.payment.model.enums.ProvisioningStatus;
import org.example.loficonnect.payment.repository.PaymentEventRepository;
import org.example.loficonnect.payment.repository.SubscriptionPaymentDetailsRepository;
import org.example.loficonnect.payment.service.provisioning.ProvisioningContext;
import org.example.loficonnect.payment.service.provisioning.ProvisioningStrategyFactory;
import org.example.loficonnect.subscription.model.entity.SubscriptionPlanEntity;
import org.example.loficonnect.subscription.model.entity.TenantSubscriptionEntity;
import org.example.loficonnect.subscription.model.enums.TenantSubscriptionStatus;
import org.example.loficonnect.subscription.repository.SubscriptionPlanRepository;
import org.example.loficonnect.subscription.repository.TenantSubscriptionRepository;
import org.example.loficonnect.subscription.service.TenantSubscriptionService;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

/**
 * Processes verified Paddle webhook events and keeps the local subscription mirror in sync.
 *
 * <p>Event routing:
 * <ul>
 *   <li>{@code transaction.completed}  — on first payment: creates TenantSubscription + SubscriptionPaymentDetails;
 *                                        on renewal: extends end_date.</li>
 *   <li>{@code subscription.created}   — only for TRIAL subscriptions (no initial payment). Paid subscriptions
 *                                        are handled by {@code transaction.completed}.</li>
 *   <li>{@code subscription.activated} — updates status to ACTIVE, sets end_date, triggers provisioning.</li>
 *   <li>{@code subscription.cancelled} — updates status to CANCELLED, triggers deprovisioning.</li>
 *   <li>{@code subscription.past_due}  — updates status to PAST_DUE.</li>
 * </ul>
 */
@Slf4j
@Component
public class PaddleWebhookProcessor {

    private final PaymentEventRepository paymentEventRepository;
    private final TenantSubscriptionRepository tenantSubscriptionRepository;
    private final SubscriptionPlanRepository subscriptionPlanRepository;
    private final SubscriptionPaymentDetailsRepository paymentDetailsRepository;
    private final ProvisioningStrategyFactory provisioningStrategyFactory;
    private final ObjectMapper objectMapper;
    private final UserRepository userRepository;
    private final TenantSubscriptionService tenantSubscriptionService;

    public PaddleWebhookProcessor(
            PaymentEventRepository paymentEventRepository,
            TenantSubscriptionRepository tenantSubscriptionRepository,
            SubscriptionPlanRepository subscriptionPlanRepository,
            SubscriptionPaymentDetailsRepository paymentDetailsRepository,
            ProvisioningStrategyFactory provisioningStrategyFactory,
            ObjectMapper objectMapper,
            UserRepository userRepository,
            TenantSubscriptionService tenantSubscriptionService) {
        this.paymentEventRepository = paymentEventRepository;
        this.tenantSubscriptionRepository = tenantSubscriptionRepository;
        this.subscriptionPlanRepository = subscriptionPlanRepository;
        this.paymentDetailsRepository = paymentDetailsRepository;
        this.provisioningStrategyFactory = provisioningStrategyFactory;
        this.objectMapper = objectMapper;
        this.userRepository = userRepository;
        this.tenantSubscriptionService = tenantSubscriptionService;
    }

    @Transactional
    public void process(PaddleWebhookPayload payload, String rawBody) {
        // ── Idempotency guard ──────────────────────────────────────────────────
        if (paymentEventRepository.existsByEventId(payload.getEventId())) {
            log.info("Skipping duplicate Paddle event: eventId={}", payload.getEventId());
            return;
        }

        // ── Persist event for audit ────────────────────────────────────────────
        saveEvent(payload.getEventId(), payload.getEventType(), rawBody);

        // ── Route by event type ────────────────────────────────────────────────
        switch (payload.getEventType()) {
            case "subscription.created" -> handleSubscriptionCreated(payload);
            case "subscription.activated" -> handleSubscriptionActivated(payload);
            case "subscription.cancelled" -> handleSubscriptionCancelled(payload);
            case "subscription.past_due" -> handleSubscriptionPastDue(payload);
            case "transaction.completed" -> handleTransactionCompleted(payload);
            default -> log.debug("Unhandled Paddle event type: {}", payload.getEventType());
        }
    }

    // ─── Event Handlers ───────────────────────────────────────────────────────

    /**
     * Handles trial-only subscriptions. Paid subscriptions are created in
     * {@link #handleTransactionCompleted} after payment is confirmed.
     */
    private void handleSubscriptionCreated(PaddleWebhookPayload payload) {
        PaddleSubscriptionEventData data = parseSubscriptionData(payload);

        // Paid subscriptions: wait for transaction.completed (payment confirmed).
        if (!"trialing".equals(data.getStatus())) {
            log.debug("subscription.created: non-trial, deferring to transaction.completed. paddleSubId={}", data.getId());
            return;
        }

        PaddleCustomData customData = data.getCustomData();
        if (customData == null || customData.getUserId() == null || customData.getPlanIdAsLong() == null) {
            log.warn("subscription.created (trial) missing custom_data, skipping. eventId={}", payload.getEventId());
            return;
        }

        if (paymentDetailsRepository.existsByPaddleSubscriptionId(data.getId())) {
            log.info("Payment details already exist for paddleSubId={}, skipping", data.getId());
            return;
        }

        Long userId = resolveUserId(customData.getUserId());
        Long planId = customData.getPlanIdAsLong();

        if (tenantSubscriptionRepository.existsByUserIdAndStatusIn(userId,
                List.of(TenantSubscriptionStatus.ACTIVE, TenantSubscriptionStatus.TRIAL))) {
            log.warn("subscription.created (trial): userId={} already has an active subscription, skipping. paddleSubId={}",
                    userId, data.getId());
            return;
        }

        SubscriptionPlanEntity plan = subscriptionPlanRepository
                .findByIdAndIsActiveAndIsDeleted(planId, true, false)
                .orElseThrow(() -> new EntityNotFoundException("Plan not found: " + planId));

        TenantSubscriptionEntity subscription = new TenantSubscriptionEntity();
        subscription.setUserId(userId);
        subscription.setSubscriptionPlan(plan);
        subscription.setStatus(TenantSubscriptionStatus.TRIAL);
        subscription.setProvisioningStatus(ProvisioningStatus.PENDING);
        subscription.setStartDate(Instant.now());
        if (data.getTrialDates() != null) {
            subscription.setTrialEndsAt(data.getTrialDates().getEndsAt());
            subscription.setEndDate(data.getTrialDates().getEndsAt());
        }
        tenantSubscriptionRepository.save(subscription);

        SubscriptionPaymentDetailsEntity paymentDetails = new SubscriptionPaymentDetailsEntity();
        paymentDetails.setTenantSubscriptionId(subscription.getId());
        paymentDetails.setPaymentProvider(PaymentProvider.PADDLE);
        paymentDetails.setPaddleSubscriptionId(data.getId());
        paymentDetails.setPaddleCustomerId(data.getCustomerId());
        paymentDetailsRepository.save(paymentDetails);

        log.info("subscription.created (trial): userId={}, planId={}, paddleSubId={}", userId, planId, data.getId());
    }

    private void handleSubscriptionActivated(PaddleWebhookPayload payload) {
        PaddleSubscriptionEventData data = parseSubscriptionData(payload);

        TenantSubscriptionEntity sub = findSubscriptionByPaddleId(data.getId());
        if (sub == null) return;

        sub.setStatus(TenantSubscriptionStatus.ACTIVE);
        sub.setIsActive(true);
        if (data.getCurrentBillingPeriod() != null) {
            sub.setEndDate(data.getCurrentBillingPeriod().getEndsAt());
        }
        tenantSubscriptionRepository.save(sub);

        log.info("subscription.activated: userId={}, paddleSubId={}", sub.getUserId(), data.getId());
        triggerProvisioning(sub, data.getId(), data.getCustomerId());
    }

    private void handleSubscriptionCancelled(PaddleWebhookPayload payload) {
        PaddleSubscriptionEventData data = parseSubscriptionData(payload);

        TenantSubscriptionEntity sub = findSubscriptionByPaddleId(data.getId());
        if (sub == null) return;

        sub.setStatus(TenantSubscriptionStatus.CANCELLED);
        sub.setIsActive(false);
        tenantSubscriptionRepository.save(sub);

        log.info("subscription.cancelled: userId={}, paddleSubId={}", sub.getUserId(), data.getId());
        triggerDeprovisioning(sub, data.getId(), data.getCustomerId());
    }

    private void handleSubscriptionPastDue(PaddleWebhookPayload payload) {
        PaddleSubscriptionEventData data = parseSubscriptionData(payload);

        TenantSubscriptionEntity sub = findSubscriptionByPaddleId(data.getId());
        if (sub == null) return;

        sub.setStatus(TenantSubscriptionStatus.PAST_DUE);
        tenantSubscriptionRepository.save(sub);

        log.warn("subscription.past_due: userId={}, paddleSubId={}", sub.getUserId(), data.getId());
    }

    /**
     * The authoritative payment confirmation event.
     *
     * <ul>
     *   <li><b>First payment</b> — no local subscription exists yet for the Paddle subscription ID.
     *       Creates {@code TenantSubscriptionEntity} + {@code SubscriptionPaymentDetailsEntity}.</li>
     *   <li><b>Renewal</b> — subscription already exists. Extends {@code end_date} by the billing cycle.</li>
     * </ul>
     */
    private void handleTransactionCompleted(PaddleWebhookPayload payload) {
        PaddleTransactionEventData data = parseTransactionData(payload);

        if (data.getSubscriptionId() == null) {
            log.debug("transaction.completed is not subscription-linked, skipping. txnId={}", data.getId());
            return;
        }

        Optional<SubscriptionPaymentDetailsEntity> existingDetails =
                paymentDetailsRepository.findByPaddleSubscriptionId(data.getSubscriptionId());

        if (existingDetails.isEmpty()) {
            // ── First payment: create the subscription ─────────────────────────
            createSubscriptionFromTransaction(data);
        } else {
            // ── Renewal: extend end date ───────────────────────────────────────
            TenantSubscriptionEntity sub = tenantSubscriptionRepository
                    .findById(existingDetails.get().getTenantSubscriptionId())
                    .orElseGet(() -> {
                        log.warn("TenantSubscription not found for id={}", existingDetails.get().getTenantSubscriptionId());
                        return null;
                    });
            if (sub == null) return;

            Instant newEndDate = calculateRenewalEndDate(sub);
            sub.setEndDate(newEndDate);
            sub.setStatus(TenantSubscriptionStatus.ACTIVE);
            sub.setIsActive(true);
            tenantSubscriptionRepository.save(sub);

            log.info("transaction.completed (renewal): userId={}, paddleSubId={}, newEndDate={}",
                    sub.getUserId(), data.getSubscriptionId(), newEndDate);
        }
    }

    private void createSubscriptionFromTransaction(PaddleTransactionEventData data) {
        PaddleCustomData customData = data.getCustomData();

        if (customData == null || customData.getUserId() == null || customData.getPlanIdAsLong() == null) {
            log.warn("transaction.completed missing custom_data, cannot create subscription. paddleSubId={}",
                    data.getSubscriptionId());
            return;
        }

        Long userId = resolveUserId(customData.getUserId());
        Long planId = customData.getPlanIdAsLong();

        // Guard: duplicate checkout (user paid twice before webhook arrived)
        if (tenantSubscriptionRepository.existsByUserIdAndStatusIn(userId,
                List.of(TenantSubscriptionStatus.ACTIVE, TenantSubscriptionStatus.TRIAL))) {
            log.warn("transaction.completed: userId={} already has an active subscription, skipping. paddleSubId={}",
                    userId, data.getSubscriptionId());
            return;
        }

        Instant startDate = Instant.now();
        Instant endDate = data.getBillingPeriod() != null ? data.getBillingPeriod().getEndsAt() : null;

        // Create subscription via service (business logic gateway)
        TenantSubscriptionEntity subscription = tenantSubscriptionService.subscribeFromPayment(userId, planId, startDate, endDate);

        // Link Paddle-specific IDs in a separate record
        SubscriptionPaymentDetailsEntity paymentDetails = new SubscriptionPaymentDetailsEntity();
        paymentDetails.setTenantSubscriptionId(subscription.getId());
        paymentDetails.setPaymentProvider(PaymentProvider.PADDLE);
        paymentDetails.setPaddleSubscriptionId(data.getSubscriptionId());
        paymentDetails.setPaddleCustomerId(data.getCustomerId());
        paymentDetailsRepository.save(paymentDetails);

        log.info("transaction.completed (new subscription): userId={}, planId={}, paddleSubId={}",
                userId, planId, data.getSubscriptionId());
    }

    // ─── Helpers ──────────────────────────────────────────────────────────────

    private TenantSubscriptionEntity findSubscriptionByPaddleId(String paddleSubId) {
        Optional<SubscriptionPaymentDetailsEntity> details =
                paymentDetailsRepository.findByPaddleSubscriptionId(paddleSubId);

        if (details.isEmpty()) {
            log.warn("No payment details found for paddleSubId={}", paddleSubId);
            return null;
        }

        return tenantSubscriptionRepository.findById(details.get().getTenantSubscriptionId())
                .orElseGet(() -> {
                    log.warn("TenantSubscription not found for id={}", details.get().getTenantSubscriptionId());
                    return null;
                });
    }

    private void triggerProvisioning(TenantSubscriptionEntity sub, String paddleSubId, String paddleCustomerId) {
        ProductType productType = sub.getSubscriptionPlan().getProductType();
        ProvisioningContext ctx = new ProvisioningContext(
                sub.getUserId(), sub.getId(), productType, paddleSubId, paddleCustomerId);
        provisioningStrategyFactory.get(productType).provision(ctx);
    }

    private void triggerDeprovisioning(TenantSubscriptionEntity sub, String paddleSubId, String paddleCustomerId) {
        ProductType productType = sub.getSubscriptionPlan().getProductType();
        ProvisioningContext ctx = new ProvisioningContext(
                sub.getUserId(), sub.getId(), productType, paddleSubId, paddleCustomerId);
        provisioningStrategyFactory.get(productType).deprovision(ctx);
    }

    private Instant calculateRenewalEndDate(TenantSubscriptionEntity sub) {
        Instant base = sub.getEndDate() != null ? sub.getEndDate() : Instant.now();
        return switch (sub.getSubscriptionPlan().getBillingCycle()) {
            case MONTHLY -> base.plus(30, ChronoUnit.DAYS);
            case QUARTERLY -> base.plus(90, ChronoUnit.DAYS);
            case ANNUAL -> base.plus(365, ChronoUnit.DAYS);
            case LIFETIME -> null;
        };
    }

    /**
     * Resolves a numeric user ID from custom_data's user_id field.
     * Accepts either a numeric string ("3") or an email ("user@example.com").
     */
    private Long resolveUserId(String userIdField) {
        try {
            return Long.parseLong(userIdField);
        } catch (NumberFormatException e) {
            return userRepository.findByUsername(userIdField)
                    .orElseThrow(() -> new EntityNotFoundException("User not found for identifier: " + userIdField))
                    .getId();
        }
    }

    private PaddleSubscriptionEventData parseSubscriptionData(PaddleWebhookPayload payload) {
        return objectMapper.convertValue(payload.getData(), PaddleSubscriptionEventData.class);
    }

    private PaddleTransactionEventData parseTransactionData(PaddleWebhookPayload payload) {
        return objectMapper.convertValue(payload.getData(), PaddleTransactionEventData.class);
    }

    private void saveEvent(String eventId, String eventType, String rawBody) {
        PaymentEventEntity event = new PaymentEventEntity();
        event.setEventId(eventId);
        event.setProvider(PaymentProvider.PADDLE);
        event.setEventType(eventType);
        event.setPayload(rawBody);
        event.setProcessedAt(Instant.now());
        paymentEventRepository.save(event);
    }
}
