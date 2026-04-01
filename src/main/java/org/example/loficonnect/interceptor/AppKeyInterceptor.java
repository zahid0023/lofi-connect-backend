package org.example.loficonnect.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.example.loficonnect.commons.annotation.AppKey;
import org.example.loficonnect.commons.service.UsageEnforcementService;
import org.example.loficonnect.commons.service.UsageEnforcementService.EnforcementResult;
import org.example.loficonnect.util.AppKeyContext;
import org.example.loficonnect.util.LocationContext;
import org.example.loficonnect.util.VersionContext;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@Slf4j
public class AppKeyInterceptor implements HandlerInterceptor {

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";

    private final UsageEnforcementService usageEnforcementService;

    public AppKeyInterceptor(UsageEnforcementService usageEnforcementService) {
        this.usageEnforcementService = usageEnforcementService;
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

            // Validate key, subscription, and quota — throws AppKeyInvalidException,
            // SubscriptionInvalidException, or QuotaExceededException on failure
            EnforcementResult result = usageEnforcementService.enforce(appKeyValue);
            request.setAttribute(EnforcementResult.REQUEST_ATTR, result);

            AppKeyContext.setAppKey(appKeyValue);
            VersionContext.setVersion("2021-04-15");
        }
        return true;
    }

    @Override
    public void afterCompletion(@NonNull HttpServletRequest request,
                                @NonNull HttpServletResponse response,
                                @NonNull Object handler,
                                Exception ex) {
        if (handler instanceof HandlerMethod method && method.hasMethodAnnotation(AppKey.class)) {
            EnforcementResult result = (EnforcementResult) request.getAttribute(EnforcementResult.REQUEST_ATTR);
            if (result != null) {
                try {
                    usageEnforcementService.recordUsage(
                            result,
                            request.getRequestURI(),
                            request.getMethod(),
                            response.getStatus()
                    );
                } catch (Exception recordEx) {
                    log.error("Failed to record usage for tenant={} appKey={}",
                            result.tenant().getId(), result.appKey().getId(), recordEx);
                }
            }
        }

        AppKeyContext.clearAppKeyHolder();
        VersionContext.clearVersionHolder();
        LocationContext.clearLocationIdHolder();
    }
}
