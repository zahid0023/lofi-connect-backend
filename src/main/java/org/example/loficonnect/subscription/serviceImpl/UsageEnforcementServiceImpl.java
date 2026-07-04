package org.example.loficonnect.subscription.serviceImpl;

import org.example.loficonnect.subscription.exception.NoActiveSubscriptionException;
import org.example.loficonnect.subscription.exception.UsageLimitExceededException;
import org.example.loficonnect.subscription.model.entity.TenantSubscriptionEntity;
import org.example.loficonnect.subscription.model.enums.TenantSubscriptionStatus;
import org.example.loficonnect.subscription.repository.SubscriptionPlanLimitRepository;
import org.example.loficonnect.subscription.repository.TenantSubscriptionRepository;
import org.example.loficonnect.subscription.service.UsageEnforcementService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UsageEnforcementServiceImpl implements UsageEnforcementService {

    /** Statuses where API operations are still permitted (grace period has full access). */
    private static final List<TenantSubscriptionStatus> ACTIVE_STATUSES =
            List.of(TenantSubscriptionStatus.ACTIVE, TenantSubscriptionStatus.TRIAL,
                    TenantSubscriptionStatus.GRACE_PERIOD);

    private final TenantSubscriptionRepository tenantSubscriptionRepository;
    private final SubscriptionPlanLimitRepository subscriptionPlanLimitRepository;

    public UsageEnforcementServiceImpl(
            TenantSubscriptionRepository tenantSubscriptionRepository,
            SubscriptionPlanLimitRepository subscriptionPlanLimitRepository) {
        this.tenantSubscriptionRepository = tenantSubscriptionRepository;
        this.subscriptionPlanLimitRepository = subscriptionPlanLimitRepository;
    }

    @Transactional(readOnly = true)
    @Override
    public void enforce(Long userId, String limitKeyCode, long currentUsage) {
        TenantSubscriptionEntity subscription = tenantSubscriptionRepository
                .findByUserIdAndStatusIn(userId, ACTIVE_STATUSES)
                .orElseThrow(() -> new NoActiveSubscriptionException(
                        "An active subscription is required."));

        subscriptionPlanLimitRepository
                .findBySubscriptionPlanIdAndLimitKeyCode(
                        subscription.getSubscriptionPlan().getId(), limitKeyCode)
                .ifPresent(limit -> {
                    if (currentUsage >= limit.getLimitValue()) {
                        throw new UsageLimitExceededException(
                                "Plan limit reached for '" + limitKeyCode + "': " +
                                currentUsage + " of " + limit.getLimitValue() + " used.");
                    }
                });
    }
}
