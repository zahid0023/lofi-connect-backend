package org.example.loficonnect.commons.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.example.loficonnect.commons.dto.response.ApiErrorResponse;
import org.example.loficonnect.commons.exception.AppKeyInvalidException;
import org.example.loficonnect.commons.exception.QuotaExceededException;
import org.example.loficonnect.commons.exception.SubscriptionInvalidException;
import org.example.loficonnect.commons.service.UsageEnforcementService;
import org.example.loficonnect.commons.service.UsageEnforcementService.EnforcementResult;
import org.jspecify.annotations.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@Slf4j
public class AppKeyUsageFilter extends OncePerRequestFilter {

    public static final String APP_KEY_HEADER = "Authorization";

    private final UsageEnforcementService usageEnforcementService;
    private final ObjectMapper objectMapper;

    public AppKeyUsageFilter(UsageEnforcementService usageEnforcementService, ObjectMapper objectMapper) {
        this.usageEnforcementService = usageEnforcementService;
        this.objectMapper = objectMapper;
    }

    @Override
    protected boolean shouldNotFilter(@NonNull HttpServletRequest request) {
        return !request.getServletPath().startsWith("/api/v1/ghl/");
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain chain)
            throws ServletException, IOException {

        String appKeyValue = request.getHeader(APP_KEY_HEADER);

        if (appKeyValue == null || appKeyValue.isBlank()) {
            writeError(response, HttpStatus.UNAUTHORIZED, "MISSING_APP_KEY",
                    "Request header '" + APP_KEY_HEADER + "' is required.");
            return;
        }

        EnforcementResult result;
        try {
            result = usageEnforcementService.enforce(appKeyValue);
        } catch (AppKeyInvalidException ex) {
            writeError(response, HttpStatus.UNAUTHORIZED, "INVALID_APP_KEY", ex.getMessage());
            return;
        } catch (SubscriptionInvalidException ex) {
            writeError(response, HttpStatus.FORBIDDEN, "SUBSCRIPTION_INVALID", ex.getMessage());
            return;
        } catch (QuotaExceededException ex) {
            writeError(response, HttpStatus.TOO_MANY_REQUESTS, "QUOTA_EXCEEDED", ex.getMessage());
            return;
        }

        // Proceed with the request
        chain.doFilter(request, response);

        // After response — record usage + log (best-effort, never block the response)
        try {
            usageEnforcementService.recordUsage(
                    result,
                    request.getRequestURI(),
                    request.getMethod(),
                    response.getStatus()
            );
        } catch (Exception e) {
            log.error("Failed to record usage for tenant={} appKey={}",
                    result.tenant().getId(), result.appKey().getId(), e);
        }
    }

    private void writeError(HttpServletResponse response,
                            HttpStatus status,
                            String error,
                            String message) throws IOException {
        ApiErrorResponse body = new ApiErrorResponse(null, status.value(), error, message);
        response.setStatus(status.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        objectMapper.writeValue(response.getWriter(), body);
    }
}
