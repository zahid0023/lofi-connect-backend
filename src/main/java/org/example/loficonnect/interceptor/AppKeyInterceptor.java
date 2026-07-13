package org.example.loficonnect.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.example.loficonnect.auth.model.enitty.LofiConnectAppKeyEntity;
import org.example.loficonnect.auth.service.AppKeyService;
import org.example.loficonnect.commons.annotation.AppKey;
import org.example.loficonnect.commons.exception.AppKeyInvalidException;
import org.example.loficonnect.subscription.exception.NoActiveSubscriptionException;
import org.example.loficonnect.subscription.model.entity.SubscriptionPlanEntity;
import org.example.loficonnect.subscription.model.entity.TenantSubscriptionEntity;
import org.example.loficonnect.subscription.model.enums.BillingCycle;
import org.example.loficonnect.subscription.model.enums.TenantSubscriptionStatus;
import org.example.loficonnect.usage.service.ApiUsageCheckService;
import org.example.loficonnect.usage.service.ApiUsageCheckService.UsageLimitStatus;
import org.example.loficonnect.util.AppKeyContext;
import org.example.loficonnect.util.LocationContext;
import org.example.loficonnect.util.VersionContext;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import java.time.Instant;
import java.time.ZoneOffset;
import java.util.Set;

@Component
@Slf4j
public class AppKeyInterceptor implements HandlerInterceptor {

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";
    private static final Set<TenantSubscriptionStatus> VALID_STATUSES =
            Set.of(TenantSubscriptionStatus.ACTIVE, TenantSubscriptionStatus.TRIAL,
                    TenantSubscriptionStatus.GRACE_PERIOD);

    private final AppKeyService        appKeyService;
    private final ApiUsageCheckService apiUsageCheckService;

    public AppKeyInterceptor(AppKeyService appKeyService,
                             ApiUsageCheckService apiUsageCheckService) {
        this.appKeyService        = appKeyService;
        this.apiUsageCheckService = apiUsageCheckService;
    }

    @Override
    public boolean preHandle(@NonNull HttpServletRequest request,
                             @NonNull HttpServletResponse response,
                             @NonNull Object handler) throws Exception {
        AppKeyContext.clearAppKeyHolder();
        VersionContext.clearVersionHolder();
        LocationContext.clearLocationIdHolder();

        if (handler instanceof HandlerMethod method && method.hasMethodAnnotation(AppKey.class)) {
            String header = request.getHeader(AUTHORIZATION_HEADER);

            if (header == null || !header.startsWith(BEARER_PREFIX)) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Missing or invalid Authorization header");
                return false;
            }

            String appKeyValue = header.substring(BEARER_PREFIX.length()).trim();

            // Validate the app key exists and is active in the database.
            // findByAppKeyWithSubscription also eagerly fetches tenantSubscription + subscriptionPlan.
            LofiConnectAppKeyEntity appKeyEntity;
            try {
                appKeyEntity = appKeyService.getAppKeyEntity(appKeyValue);
            } catch (Exception ex) {
                throw new AppKeyInvalidException("Invalid app key");
            }

            // Check the linked subscription is in a valid state
            TenantSubscriptionEntity subscription = appKeyEntity.getTenantSubscription();
            if (!VALID_STATUSES.contains(subscription.getStatus())) {
                throw new NoActiveSubscriptionException(
                        "Your subscription is " + subscription.getStatus().name().toLowerCase() +
                        ". Please renew your subscription to continue using the API.");
            }

            // Check the subscription has not expired (null endDate = LIFETIME plan)
            if (subscription.getEndDate() != null && subscription.getEndDate().isBefore(Instant.now())) {
                throw new NoActiveSubscriptionException(
                        "Your subscription has expired. Please renew to continue using the API.");
            }

            // Enforce the MONTHLY_OPERATIONS plan limit for the current billing period
            SubscriptionPlanEntity plan = subscription.getSubscriptionPlan();
            Instant periodStart = resolvePeriodStart(subscription, plan.getBillingCycle());
            UsageLimitStatus limitStatus = apiUsageCheckService.checkMonthlyLimit(
                    subscription.getUserId(), plan.getId(), periodStart);

            // Attach rate-limit headers so API clients can track their quota
            applyRateLimitHeaders(response, subscription, limitStatus);

            AppKeyContext.setAppKey(appKeyValue);
            AppKeyContext.setAppKeyId(appKeyEntity.getId());
            VersionContext.setVersion("2021-04-15");
        }
        return true;
    }

    @Override
    public void afterCompletion(@NonNull HttpServletRequest request,
                                @NonNull HttpServletResponse response,
                                @NonNull Object handler,
                                Exception ex) {
        AppKeyContext.clearAppKeyHolder();
        VersionContext.clearVersionHolder();
        LocationContext.clearLocationIdHolder();
    }

    // ─── Helpers ──────────────────────────────────────────────────────────────

    /**
     * Derives the start of the current billing period from the subscription's {@code endDate}
     * and the plan's {@code billingCycle}.  This aligns with the user's renewal date rather
     * than the calendar month.
     *
     * <p>If {@code endDate} is null (LIFETIME plan) the subscription {@code startDate} is used.
     */
    private Instant resolvePeriodStart(TenantSubscriptionEntity sub, BillingCycle cycle) {
        Instant endDate = sub.getEndDate();
        if (endDate == null) {
            return sub.getStartDate(); // LIFETIME — count from original start
        }
        return switch (cycle) {
            case MONTHLY   -> endDate.atZone(ZoneOffset.UTC).minusMonths(1).toInstant();
            case QUARTERLY -> endDate.atZone(ZoneOffset.UTC).minusMonths(3).toInstant();
            case ANNUAL    -> endDate.atZone(ZoneOffset.UTC).minusYears(1).toInstant();
            case LIFETIME  -> sub.getStartDate();
        };
    }

    /**
     * Sets standard {@code X-RateLimit-*} response headers and stores the remaining count
     * in {@link AppKeyContext} so {@code ApiUsageInterceptor} can persist it in the log.
     */
    private void applyRateLimitHeaders(HttpServletResponse response,
                                       TenantSubscriptionEntity subscription,
                                       UsageLimitStatus status) {
        if (status.unlimited()) return;

        long remaining = Math.max(0, status.limit() - status.used() - 1); // -1 for current request
        response.setHeader("X-RateLimit-Limit",     String.valueOf(status.limit()));
        response.setHeader("X-RateLimit-Remaining", String.valueOf(remaining));

        // Reset epoch = start of next billing period = current endDate
        if (subscription.getEndDate() != null) {
            response.setHeader("X-RateLimit-Reset",
                    String.valueOf(subscription.getEndDate().getEpochSecond()));
        }

        AppKeyContext.setRateLimitRemaining((int) remaining);
        AppKeyContext.setRateLimitTotal(status.limit());
    }
}
