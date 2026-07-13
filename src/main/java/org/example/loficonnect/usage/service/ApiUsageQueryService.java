package org.example.loficonnect.usage.service;

import org.example.loficonnect.commons.dto.request.PaginatedRequest;
import org.example.loficonnect.commons.dto.response.PaginatedResponse;
import org.example.loficonnect.usage.dto.response.ApiUsageLogResponse;
import org.example.loficonnect.usage.dto.response.ApiUsageStatsResponse;
import org.example.loficonnect.usage.dto.response.DailyUsageResponse;
import org.example.loficonnect.usage.dto.response.UsageByConnectionResponse;
import org.example.loficonnect.usage.dto.response.UsageStatsResponse;
import org.example.loficonnect.usage.model.enums.ApiPlatform;

import java.time.Instant;

public interface ApiUsageQueryService {

    /**
     * Admin: paginated list of all API usage logs with optional filters.
     *
     * @param appKeyId  filter by a specific app key (null = all)
     * @param userId    filter by the user who owns the app key (null = all)
     * @param platform  filter by platform (null = all)
     * @param isError   filter by error flag (null = all)
     * @param from      start of time window (null = unbounded)
     * @param to        end of time window (null = unbounded)
     */
    PaginatedResponse<ApiUsageLogResponse> getLogsAdmin(
            Long appKeyId, Long userId, ApiPlatform platform,
            Boolean isError, Instant from, Instant to,
            PaginatedRequest pageRequest);

    /**
     * Tenant: paginated list of the caller's own API usage logs.
     *
     * @param userId    the authenticated user's ID (logs for all their app keys)
     * @param isError   filter by error flag (null = all)
     * @param from      start of time window (null = unbounded)
     * @param to        end of time window (null = unbounded)
     */
    PaginatedResponse<ApiUsageLogResponse> getMyLogs(
            Long userId, Boolean isError, Instant from, Instant to,
            PaginatedRequest pageRequest);

    /**
     * Tenant: aggregate statistics for the caller's own API usage.
     *
     * @param userId the authenticated user's ID
     * @param from   start of time window (null = unbounded)
     * @param to     end of time window (null = unbounded)
     */
    ApiUsageStatsResponse getMyStats(Long userId, Instant from, Instant to);

    /**
     * Tenant: 4-card usage stats comparing the current period against the previous period.
     *
     * @param userId the authenticated user's ID
     * @param range  number of days to look back (e.g. 1 = 24 h, 7, 30)
     */
    UsageStatsResponse getMyUsageStats(Long userId, int range);

    /**
     * Tenant: per-day breakdown of API calls and errors.
     *
     * @param userId the authenticated user's ID
     * @param range  1 (24 h), 7, or 30 days
     */
    DailyUsageResponse getMyDailyUsage(Long userId, int range);

    /**
     * Tenant: usage stats broken down per app key, each annotated with its GHL connection info.
     * App keys with no activity in the window are included with zero counts.
     *
     * @param userId the authenticated user's ID
     * @param range  1 (24 h), 7, or 30 days
     */
    UsageByConnectionResponse getMyUsageByConnection(Long userId, int range);
}
