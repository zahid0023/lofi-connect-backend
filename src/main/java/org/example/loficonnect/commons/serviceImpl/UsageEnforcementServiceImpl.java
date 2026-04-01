package org.example.loficonnect.commons.serviceImpl;

import lombok.extern.slf4j.Slf4j;
import org.example.loficonnect.auth.model.enitty.LofiConnectAppKeyEntity;
import org.example.loficonnect.auth.model.enitty.UserEntity;
import org.example.loficonnect.auth.service.AppKeyService;
import org.example.loficonnect.commons.exception.AppKeyInvalidException;
import org.example.loficonnect.commons.exception.SubscriptionInvalidException;
import org.example.loficonnect.commons.model.entity.LimitKeyEntity;
import org.example.loficonnect.commons.model.entity.SubscriptionPlanLimitEntity;
import org.example.loficonnect.commons.model.entity.TenantApiLogEntity;
import org.example.loficonnect.commons.model.entity.TenantSubscriptionEntity;
import org.example.loficonnect.commons.model.entity.TenantUsageEntity;
import org.example.loficonnect.commons.repository.SubscriptionPlanLimitRepository;
import org.example.loficonnect.commons.repository.TenantApiLogRepository;
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
    private final TenantUsageRepository usageRepository;
    private final SubscriptionPlanLimitRepository planLimitRepository;
    private final TenantApiLogRepository apiLogRepository;

    public UsageEnforcementServiceImpl(AppKeyService appKeyService,
                                       TenantUsageRepository usageRepository,
                                       SubscriptionPlanLimitRepository planLimitRepository,
                                       TenantApiLogRepository apiLogRepository) {
        this.appKeyService = appKeyService;
        this.usageRepository = usageRepository;
        this.planLimitRepository = planLimitRepository;
        this.apiLogRepository = apiLogRepository;
    }

    @Override
    @Transactional
    public EnforcementResult enforce(String appKeyValue) {
        // 1. Validate app key exists and is active
        LofiConnectAppKeyEntity appKeyEntity = resolveAppKey(appKeyValue);

        // 2. Get subscription directly from the app key
        TenantSubscriptionEntity subscription = appKeyEntity.getTenantSubscriptionEntity();

        if (subscription == null
                || !"ACTIVE".equalsIgnoreCase(subscription.getStatus())
                || subscription.getEndAt().isBefore(OffsetDateTime.now())
                || Boolean.FALSE.equals(subscription.getIsActive())
                || Boolean.TRUE.equals(subscription.getIsDeleted())) {
            throw new SubscriptionInvalidException(
                    "App key is not linked to a valid active subscription.");
        }

        UserEntity tenant = subscription.getTenantEntity();
        return new EnforcementResult(tenant, appKeyEntity, subscription);
    }

    @Override
    @Transactional
    public void recordUsage(EnforcementResult result, String endpoint, String method, int statusCode) {
        LofiConnectAppKeyEntity appKey = result.appKey();
        TenantSubscriptionEntity subscription = result.subscription();

        // Increment per-app-key usage for each limit key defined on the plan
        List<SubscriptionPlanLimitEntity> planLimits = planLimitRepository
                .findAllBySubscriptionPlanEntityAndIsActiveAndIsDeleted(
                        subscription.getSubscriptionPlanEntity(), true, false);

        for (SubscriptionPlanLimitEntity planLimit : planLimits) {
            TenantUsageEntity usage = getOrCreateUsage(appKey, planLimit.getLimitKeyEntity());

            if (windowExpired(usage)) {
                resetWindow(usage);
            }

            usage.setUsageCount(usage.getUsageCount() + 1);
            usageRepository.save(usage);
        }

        // Save API log entry
        TenantApiLogEntity log = new TenantApiLogEntity();
        log.setTenantEntity(result.tenant());
        log.setAppKeyEntity(appKey);
        log.setEndpoint(endpoint);
        log.setMethod(method);
        log.setStatusCode(statusCode);
        apiLogRepository.save(log);
    }

    // ── private helpers ──────────────────────────────────────────────────────

    private LofiConnectAppKeyEntity resolveAppKey(String appKeyValue) {
        try {
            return appKeyService.getAppKeyEntity(appKeyValue);
        } catch (RuntimeException ex) {
            throw new AppKeyInvalidException("Invalid or inactive app key.");
        }
    }

    private TenantUsageEntity getOrCreateUsage(LofiConnectAppKeyEntity appKey,
                                               LimitKeyEntity limitKey) {
        return usageRepository
                .findByAppKeyEntityAndLimitKeyEntity(appKey, limitKey)
                .orElseGet(() -> {
                    OffsetDateTime now = OffsetDateTime.now();
                    TenantUsageEntity fresh = new TenantUsageEntity();
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
        if (unit == null) return from.plusDays(1);
        return switch (unit.toUpperCase()) {
            case "RPM" -> from.plusMinutes(1);
            case "MONTHLY" -> from.withDayOfMonth(1).plusMonths(1)
                    .withHour(0).withMinute(0).withSecond(0).withNano(0);
            default -> from.plusDays(1);
        };
    }
}
