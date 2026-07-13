package org.example.loficonnect.usage.serviceImpl;

import lombok.extern.slf4j.Slf4j;
import org.example.loficonnect.commons.dto.request.PaginatedRequest;
import org.example.loficonnect.commons.dto.response.PaginatedResponse;
import org.example.loficonnect.commons.model.entity.AuditableEntity;
import org.example.loficonnect.repository.LofiConnectAppKeyRepository;
import org.example.loficonnect.auth.model.enitty.LofiConnectAppKeyEntity;
import org.example.loficonnect.model.entity.GoHighLevelTokenEntity;
import org.example.loficonnect.usage.dto.response.ApiUsageLogResponse;
import org.example.loficonnect.usage.dto.response.ApiUsageStatsResponse;
import org.example.loficonnect.usage.dto.response.ConnectionInfo;
import org.example.loficonnect.usage.dto.response.ConnectionUsageItem;
import org.example.loficonnect.usage.dto.response.DailyUsagePoint;
import org.example.loficonnect.usage.dto.response.DailyUsageResponse;
import org.example.loficonnect.usage.dto.response.StatCard;
import org.example.loficonnect.usage.dto.response.UsageByConnectionResponse;
import org.example.loficonnect.usage.dto.response.UsageStatsResponse;
import org.example.loficonnect.usage.model.entity.ApiUsageLogEntity;
import org.example.loficonnect.usage.model.enums.ApiPlatform;
import org.example.loficonnect.usage.model.enums.StatIcon;
import org.example.loficonnect.usage.model.enums.StatTrend;
import org.example.loficonnect.usage.repository.ApiUsageLogRepository;
import org.example.loficonnect.usage.service.ApiUsageQueryService;
import org.example.loficonnect.util.Pagination;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ApiUsageQueryServiceImpl implements ApiUsageQueryService {

    private static final Set<String> ALLOWED_SORT_FIELDS = Set.of(
            "id", "requestedAt", "responseTimeMs", "responseStatus", "appKeyId");

    private final ApiUsageLogRepository repository;
    private final LofiConnectAppKeyRepository appKeyRepository;

    public ApiUsageQueryServiceImpl(ApiUsageLogRepository repository,
                                    LofiConnectAppKeyRepository appKeyRepository) {
        this.repository = repository;
        this.appKeyRepository = appKeyRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public PaginatedResponse<ApiUsageLogResponse> getLogsAdmin(
            Long appKeyId, Long userId, ApiPlatform platform,
            Boolean isError, Instant from, Instant to,
            PaginatedRequest pageRequest) {

        // If admin filters by userId, resolve their app keys and use the first one found.
        // If multiple keys exist for that user, treat them as an OR by querying per user's keys.
        if (userId != null && appKeyId == null) {
            List<Long> userKeyIds = resolveAppKeyIds(userId);
            if (userKeyIds.isEmpty()) {
                return emptyPage(pageRequest);
            }
            Page<ApiUsageLogEntity> page = repository.findByAppKeyIds(
                    userKeyIds, isError, from, to,
                    pageRequest.toPageable(ALLOWED_SORT_FIELDS));
            return Pagination.buildPaginatedResponse(page.map(this::toResponse));
        }

        Page<ApiUsageLogEntity> page = repository.findFiltered(
                appKeyId, platform, isError, from, to,
                pageRequest.toPageable(ALLOWED_SORT_FIELDS));
        return Pagination.buildPaginatedResponse(page.map(this::toResponse));
    }

    @Override
    @Transactional(readOnly = true)
    public PaginatedResponse<ApiUsageLogResponse> getMyLogs(
            Long userId, Boolean isError, Instant from, Instant to,
            PaginatedRequest pageRequest) {

        List<Long> appKeyIds = resolveAppKeyIds(userId);
        if (appKeyIds.isEmpty()) {
            return emptyPage(pageRequest);
        }
        Page<ApiUsageLogEntity> page = repository.findByAppKeyIds(
                appKeyIds, isError, from, to,
                pageRequest.toPageable(ALLOWED_SORT_FIELDS));
        return Pagination.buildPaginatedResponse(page.map(this::toResponse));
    }

    @Override
    @Transactional(readOnly = true)
    public ApiUsageStatsResponse getMyStats(Long userId, Instant from, Instant to) {
        List<Long> appKeyIds = resolveAppKeyIds(userId);
        if (appKeyIds.isEmpty()) {
            return emptyStats(from, to);
        }

        List<Object[]> rows = repository.getStatsByAppKeyIds(appKeyIds, from, to);
        Object[] row = (rows != null && !rows.isEmpty()) ? rows.get(0) : null;
        long total = row != null && row[0] != null ? ((Number) row[0]).longValue() : 0L;
        long errors = row != null && row[1] != null ? ((Number) row[1]).longValue() : 0L;
        Double avg   = row != null && row[2] != null ? ((Number) row[2]).doubleValue() : null;
        Long   min   = row != null && row[3] != null ? ((Number) row[3]).longValue()   : null;
        Long   max   = row != null && row[4] != null ? ((Number) row[4]).longValue()   : null;

        double errorRate = total > 0 ? (errors * 100.0) / total : 0.0;

        return ApiUsageStatsResponse.builder()
                .totalRequests(total)
                .errorRequests(errors)
                .errorRate(Math.round(errorRate * 100.0) / 100.0)
                .avgResponseTimeMs(avg)
                .minResponseTimeMs(min)
                .maxResponseTimeMs(max)
                .from(from)
                .to(to)
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public UsageStatsResponse getMyUsageStats(Long userId, int range) {
        List<Long> appKeyIds = resolveAppKeyIds(userId);

        Instant now = Instant.now();
        Instant curFrom = now.minus(range, ChronoUnit.DAYS);
        Instant prevFrom = curFrom.minus(range, ChronoUnit.DAYS);

        // Current period stats
        List<Object[]> curRows  = appKeyIds.isEmpty() ? null : repository.getStatsByAppKeyIdsAndPeriod(appKeyIds, curFrom, now);
        List<Object[]> prevRows = appKeyIds.isEmpty() ? null : repository.getStatsByAppKeyIdsAndPeriod(appKeyIds, prevFrom, curFrom);
        Object[] cur  = (curRows  != null && !curRows.isEmpty())  ? curRows.get(0)  : null;
        Object[] prev = (prevRows != null && !prevRows.isEmpty()) ? prevRows.get(0) : null;

        long curTotal = extract(cur, 0);
        long curErrors = extract(cur, 1);
        long prevTotal = extract(prev, 0);
        long prevErrors = extract(prev, 1);

        double curSuccessRate = curTotal > 0 ? ((curTotal - curErrors) * 100.0) / curTotal : 0.0;
        double prevSuccessRate = prevTotal > 0 ? ((prevTotal - prevErrors) * 100.0) / prevTotal : 0.0;

        double curAvg = curTotal / (double) range;
        double prevAvg = prevTotal / (double) range;

        return UsageStatsResponse.builder()
                .totalCalls(buildCard((double) curTotal, prevTotal > 0 ? (double) prevTotal : null, StatIcon.ZAP))
                .successRate(buildCard(curSuccessRate, prevTotal > 0 ? prevSuccessRate : null, StatIcon.TRENDING_UP))
                .errors(buildCard(curErrors, prevErrors > 0 ? (double) prevErrors : null, StatIcon.ALERT_CIRCLE))
                .avgCallsPerDay(buildCard(curAvg, prevTotal > 0 ? prevAvg : null, StatIcon.ACTIVITY))
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public DailyUsageResponse getMyDailyUsage(Long userId, int range) {
        if (range != 1 && range != 7 && range != 30) {
            range = 7;
        }

        List<Long> appKeyIds = resolveAppKeyIds(userId);
        Instant now = Instant.now();
        Instant from = now.minus(range, ChronoUnit.DAYS);

        // Index DB rows by LocalDate for O(1) gap-filling
        Map<LocalDate, long[]> statsByDay = new HashMap<>();
        if (!appKeyIds.isEmpty()) {
            List<Object[]> rows = repository.getDailyStatsByAppKeyIds(appKeyIds, from, now);
            for (Object[] row : rows) {
                int year  = ((Number) row[0]).intValue();
                int month = ((Number) row[1]).intValue();
                int day   = ((Number) row[2]).intValue();
                long calls = ((Number) row[3]).longValue();
                long errs  = row[4] != null ? ((Number) row[4]).longValue() : 0L;
                statsByDay.put(LocalDate.of(year, month, day), new long[]{calls, errs});
            }
        }

        // Generate every day in range (oldest → newest), filling gaps with 0
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("MMM d", Locale.ENGLISH);
        LocalDate today = LocalDate.now(ZoneOffset.UTC);
        List<DailyUsagePoint> data = new ArrayList<>(range);
        for (int i = range - 1; i >= 0; i--) {
            LocalDate date = today.minusDays(i);
            long[] stats = statsByDay.getOrDefault(date, new long[]{0L, 0L});
            data.add(DailyUsagePoint.builder()
                    .date(date.format(fmt))
                    .apiCalls(stats[0])
                    .errors(stats[1])
                    .build());
        }

        return DailyUsageResponse.builder()
                .rangeDays(range)
                .data(data)
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public UsageByConnectionResponse getMyUsageByConnection(Long userId, int range) {
        if (range != 1 && range != 7 && range != 30) {
            range = 7;
        }

        // Load app keys with GHL tokens in one query (no N+1)
        List<LofiConnectAppKeyEntity> appKeys = appKeyRepository.findByCreatedByWithGhlTokens(userId);

        if (appKeys.isEmpty()) {
            return UsageByConnectionResponse.builder()
                    .rangeDays(range)
                    .data(Collections.emptyList())
                    .build();
        }

        List<Long> appKeyIds = appKeys.stream().map(AuditableEntity::getId).collect(Collectors.toList());

        Instant now = Instant.now();
        Instant from = now.minus(range, ChronoUnit.DAYS);

        // Single aggregation query — one row per app key that has activity
        List<Object[]> rows = repository.getStatsByEachAppKey(appKeyIds, from, now);

        // Index by appKeyId for O(1) lookup
        Map<Long, Object[]> statMap = new HashMap<>();
        for (Object[] row : rows) {
            statMap.put(((Number) row[0]).longValue(), row);
        }

        List<ConnectionUsageItem> data = new ArrayList<>(appKeys.size());
        for (LofiConnectAppKeyEntity key : appKeys) {
            // Pick the single active, non-deleted GHL token (if any)
            GoHighLevelTokenEntity activeToken = key.getGoHighLevelTokens().stream()
                    .filter(t -> Boolean.TRUE.equals(t.getIsActive()) && Boolean.FALSE.equals(t.getIsDeleted()))
                    .findFirst()
                    .orElse(null);

            ConnectionInfo connection = null;
            if (activeToken != null) {
                connection = ConnectionInfo.builder()
                        .platform("GHL")
                        .companyId(activeToken.getCompanyId())
                        .subaccountName(activeToken.getSubaccountName())
                        .locationId(activeToken.getLocationId())
                        .userType(activeToken.getUserType())
                        .build();
            }

            Object[] stat = statMap.get(key.getId());
            long totalCalls = stat != null && stat[1] != null ? ((Number) stat[1]).longValue() : 0L;
            long errors     = stat != null && stat[2] != null ? ((Number) stat[2]).longValue() : 0L;
            Double avgMs    = stat != null && stat[3] != null ? ((Number) stat[3]).doubleValue() : null;
            double errorRate = totalCalls > 0 ? Math.round((errors * 100.0 / totalCalls) * 100.0) / 100.0 : 0.0;

            data.add(ConnectionUsageItem.builder()
                    .appKeyId(key.getId())
                    .appKeyName(key.getName())
                    .connected(activeToken != null)
                    .connection(connection)
                    .totalCalls(totalCalls)
                    .errors(errors)
                    .errorRate(errorRate)
                    .avgResponseTimeMs(avgMs != null ? Math.round(avgMs * 100.0) / 100.0 : null)
                    .build());
        }

        return UsageByConnectionResponse.builder()
                .rangeDays(range)
                .data(data)
                .build();
    }

    // ─── Helpers ──────────────────────────────────────────────────────────────

    private List<Long> resolveAppKeyIds(Long userId) {
        return appKeyRepository
                .findByCreatedByAndIsActiveAndIsDeleted(userId, true, false)
                .stream()
                .map(AuditableEntity::getId)
                .collect(Collectors.toList());
    }

    private ApiUsageLogResponse toResponse(ApiUsageLogEntity e) {
        ApiUsageLogResponse r = new ApiUsageLogResponse();
        r.setId(e.getId());
        r.setAppKeyId(e.getAppKeyId());
        r.setPlatform(e.getPlatform());
        r.setRequestId(e.getRequestId());
        r.setHttpMethod(e.getHttpMethod());
        r.setEndpoint(e.getEndpoint());
        r.setEndpointPattern(e.getEndpointPattern());
        r.setIpAddress(e.getIpAddress());
        r.setUserAgent(e.getUserAgent());
        r.setRequestSizeBytes(e.getRequestSizeBytes());
        r.setResponseStatus(e.getResponseStatus());
        r.setResponseSizeBytes(e.getResponseSizeBytes());
        r.setResponseTimeMs(e.getResponseTimeMs());
        r.setError(e.isError());
        r.setErrorCode(e.getErrorCode());
        r.setRateLimitRemaining(e.getRateLimitRemaining());
        r.setRequestedAt(e.getRequestedAt());
        return r;
    }

    private PaginatedResponse<ApiUsageLogResponse> emptyPage(PaginatedRequest pageRequest) {
        Page<ApiUsageLogResponse> empty = new PageImpl<>(
                Collections.emptyList(),
                PageRequest.of(pageRequest.getPage(), pageRequest.getSize()),
                0);
        return Pagination.buildPaginatedResponse(empty);
    }

    private ApiUsageStatsResponse emptyStats(Instant from, Instant to) {
        return ApiUsageStatsResponse.builder()
                .totalRequests(0).errorRequests(0).errorRate(0.0)
                .from(from).to(to)
                .build();
    }

    /**
     * Extracts a long from a getStatsByAppKeyIds result row (null-safe).
     */
    private long extract(Object[] row, int idx) {
        if (row == null || row[idx] == null) return 0L;
        return ((Number) row[idx]).longValue();
    }

    /**
     * Builds a {@link StatCard} from current/previous values.
     * {@code prevValue == null} means no meaningful baseline (prev period had 0 activity).
     */
    private StatCard buildCard(double curValue, Double prevValue, StatIcon icon) {
        Double changePct = null;
        StatTrend trend = StatTrend.NEUTRAL;

        if (prevValue != null) {
            changePct = Math.round(((curValue - prevValue) / prevValue) * 10000.0) / 100.0;
            if (changePct > 0) trend = StatTrend.UP;
            else if (changePct < 0) trend = StatTrend.DOWN;
        }

        return StatCard.builder()
                .value(Math.round(curValue * 100.0) / 100.0)
                .changePercentage(changePct)
                .trend(trend)
                .icon(icon)
                .build();
    }
}
