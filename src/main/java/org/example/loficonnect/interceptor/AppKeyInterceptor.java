package org.example.loficonnect.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.loficonnect.config.AppKey;
import org.example.loficonnect.util.AppKeyContext;
import org.example.loficonnect.util.VersionContext;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class AppKeyInterceptor implements HandlerInterceptor {

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws Exception {
        if (handler instanceof HandlerMethod method) {
            // Check if method has @AppKey
            if (method.hasMethodAnnotation(AppKey.class)) {
                String header = request.getHeader(AUTHORIZATION_HEADER);

                if (header == null || !header.startsWith(BEARER_PREFIX)) {
                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Missing or invalid Authorization header");
                    return false;
                }

                String appKey = header.substring(BEARER_PREFIX.length()).trim();
                AppKeyContext.setAppKey(appKey);
                VersionContext.setVersion("2021-04-15");
            }
        }
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request,
                                HttpServletResponse response,
                                Object handler,
                                Exception ex) {
        AppKeyContext.clearAppKeyHolder();
        VersionContext.clearVersionHolder();
    }
}
