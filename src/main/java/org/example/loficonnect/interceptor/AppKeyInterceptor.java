package org.example.loficonnect.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.example.loficonnect.commons.annotation.AppKey;
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
        AppKeyContext.clearAppKeyHolder();
        VersionContext.clearVersionHolder();
        LocationContext.clearLocationIdHolder();
    }
}
