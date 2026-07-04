package org.example.loficonnect.subscription.scheduler;

import lombok.extern.slf4j.Slf4j;
import org.example.loficonnect.auth.model.enitty.UserEntity;
import org.example.loficonnect.auth.repository.UserRepository;
import org.example.loficonnect.payment.model.entity.CheckoutIntentEntity;
import org.example.loficonnect.payment.model.enums.CheckoutIntentStatus;
import org.example.loficonnect.payment.repository.CheckoutIntentRepository;
import org.example.loficonnect.subscription.model.entity.TenantSubscriptionEntity;
import org.example.loficonnect.subscription.model.enums.AuditEventType;
import org.example.loficonnect.subscription.model.enums.TenantSubscriptionStatus;
import org.example.loficonnect.subscription.repository.TenantSubscriptionRepository;
import org.example.loficonnect.subscription.service.AuditLogService;
import org.example.loficonnect.subscription.service.SubscriptionEmailService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

/**
 * Drives the subscription lifecycle state machine for time-based transitions that are
 * NOT triggered by Paddle webhooks:
 *
 * <ul>
 *   <li>PAST_DUE → GRACE_PERIOD (immediately, driven by the webhook handler itself)</li>
 *   <li>GRACE_PERIOD → READ_ONLY after {@value #GRACE_PERIOD_DAYS} days</li>
 *   <li>READ_ONLY → SUSPENDED after {@value #READ_ONLY_DAYS} days</li>
 *   <li>Checkout intents: reminder email after 24h, expire after 48h</li>
 * </ul>
 */
@Slf4j
@Component
public class SubscriptionLifecycleScheduler {

    static final int GRACE_PERIOD_DAYS = 5;
    static final int READ_ONLY_DAYS    = 7;

    private final TenantSubscriptionRepository subscriptionRepository;
    private final CheckoutIntentRepository checkoutIntentRepository;
    private final AuditLogService auditLogService;
    private final SubscriptionEmailService emailService;
    private final UserRepository userRepository;

    public SubscriptionLifecycleScheduler(
            TenantSubscriptionRepository subscriptionRepository,
            CheckoutIntentRepository checkoutIntentRepository,
            AuditLogService auditLogService,
            SubscriptionEmailService emailService,
            UserRepository userRepository) {
        this.subscriptionRepository = subscriptionRepository;
        this.checkoutIntentRepository = checkoutIntentRepository;
        this.auditLogService = auditLogService;
        this.emailService = emailService;
        this.userRepository = userRepository;
    }

    // ─── Subscription lifecycle transitions ───────────────────────────────────

    /**
     * Transitions PAST_DUE subscriptions to GRACE_PERIOD immediately.
     * In practice Paddle sends past_due webhook → we set PAST_DUE.
     * This job starts the grace clock and sends the warning email.
     * Runs every hour.
     */
    @Scheduled(fixedDelay = 3_600_000)
    @Transactional
    public void startGracePeriod() {
        List<TenantSubscriptionEntity> pastDue =
                subscriptionRepository.findByStatus(TenantSubscriptionStatus.PAST_DUE);

        for (TenantSubscriptionEntity sub : pastDue) {
            if (sub.getGracePeriodStartsAt() != null) continue; // already started

            sub.setStatus(TenantSubscriptionStatus.GRACE_PERIOD);
            sub.setGracePeriodStartsAt(Instant.now());
            subscriptionRepository.save(sub);

            auditLogService.logSystem(sub.getId(), AuditEventType.GRACE_PERIOD_STARTED,
                    TenantSubscriptionStatus.PAST_DUE.name(),
                    TenantSubscriptionStatus.GRACE_PERIOD.name());

            sendEmailSafely(sub.getUserId(), email ->
                    emailService.sendGraceWarning(email, sub));

            log.info("Grace period started: subscriptionId={}, userId={}", sub.getId(), sub.getUserId());
        }
    }

    /**
     * Transitions GRACE_PERIOD → READ_ONLY after {@value #GRACE_PERIOD_DAYS} days.
     * Runs every hour.
     */
    @Scheduled(fixedDelay = 3_600_000)
    @Transactional
    public void enforceReadOnly() {
        Instant cutoff = Instant.now().minus(GRACE_PERIOD_DAYS, ChronoUnit.DAYS);
        List<TenantSubscriptionEntity> graceSubs =
                subscriptionRepository.findByStatusAndGracePeriodStartsAtBefore(
                        TenantSubscriptionStatus.GRACE_PERIOD, cutoff);

        for (TenantSubscriptionEntity sub : graceSubs) {
            sub.setStatus(TenantSubscriptionStatus.READ_ONLY);
            sub.setReadOnlyStartsAt(Instant.now());
            subscriptionRepository.save(sub);

            auditLogService.logSystem(sub.getId(), AuditEventType.READ_ONLY_STARTED,
                    TenantSubscriptionStatus.GRACE_PERIOD.name(),
                    TenantSubscriptionStatus.READ_ONLY.name());

            sendEmailSafely(sub.getUserId(), email ->
                    emailService.sendAccessLimited(email, sub));

            log.warn("Subscription moved to READ_ONLY: subscriptionId={}, userId={}",
                    sub.getId(), sub.getUserId());
        }
    }

