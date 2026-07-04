package org.example.loficonnect.payment.webhook;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.example.loficonnect.auth.repository.UserRepository;
import org.example.loficonnect.payment.dto.webhook.PaddleCustomData;
import org.example.loficonnect.payment.dto.webhook.PaddleSubscriptionEventData;
import org.example.loficonnect.payment.dto.webhook.PaddleSubscriptionItem;
import org.example.loficonnect.payment.dto.webhook.PaddleTransactionEventData;
import org.example.loficonnect.payment.dto.webhook.PaddleWebhookPayload;
import org.example.loficonnect.payment.model.entity.CheckoutIntentEntity;
import org.example.loficonnect.payment.model.entity.PaymentEventEntity;
import org.example.loficonnect.payment.model.entity.SubscriptionPaymentDetailsEntity;
import org.example.loficonnect.payment.model.enums.CheckoutIntentStatus;
import org.example.loficonnect.payment.model.enums.PaymentProvider;
import org.example.loficonnect.payment.model.enums.ProductType;
import org.example.loficonnect.payment.model.enums.ProvisioningStatus;
import org.example.loficonnect.payment.repository.CheckoutIntentRepository;
import org.example.loficonnect.payment.repository.PaymentEventRepository;
import org.example.loficonnect.payment.repository.SubscriptionPaymentDetailsRepository;
import org.example.loficonnect.payment.service.provisioning.ProvisioningContext;
import org.example.loficonnect.payment.service.provisioning.ProvisioningStrategyFactory;
import org.example.loficonnect.subscription.model.entity.SubscriptionPlanEntity;
import org.example.loficonnect.subscription.model.entity.TenantSubscriptionEntity;
import org.example.loficonnect.subscription.model.enums.AuditEventType;
import org.example.loficonnect.subscription.model.enums.TenantSubscriptionStatus;
import org.example.loficonnect.subscription.repository.SubscriptionPlanRepository;
import org.example.loficonnect.subscription.repository.TenantSubscriptionRepository;
import org.example.loficonnect.subscription.service.AuditLogService;
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
    private final AuditLogService auditLogService;
    private final CheckoutIntentRepository checkoutIntentRepository;

    public PaddleWebhookProcessor(
            PaymentEventRepository paymentEventRepository,
            TenantSubscriptionRepository tenantSubscriptionRepository,
            SubscriptionPlanRepository subscriptionPlanRepository,
            SubscriptionPaymentDetailsRepository paymentDetailsRepository,
            ProvisioningStrategyFactory provisioningStrategyFactory,
            ObjectMapper objectMapper,
            UserRepository userRepository,
            TenantSubscriptionService tenantSubscriptionService,
            AuditLogService auditLogService,
            CheckoutIntentRepository checkoutIntentRepository) {
        this.paymentEventRepository = paymentEventRepository;
        this.tenantSubscriptionRepository = tenantSubscriptionRepository;
        this.subscriptionPlanRepository = subscriptionPlanRepository;
        this.paymentDetailsRepository = paymentDetailsRepository;
        this.provisioningStrategyFactory = provisioningStrategyFactory;
        this.objectMapper = objectMapper;
        this.userRepository = userRepository;
        this.tenantSubscriptionService = tenantSubscriptionService;
        this.auditLogService = auditLogService;
        this.checkoutIntentRepository = checkoutIntentRepository;
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

        // ── Audit: record that the webhook arrived ─────────────────────────────
        auditLogService.logPaddle(null, AuditEventType.PADDLE_WEBHOOK_RECEIVED,
                null, payload.getEventType(), payload.getEventId());

        // ── Route by event type ────────────────────────────────────────────────
        switch (payload.getEventType()) {
            case "subscription.created"   -> handleSubscriptionCreated(payload);
            case "subscription.activated" -> handleSubscriptionActivated(payload);
            case "subscription.updated"   -> handleSubscriptionUpdated(payload);
            case "subscription.cancelled" -> handleSubscriptionCancelled(payload);
            case "subscription.past_due"  -> handleSubscriptionPastDue(payload);
            case "subscription.paused"    -> handleSubscriptionPaused(payload);
            case "subscription.resumed"   -> handleSubscriptionResumed(payload);
            case "transaction.completed"  -> handleTransactionCompleted(payload);
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

        // Provision immediately so trial users can generate API keys right away
        triggerProvisioning(subscription, data.getId(), data.getCustomerId());

        log.info("subscription.created (trial): userId={}, planId={}, paddleSubId={}", userId, planId, data.getId());
    }

    private void handleSubscriptionActivated(PaddleWebhookPayload payload) {
        PaddleSubscriptionEventData data = parseSubscriptionData(payload);

        TenantSubscriptionEntity sub = findSubscriptionByPaddleId(data.getId());
        if (sub == null) return;

        TenantSubscriptionStatus previous = sub.getStatus();
        sub.setStatus(TenantSubscriptionStatus.ACTIVE);
        sub.setIsActive(true);
        if (data.getCurrentBillingPeriod() != null) {
            sub.setEndDate(data.getCurrentBillingPeriod().getEndsAt());
        }
        tenantSubscriptionRepository.save(sub);

        auditLogService.logPaddle(sub.getId(), AuditEventType.SUBSCRIPTION_STATUS_CHANGED,
                previous.name(), TenantSubscriptionStatus.ACTIVE.name(), payload.getEventId());

        log.info("subscription.activated: userId={}, paddleSubId={}", sub.getUserId(), data.getId());
        // Only provision if not already done by transaction.completed (race-condition guard)
        if (sub.getProvisioningStatus() == ProvisioningStatus.PENDING) {
            triggerProvisioning(sub, data.getId(), data.getCustomerId());
        }
        markCheckoutIntentCompleted(data.getCustomData());
    }

    /**
     * Handles subscription.updated — fires when any property changes (plan, billing period, status).
     * Key uses:
     * - Detect plan change: compare items[0].price.id to current plan's paddlePriceId
     * - Detect status change: map Paddle status to local status
     */
    private void handleSubscriptionUpdated(PaddleWebhookPayload payload) {
        PaddleSubscriptionEventData data = parseSubscriptionData(payload);

        TenantSubscriptionEntity sub = findSubscriptionByPaddleId(data.getId());
        if (sub == null) return;

        boolean changed = false;

        // ── Detect plan change via items ──────────────────────────────────────
        if (data.getItems() != null && !data.getItems().isEmpty()) {
            PaddleSubscriptionItem firstItem = data.getItems().get(0);
            if (firstItem.getPrice() != null && firstItem.getPrice().getId() != null) {
                String newPriceId = firstItem.getPrice().getId();
                String currentPriceId = sub.getSubscriptionPlan().getPaddlePriceId();
                if (!newPriceId.equals(currentPriceId)) {
                    subscriptionPlanRepository.findByPaddlePriceId(newPriceId).ifPresent(newPlan -> {
                        String oldPlanName = sub.getSubscriptionPlan().getName();
                        sub.setSubscriptionPlan(newPlan);
                        auditLogService.logPaddle(sub.getId(), AuditEventType.PLAN_CHANGED,
                                oldPlanName, newPlan.getName(), payload.getEventId());
                        log.info("subscription.updated: plan changed userId={}, {} → {}",
                                sub.getUserId(), oldPlanName, newPlan.getName());
                    });
                    changed = true;
                }
            }
        }

        // ── Sync billing period end date ──────────────────────────────────────
        if (data.getCurrentBillingPeriod() != null && data.getCurrentBillingPeriod().getEndsAt() != null) {
            sub.setEndDate(data.getCurrentBillingPeriod().getEndsAt());
            changed = true;
        }

        if (changed) {
            tenantSubscriptionRepository.save(sub);
        }

        log.info("subscription.updated: userId={}, paddleSubId={}", sub.getUserId(), data.getId());
    }

    private void handleSubscriptionCancelled(PaddleWebhookPayload payload) {
        PaddleSubscriptionEventData data = parseSubscriptionData(payload);

        TenantSubscriptionEntity sub = findSubscriptionByPaddleId(data.getId());
        if (sub == null) return;

        TenantSubscriptionStatus previous = sub.getStatus();
        sub.setStatus(TenantSubscriptionStatus.CANCELLED);
        sub.setIsActive(false);
        sub.setCancelledAt(Instant.now());
        tenantSubscriptionRepository.save(sub);

        auditLogService.logPaddle(sub.getId(), AuditEventType.SUBSCRIPTION_STATUS_CHANGED,
                previous.name(), TenantSubscriptionStatus.CANCELLED.name(), payload.getEventId());

        log.info("subscription.cancelled: userId={}, paddleSubId={}", sub.getUserId(), data.getId());
        triggerDeprovisioning(sub, data.getId(), data.getCustomerId());
    }

    private void handleSubscriptionPastDue(PaddleWebhookPayload payload) {
        PaddleSubscriptionEventData data = parseSubscriptionData(payload);

        TenantSubscriptionEntity sub = findSubscriptionByPaddleId(data.getId());
        if (sub == null) return;

        TenantSubscriptionStatus previous = sub.getStatus();
        sub.setStatus(TenantSubscriptionStatus.PAST_DUE);
        tenantSubscriptionRepository.save(sub);

        auditLogService.logPaddle(sub.getId(), AuditEventType.SUBSCRIPTION_STATUS_CHANGED,
                previous.name(), TenantSubscriptionStatus.PAST_DUE.name(), payload.getEventId());

        log.warn("subscription.past_due: userId={}, paddleSubId={}", sub.getUserId(), data.getId());
    }

    private void handleSubscriptionPaused(PaddleWebhookPayload payload) {
        PaddleSubscriptionEventData data = parseSubscriptionData(payload);

        TenantSubscriptionEntity sub = findSubscriptionByPaddleId(data.getId());
        if (sub == null) return;

        TenantSubscriptionStatus previous = sub.getStatus();
        sub.setStatus(TenantSubscriptionStatus.PAUSED);
        sub.setIsActive(false);
        tenantSubscriptionRepository.save(sub);

        auditLogService.logPaddle(sub.getId(), AuditEventType.SUBSCRIPTION_STATUS_CHANGED,
                previous.name(), TenantSubscriptionStatus.PAUSED.name(), payload.getEventId());

        log.info("subscription.paused: userId={}, paddleSubId={}", sub.getUserId(), data.getId());
    }

    private void handleSubscriptionResumed(PaddleWebhookPayload payload) {
        PaddleSubscriptionEventData data = parseSubscriptionData(payload);

        TenantSubscriptionEntity sub = findSubscriptionByPaddleId(data.getId());
        if (sub == null) return;

        TenantSubscriptionStatus previous = sub.getStatus();
        sub.setStatus(TenantSubscriptionStatus.ACTIVE);
        sub.setIsActive(true);
        if (data.getCurrentBillingPeriod() != null) {
            sub.setEndDate(data.getCurrentBillingPeriod().getEndsAt());
        }
        tenantSubscriptionRepository.save(sub);

        auditLogService.logPaddle(sub.getId(), AuditEventType.SUBSCRIPTION_STATUS_CHANGED,
                previous.name(), TenantSubscriptionStatus.ACTIVE.name(), payload.getEventId());

        log.info("subscription.resumed: userId={}, paddleSubId={}", sub.getUserId(), data.getId());
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
            // ── Renewal: use Paddle's authoritative billing period end date ─────
            TenantSubscriptionEntity sub = tenantSubscriptionRepository
                    .findById(existingDetails.get().getTenantSubscriptionId())
                    .orElseGet(() -> {
                        log.warn("TenantSubscription not found for id={}", existingDetails.get().getTenantSubscriptionId());
                        return null;
                    });
            if (sub == null) return;

            // Prefer Paddle's billing period over local calculation to prevent date drift
            Instant newEndDate = (data.getBillingPeriod() != null && data.getBillingPeriod().getEndsAt() != null)
                    ? data.getBillingPeriod().getEndsAt()
                    : calculateRenewalEndDate(sub);

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

        // Provision immediately on first payment — guards against subscription.activated
        // arriving before transaction.completed (Paddle delivers events independently)
        if (subscription.getProvisioningStatus() == ProvisioningStatus.PENDING) {
            triggerProvisioning(subscription, data.getSubscriptionId(), data.getCustomerId());
        }

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

    /** Marks the checkout intent COMPLETED when a subscription is successfully activated. */
    private void markCheckoutIntentCompleted(PaddleCustomData customData) {
        if (customData == null) return;
        // We don't have the transaction ID here, but we can match by userId
        // Best-effort: mark the most recent PENDING intent for this user as COMPLETED
        try {
            if (customData.getUserId() != null) {
                Long userId = resolveUserId(customData.getUserId());
                checkoutIntentRepository
                        .findByStatusAndExpiresAtBefore(CheckoutIntentStatus.PENDING,
                                Instant.now().plus(48, java.time.temporal.ChronoUnit.HOURS))
                        .stream()
                        .filter(i -> i.getUserId().equals(userId))
                        .findFirst()
                        .ifPresent(intent -> {
                            intent.setStatus(CheckoutIntentStatus.COMPLETED);
                            checkoutIntentRepository.save(intent);
                        });
            }
        } catch (Exception e) {
            log.warn("Could not mark checkout intent completed: {}", e.getMessage());
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
