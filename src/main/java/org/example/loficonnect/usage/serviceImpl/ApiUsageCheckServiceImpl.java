package org.example.loficonnect.usage.serviceImpl;

import org.example.loficonnect.repository.LofiConnectAppKeyRepository;
import org.example.loficonnect.subscription.exception.UsageLimitExceededException;
import org.example.loficonnect.subscription.model.enums.LimitKeys;
import org.example.loficonnect.subscription.repository.SubscriptionPlanLimitRepository;
import org.example.loficonnect.usage.repository.ApiUsageLogRepository;
import org.example.loficonnect.usage.service.ApiUsageCheckService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

/**
 * Enforces the {@code MONTHLY_OPERATIONS} plan limit on every GHL API request.
 *
 * <p>Query sequence (only 2 queries for unlimited plans, 3 for limited ones):
 * <ol>
 *   <li>Look up the plan limit — short-circuit if not configured (unlimited plan).</li>
 *   <li>Get all app key IDs owned by this user.</li>
 *   <li>COUNT calls since the current billing period start.</li>
 * </ol>
 *
 * <p><b>Note on concurrency:</b> The count is non-atomic. Under very high concurrency near
 * the exact limit boundary, a small number of calls may exceed the cap before the next
 * request is blocked. This is acceptable for quota enforcement; exact enforcement would
 * require a Redis atomic counter.
 */
@Service
public class ApiUsageCheckServiceImpl implements ApiUsageCheckService {

    private final SubscriptionPlanLimitRepository planLimitRepository;
    private final LofiConnectAppKeyRepository     appKeyRepository;
    private final ApiUsageLogRepository           apiUsageLogRepository;

    public ApiUsageCheckServiceImpl(SubscriptionPlanLimitRepository planLimitRepository,
                                    LofiConnectAppKeyRepository appKeyRepository,
                                    ApiUsageLogRepository apiUsageLogRepository) {
        this.planLimitRepository  = planLimitRepository;
        this.appKeyRepository     = appKeyRepository;
        this.apiUsageLogRepository = apiUsageLogRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public UsageLimitStatus checkMonthlyLimit(Long userId, Long planId, Instant periodStart) {
        // 1. Is there a limit configured for this plan?
        var limitOpt = planLimitRepository
                .findBySubscriptionPlanIdAndLimitKeyCode(planId, LimitKeys.MONTHLY_OPERATIONS);

        if (limitOpt.isEmpty()) {
            return UsageLimitStatus.ofUnlimited(); // no limit = unlimited plan
        }

        long limitValue = limitOpt.get().getLimitValue();

        // 2. All active app key IDs for this user (shares the quota across keys)
        List<Long> keyIds = appKeyRepository
                .findByCreatedByAndIsActiveAndIsDeleted(userId, true, false)
                .stream()
                .map(k -> k.getId())
                .toList();

        if (keyIds.isEmpty()) {
            return new UsageLimitStatus(0, limitValue, false);
        }

        // 3. Count calls in the current billing period
        long used = apiUsageLogRepository.countByAppKeyIdsSincePeriodStart(keyIds, periodStart);

        if (used >= limitValue) {
            throw new UsageLimitExceededException(
                    "API call limit reached for the current billing period: " +
                    used + " of " + limitValue + " used. " +
                    "Your quota resets at the start of your next billing period.");
        }

        return new UsageLimitStatus(used, limitValue, false);
    }
}
