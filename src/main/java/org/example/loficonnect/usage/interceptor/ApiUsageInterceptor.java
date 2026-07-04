package org.example.loficonnect.usage.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.example.loficonnect.usage.model.entity.ApiUsageLogEntity;
import org.example.loficonnect.usage.model.enums.ApiPlatform;
import org.example.loficonnect.usage.service.ApiUsageLogService;
import org.example.loficonnect.util.AppKeyContext;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.time.Instant;

/**
 * Saves an {@link ApiUsageLogEntity} after every successful app-key-authenticated request.
 * Registered after {@link org.example.loficonnect.interceptor.AppKeyInterceptor} in WebConfig,
 * so {@code AppKeyContext.getAppKeyId()} is already set when {@code afterCompletion} runs.
 * The save is async — it never adds latency to the response.
 */
@Slf4j
@Component
public class ApiUsageInterceptor implements HandlerInterceptor {

    private final ApiUsageLogService apiUsageLogService;

    public ApiUsageInterceptor(ApiUsageLogService apiUsageLogService) {
        this.apiUsageLogService = apiUsageLogService;
    }

    private static final ThreadLocal<Long> startNanos    = new ThreadLocal<>();
    private static final ThreadLocal<Instant> requestedAt = new ThreadLocal<>();

    @Override
    public boolean preHandle(@NonNull HttpServletRequest request,
                             @NonNull HttpServletResponse response,
                             @NonNull Object handler) {
        startNanos.set(System.nanoTime());
        requestedAt.set(Instant.now());
        return true;
    }

    @Override
    public void afterCompletion(@NonNull HttpServletRequest request,
                                @NonNull HttpServletResponse response,
                                @NonNull Object handler,
                                Exception ex) {
        try {
            Long appKeyId = AppKeyContext.getAppKeyId();
            if (appKeyId == null) {
                return; // request was rejected by AppKeyInterceptor — nothing to log
            }

            Long nanos = startNanos.get();
            long responseTimeMs = nanos != null ? (System.nanoTime() - nanos) / 1_000_000 : -1;
            Instant at = requestedAt.get() != null ? requestedAt.get() : Instant.now();

            ApiUsageLogEntity entry = new ApiUsageLogEntity();
            entry.setAppKeyId(appKeyId);
            entry.setPlatform(ApiPlatform.GHL);
            entry.setHttpMethod(request.getMethod());
            entry.setEndpoint(request.getRequestURI());
            entry.setIpAddress(request.getRemoteAddr());
            entry.setUserAgent(request.getHeader("User-Agent"));
            entry.setResponseStatus(response.getStatus());
            entry.setError(response.getStatus() >= 400);
            entry.setResponseTimeMs(responseTimeMs);
            entry.setRequestedAt(at);

            apiUsageLogService.save(entry);
        } catch (Exception logEx) {
            log.error("ApiUsageInterceptor failed to submit log: {}", logEx.getMessage());
        } finally {
            startNanos.remove();
            requestedAt.remove();
        }
    }
}
