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
import java.util.UUID;
import java.util.regex.Pattern;

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

    /**
     * Matches path segments that look like resource IDs:
     * pure numerics, UUIDs, or alphanumeric strings of 10+ characters (GHL IDs).
     */
    private static final Pattern ID_SEGMENT = Pattern.compile(
            "/(?:\\d+" +
            "|[a-fA-F0-9]{8}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{12}" +
            "|[a-zA-Z0-9]{10,}" +
            ")(?=/|$)");

    private static final ThreadLocal<Long>    startNanos  = new ThreadLocal<>();
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

            String uri = request.getRequestURI();

            ApiUsageLogEntity entry = new ApiUsageLogEntity();
            entry.setAppKeyId(appKeyId);
            entry.setPlatform(ApiPlatform.GHL);
            entry.setRequestId(resolveRequestId(request));
            entry.setHttpMethod(request.getMethod());
            entry.setEndpoint(uri);
            entry.setEndpointPattern(normalizeEndpoint(uri));
            entry.setIpAddress(extractIpAddress(request));
            entry.setUserAgent(request.getHeader("User-Agent"));
            entry.setResponseStatus(response.getStatus());
            entry.setError(response.getStatus() >= 400);
            entry.setResponseTimeMs(responseTimeMs);
            entry.setRateLimitRemaining(AppKeyContext.getRateLimitRemaining());
            entry.setRequestedAt(at);

            apiUsageLogService.save(entry);
        } catch (Exception logEx) {
            log.error("ApiUsageInterceptor failed to submit log: {}", logEx.getMessage());
        } finally {
            startNanos.remove();
            requestedAt.remove();
        }
    }

    // ─── Helpers ──────────────────────────────────────────────────────────────

    /** Returns the X-Request-ID / X-Correlation-ID header value, or generates a new UUID. */
    private String resolveRequestId(HttpServletRequest request) {
        String rid = request.getHeader("X-Request-ID");
        if (rid == null || rid.isBlank()) {
            rid = request.getHeader("X-Correlation-ID");
        }
        return (rid != null && !rid.isBlank()) ? rid.trim() : UUID.randomUUID().toString();
    }

    /**
     * Replaces ID-like path segments with {@code {id}}.
     * E.g. {@code /api/v1/ghl/contacts/abc123xyz456/notes} →
     *      {@code /api/v1/ghl/contacts/{id}/notes}
     */
    private String normalizeEndpoint(String uri) {
        return ID_SEGMENT.matcher(uri).replaceAll("/{id}");
    }

    /**
     * Extracts the real client IP, respecting X-Forwarded-For (first entry)
     * and X-Real-IP headers set by reverse proxies / load balancers.
     */
    private String extractIpAddress(HttpServletRequest request) {
        String xff = request.getHeader("X-Forwarded-For");
        if (xff != null && !xff.isBlank()) {
            return xff.split(",")[0].trim();
        }
        String realIp = request.getHeader("X-Real-IP");
        if (realIp != null && !realIp.isBlank()) {
            return realIp.trim();
        }
        return request.getRemoteAddr();
    }
}
