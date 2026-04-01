package org.example.loficonnect.commons.serviceImpl;

import lombok.extern.slf4j.Slf4j;
import org.example.loficonnect.auth.model.enitty.UserEntity;
import org.example.loficonnect.commons.exception.PlanLimitExceededException;
import org.example.loficonnect.commons.exception.SubscriptionInvalidException;
import org.example.loficonnect.commons.model.entity.SubscriptionPlanLimitEntity;
import org.example.loficonnect.commons.model.entity.TenantSubscriptionEntity;
import org.example.loficonnect.commons.repository.TenantSubscriptionRepository;
import org.example.loficonnect.commons.service.SubscriptionValidationService;
import org.example.loficonnect.repository.LofiConnectAppKeyRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class SubscriptionValidationServiceImpl implements SubscriptionValidationService {

    private static final String APP_KEY_LIMIT_KEY = "MAX_APP_KEYS";

    private final TenantSubscriptionRepository subscriptionRepository;
    private final LofiConnectAppKeyRepository appKeyRepository;

    public SubscriptionValidationServiceImpl(TenantSubscriptionRepository subscriptionRepository,
                                             LofiConnectAppKeyRepository appKeyRepository) {
        this.subscriptionRepository = subscriptionRepository;
        this.appKeyRepository = appKeyRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public TenantSubscriptionEntity validateAppKeyCreation(UserEntity user) {
        // 1. Ensure the user has an active, non-expired subscription
        TenantSubscriptionEntity subscription = subscriptionRepository
                .findFirstByTenantEntityAndStatusAndEndAtAfterAndIsActiveAndIsDeleted(
                        user, "ACTIVE", OffsetDateTime.now(), true, false)
                .orElseThrow(() -> new SubscriptionInvalidException(
                        "No active subscription found. Please subscribe to a plan to generate app keys."));

        // 2. Load this subscription's limits and look for the APP_KEY limit
        List<SubscriptionPlanLimitEntity> limits = subscription.getSubscriptionPlanEntity()
                .getSubscriptionPlanLimitEntities()
                .stream().toList();

        Optional<SubscriptionPlanLimitEntity> appKeyLimit = limits.stream()
                .filter(l -> APP_KEY_LIMIT_KEY.equalsIgnoreCase(l.getLimitKeyEntity().getLimitKey()))
                .findFirst();

        // 3. If no APP_KEY limit is configured for this plan, allow creation
        if (appKeyLimit.isEmpty()) {
            log.debug("No MAX_APP_KEYS limit configured for subscription [{}] — allowing app key creation.",
                    subscription.getId());
            return subscription;
        }

        // 4. Count the user's existing (active, non-deleted) app keys
        long currentCount = appKeyRepository.countByCreatedByAndIsActiveAndIsDeleted(user.getId(), true, false);
        long allowedLimit = appKeyLimit.get().getLimitValue();

        if (currentCount >= allowedLimit) {
            throw new PlanLimitExceededException(
                    "App key limit reached. Your current plan allows " + allowedLimit
                            + " app key(s). Please upgrade your plan to generate more.");
        }

        return subscription;
    }
}