    /**
     * Transitions READ_ONLY → SUSPENDED after {@value #READ_ONLY_DAYS} days.
     * Runs every hour.
     */
    @Scheduled(fixedDelay = 3_600_000)
    @Transactional
    public void enforceSuspension() {
        Instant cutoff = Instant.now().minus(READ_ONLY_DAYS, ChronoUnit.DAYS);
        List<TenantSubscriptionEntity> readOnlySubs =
                subscriptionRepository.findByStatusAndReadOnlyStartsAtBefore(
                        TenantSubscriptionStatus.READ_ONLY, cutoff);

        for (TenantSubscriptionEntity sub : readOnlySubs) {
            sub.setStatus(TenantSubscriptionStatus.SUSPENDED);
            sub.setIsActive(false);
            sub.setSuspendedAt(Instant.now());
            subscriptionRepository.save(sub);

            auditLogService.logSystem(sub.getId(), AuditEventType.SUBSCRIPTION_SUSPENDED,
                    TenantSubscriptionStatus.READ_ONLY.name(),
                    TenantSubscriptionStatus.SUSPENDED.name());

            sendEmailSafely(sub.getUserId(), email ->
                    emailService.sendSuspended(email, sub));

            log.warn("Subscription SUSPENDED: subscriptionId={}, userId={}",
                    sub.getId(), sub.getUserId());
        }
    }

    // ─── Checkout intent lifecycle ────────────────────────────────────────────

    /**
     * Sends a 24-hour reminder email for pending checkouts that have not had a reminder.
     * Runs every 30 minutes.
     */
    @Scheduled(fixedDelay = 1_800_000)
    @Transactional
    public void sendCheckoutReminders() {
        Instant cutoff24h = Instant.now().minus(24, ChronoUnit.HOURS);
        List<CheckoutIntentEntity> toRemind =
                checkoutIntentRepository.findByStatusAndReminderSentAtIsNullAndCreatedAtBefore(
                        CheckoutIntentStatus.PENDING, cutoff24h);

        for (CheckoutIntentEntity intent : toRemind) {
            try {
                userRepository.findById(intent.getUserId()).ifPresent(user -> {
                    String planName = "your selected plan";
                    emailService.sendCheckoutReminder(user.getUsername(), planName);
                    intent.setReminderSentAt(Instant.now());
                    checkoutIntentRepository.save(intent);
                    auditLogService.logSystem(null, AuditEventType.CHECKOUT_REMINDER_SENT,
                            null, "userId=" + intent.getUserId());
                    log.info("Checkout reminder sent: userId={}", intent.getUserId());
                });
            } catch (Exception e) {
                log.error("Failed to send checkout reminder for intent {}: {}", intent.getId(), e.getMessage());
            }
        }
    }

    /**
     * Expires PENDING checkout intents that have passed their expiry time.
     * Runs every 30 minutes.
     */
    @Scheduled(fixedDelay = 1_800_000)
    @Transactional
    public void expireCheckoutIntents() {
        List<CheckoutIntentEntity> expired =
                checkoutIntentRepository.findByStatusAndExpiresAtBefore(
                        CheckoutIntentStatus.PENDING, Instant.now());

        for (CheckoutIntentEntity intent : expired) {
            intent.setStatus(CheckoutIntentStatus.EXPIRED);
            checkoutIntentRepository.save(intent);
            auditLogService.logSystem(null, AuditEventType.CHECKOUT_EXPIRED,
                    CheckoutIntentStatus.PENDING.name(), CheckoutIntentStatus.EXPIRED.name());
            log.info("Checkout intent expired: id={}, userId={}", intent.getId(), intent.getUserId());
        }
    }

    // ─── Helper ───────────────────────────────────────────────────────────────

    @FunctionalInterface
    private interface EmailSender {
        void send(String email);
    }

    private void sendEmailSafely(Long userId, EmailSender sender) {
        try {
            userRepository.findById(userId)
                    .map(UserEntity::getUsername)
                    .ifPresent(sender::send);
        } catch (Exception e) {
            log.error("Failed to send lifecycle email to userId={}: {}", userId, e.getMessage());
        }
    }
}
