package org.example.loficonnect.commons.serviceImpl;

import lombok.extern.slf4j.Slf4j;
import org.example.loficonnect.auth.model.enitty.LofiConnectAppKeyEntity;
import org.example.loficonnect.auth.model.enitty.UserEntity;
import org.example.loficonnect.auth.service.AppKeyService;
import org.example.loficonnect.auth.service.UserService;
import org.example.loficonnect.commons.exception.AppKeyInvalidException;
import org.example.loficonnect.commons.exception.QuotaExceededException;
import org.example.loficonnect.commons.exception.SubscriptionInvalidException;
import org.example.loficonnect.commons.model.entity.LimitKeyEntity;
import org.example.loficonnect.commons.model.entity.TenantSubscriptionEntity;
import org.example.loficonnect.commons.model.entity.TenantUsageEntity;
import org.example.loficonnect.commons.repository.TenantSubscriptionRepository;
import org.example.loficonnect.commons.repository.TenantUsageRepository;
import org.example.loficonnect.commons.service.UsageEnforcementService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;

@Service
@Slf4j
public class UsageEnforcementServiceImpl implements UsageEnforcementService {

    private final AppKeyService appKeyService;
    private final UserService userService;
    private final TenantSubscriptionRepository subscriptionRepository;
    private final TenantUsageRepository usageRepository;

    public UsageEnforcementServiceImpl(AppKeyService appKeyService,
                                       UserService userService,
                                       TenantSubscriptionRepository subscriptionRepository,
                                       TenantUsageRepository usageRepository) {
        this.appKeyService = appKeyService;
        this.userService = userService;
        this.subscriptionRepository = subscriptionRepository;
        this.usageRepository = usageRepository;
    }

    @Override
    @Transactional
    public EnforcementResult enforce(String appKeyValue) {
        // 1. Validate app key
        LofiConnectAppKeyEntity appKeyEntity = resolveAppKey(appKeyValue);

        // 2. Resolve tenant from app key owner
        UserEntity tenantEntity = userService.getUserById(appKeyEntity.getCreatedBy());

        // 3. Verify active subscription
        TenantSubscriptionEntity subscriptionEntity = subscriptionRepository
                .findFirstByTenantEntityAndStatusAndEndAtAfterAndIsActiveAndIsDeleted(
                        tenantEntity, "ACTIVE", OffsetDateTime.now(), true, false)
                .orElseThrow(() -> new SubscriptionInvalidException(
                        "No active subscription found. Please subscribe to a plan."));
        return new EnforcementResult(tenantEntity, appKeyEntity, subscriptionEntity);
    }

    @Override
    @Transactional
    public void recordUsage(EnforcementResult result, String endpoint, String method, int statusCode) {
        LofiConnectAppKeyEntity appKey = result.appKey();
        TenantSubscriptionEntity subscription = result.subscription();

        // API logging will be implemented later
    }

    // ── private helpers ──────────────────────────────────────────────────────

    private LofiConnectAppKeyEntity resolveAppKey(String appKeyValue) {
        try {
            return appKeyService.getAppKeyEntity(appKeyValue);
        } catch (RuntimeException ex) {
            throw new AppKeyInvalidException("Invalid or inactive app key.");
        }
    }

    private TenantUsageEntity getOrCreateUsage(TenantSubscriptionEntity subscription,
                                               LofiConnectAppKeyEntity appKey,
                                               LimitKeyEntity limitKey) {
        return usageRepository
                .findByTenantSubscriptionEntityAndLimitKeyEntity(subscription, limitKey)
                .orElseGet(() -> {
                    OffsetDateTime now = OffsetDateTime.now();
                    TenantUsageEntity fresh = new TenantUsageEntity();
                    fresh.setTenantSubscriptionEntity(subscription);
                    fresh.setAppKeyEntity(appKey);
                    fresh.setLimitKeyEntity(limitKey);
                    fresh.setUsageCount(0L);
                    fresh.setWindowStart(now);
                    fresh.setWindowEnd(computeWindowEnd(limitKey.getUnit(), now));
                    return usageRepository.save(fresh);
                });
    }

    private boolean windowExpired(TenantUsageEntity usage) {
        return OffsetDateTime.now().isAfter(usage.getWindowEnd());
    }

    private void resetWindow(TenantUsageEntity usage) {
        OffsetDateTime now = OffsetDateTime.now();
        usage.setUsageCount(0L);
        usage.setWindowStart(now);
        usage.setWindowEnd(computeWindowEnd(usage.getLimitKeyEntity().getUnit(), now));
        usageRepository.save(usage);
    }

    private OffsetDateTime computeWindowEnd(String unit, OffsetDateTime from) {
        return switch (unit.toUpperCase()) {
            case "RPM" -> from.plusMinutes(1);
            case "MONTHLY" -> from.withDayOfMonth(1).plusMonths(1)
                    .withHour(0).withMinute(0).withSecond(0).withNano(0);
            default -> from.plusDays(1);
        };
    }

    private boolean isTimeBasedUnit(String unit) {
        if (unit == null) return true;
        return !switch (unit.toUpperCase()) {
            case "RPM", "MONTHLY" -> true;
            default -> false;
        };
    }
}
