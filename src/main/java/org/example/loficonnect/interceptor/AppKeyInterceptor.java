package org.example.loficonnect.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.example.loficonnect.auth.model.enitty.LofiConnectAppKeyEntity;
import org.example.loficonnect.auth.service.AppKeyService;
import org.example.loficonnect.commons.annotation.AppKey;
import org.example.loficonnect.commons.exception.AppKeyInvalidException;
import org.example.loficonnect.subscription.exception.NoActiveSubscriptionException;
import org.example.loficonnect.subscription.model.entity.TenantSubscriptionEntity;
import org.example.loficonnect.subscription.model.enums.TenantSubscriptionStatus;
import org.example.loficonnect.util.AppKeyContext;
import org.example.loficonnect.util.LocationContext;
import org.example.loficonnect.util.VersionContext;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import java.time.Instant;
import java.util.Set;

@Component
@Slf4j
public class AppKeyInterceptor implements HandlerInterceptor {

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";
    private static final Set<TenantSubscriptionStatus> VALID_STATUSES =
            Set.of(TenantSubscriptionStatus.ACTIVE, TenantSubscriptionStatus.TRIAL,
                    TenantSubscriptionStatus.GRACE_PERIOD);

    private final AppKeyService appKeyService;

    public AppKeyInterceptor(AppKeyService appKeyService) {
        this.appKeyService = appKeyService;
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

            // Validate the app key exists and is active in the database
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
}
