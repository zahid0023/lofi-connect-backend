package org.example.loficonnect.usage.controller;

import jakarta.validation.Valid;
import org.example.loficonnect.auth.model.dto.CustomUserDetails;
import org.example.loficonnect.commons.dto.request.PaginatedRequest;
import org.example.loficonnect.usage.model.enums.ApiPlatform;
import org.example.loficonnect.usage.service.ApiUsageQueryService;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;

/**
 * API usage log endpoints.
 *
 * <ul>
 *   <li>{@code GET /api/v1/usage-logs}        — admin: all logs with optional filters</li>
 *   <li>{@code GET /api/v1/usage-logs/me}      — tenant: caller's own logs</li>
 *   <li>{@code GET /api/v1/usage-logs/stats/me} — tenant: caller's aggregate stats</li>
 * </ul>
 */
@RestController
@RequestMapping("/api/v1/usage-logs")
public class ApiUsageLogController {

    private final ApiUsageQueryService queryService;

    public ApiUsageLogController(ApiUsageQueryService queryService) {
        this.queryService = queryService;
    }

    /**
     * Admin: paginated log list with optional filters.
     *
     * @param appKeyId filter by a specific app key ID
     * @param userId   filter by the user who owns the app key
     * @param platform filter by platform (e.g. GHL)
     * @param isError  filter to errors only (true) or successes only (false)
     * @param from     inclusive start timestamp (ISO-8601)
     * @param to       inclusive end timestamp (ISO-8601)
     */
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getAll(
            @RequestParam(required = false) Long appKeyId,
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) ApiPlatform platform,
            @RequestParam(required = false) Boolean isError,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant to,
            @Valid @ParameterObject PaginatedRequest pageRequest) {
        return ResponseEntity.ok(
                queryService.getLogsAdmin(appKeyId, userId, platform, isError, from, to, pageRequest));
    }

    /**
     * Tenant: paginated log list for the authenticated user's own app keys.
     *
     * @param isError filter to errors only (true) or successes only (false)
     * @param from    inclusive start timestamp (ISO-8601)
     * @param to      inclusive end timestamp (ISO-8601)
     */
    @GetMapping("/me")
    public ResponseEntity<?> getMyLogs(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestParam(required = false) Boolean isError,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant to,
            @Valid @ParameterObject PaginatedRequest pageRequest) {
        return ResponseEntity.ok(
                queryService.getMyLogs(userDetails.getId(), isError, from, to, pageRequest));
    }

    /**
     * Tenant: 4-card usage stats (total calls, success rate, errors, avg calls/day)
     * comparing the current period against the equally-sized previous period.
     *
     * @param range number of days to look back — 1 (24 h), 7, or 30 (default 7)
     */
    @GetMapping("/stats/me")
    public ResponseEntity<?> getMyUsageStats(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestParam(defaultValue = "7") int range) {
        return ResponseEntity.ok(
                queryService.getMyUsageStats(userDetails.getId(), range));
    }

    /**
     * Tenant: per-day breakdown of API calls and errors.
     * Returns one data point per calendar day (UTC) for the requested range,
     * with days that have no activity filled in as 0.
     *
     * @param range 1 (last 24 h = today only), 7, or 30 days (default 7)
     *
     * Example response:
     * <pre>
     * {
     *   "range_days": 7,
     *   "data": [
     *     { "date": "Jun 14", "api_calls": 127, "errors": 11 },
     *     { "date": "Jun 15", "api_calls": 94,  "errors": 3  },
     *     ...
     *   ]
     * }
     * </pre>
     */
    @GetMapping("/daily/me")
    public ResponseEntity<?> getMyDailyUsage(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestParam(defaultValue = "7") int range) {
        return ResponseEntity.ok(
                queryService.getMyDailyUsage(userDetails.getId(), range));
    }

    /**
     * Tenant: usage stats broken down per app key with its GHL connection details.
     * Every app key belonging to the caller is returned — including ones with no
     * activity (zero counts) and ones not yet connected to a CRM platform (connection: null).
     *
     * @param range 1 (last 24 h), 7, or 30 days (default 7)
     */
    @GetMapping("/by-connection/me")
    public ResponseEntity<?> getMyUsageByConnection(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestParam(defaultValue = "7") int range) {
        return ResponseEntity.ok(
                queryService.getMyUsageByConnection(userDetails.getId(), range));
    }
}
