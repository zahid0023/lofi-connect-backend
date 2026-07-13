package org.example.loficonnect.usage.service;

import org.example.loficonnect.subscription.exception.UsageLimitExceededException;

import java.time.Instant;

public interface ApiUsageCheckService {

    /**
     * Checks whether the user has remaining capacity under the {@code MONTHLY_OPERATIONS}
     * limit for their current billing period.
     *
     * <p>Counts all API calls made by <em>all</em> of the user's active app keys
     * since {@code periodStart} and compares against the plan's configured limit.
     *
     * <p>If no limit is configured for the plan the call is a no-op (unlimited plan).
     *
     * @param userId      the owner of the app keys being checked
     * @param planId      the active subscription plan (determines the limit value)
     * @param periodStart start of the current billing period — derived from {@code endDate}
     *                    minus the billing cycle duration, so it aligns with the
     *                    subscription renewal date, not the calendar month.
     * @return a {@link UsageLimitStatus} snapshot for rate-limit response headers
     * @throws UsageLimitExceededException if the limit has been reached
     */
    UsageLimitStatus checkMonthlyLimit(Long userId, Long planId, Instant periodStart);

    /**
     * Snapshot of the caller's usage for the current billing period.
     *
     * @param used      calls made since {@code periodStart}
     * @param limit     plan limit (-1 if unlimited)
     * @param unlimited true when no {@code MONTHLY_OPERATIONS} limit is configured
     */
    record UsageLimitStatus(long used, long limit, boolean unlimited) {
        public static UsageLimitStatus ofUnlimited() {
            return new UsageLimitStatus(0, -1, true);
        }
    }
}
