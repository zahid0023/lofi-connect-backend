package org.example.loficonnect.usage.repository;

import org.example.loficonnect.usage.model.entity.ApiUsageLogEntity;
import org.example.loficonnect.usage.model.enums.ApiPlatform;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.List;

public interface ApiUsageLogRepository extends JpaRepository<ApiUsageLogEntity, Long> {

    /**
     * Admin: filter logs across all app keys.
     * Every parameter is optional (null means no filter on that column).
     */
    @Query("SELECT l FROM ApiUsageLogEntity l WHERE " +
           "(:appKeyId IS NULL OR l.appKeyId = :appKeyId) AND " +
           "(:platform IS NULL OR l.platform = :platform) AND " +
           "(:isError IS NULL OR l.error = :isError) AND " +
           "(:from IS NULL OR l.requestedAt >= :from) AND " +
           "(:to IS NULL OR l.requestedAt <= :to)")
    Page<ApiUsageLogEntity> findFiltered(
            @Param("appKeyId") Long appKeyId,
            @Param("platform") ApiPlatform platform,
            @Param("isError") Boolean isError,
            @Param("from") Instant from,
            @Param("to") Instant to,
            Pageable pageable);

    /**
     * Tenant: filter logs restricted to the caller's own app key IDs.
     */
    @Query("SELECT l FROM ApiUsageLogEntity l WHERE " +
           "l.appKeyId IN :appKeyIds AND " +
           "(:isError IS NULL OR l.error = :isError) AND " +
           "(:from IS NULL OR l.requestedAt >= :from) AND " +
           "(:to IS NULL OR l.requestedAt <= :to)")
    Page<ApiUsageLogEntity> findByAppKeyIds(
            @Param("appKeyIds") List<Long> appKeyIds,
            @Param("isError") Boolean isError,
            @Param("from") Instant from,
            @Param("to") Instant to,
            Pageable pageable);

    /**
     * Aggregate stats for a set of app keys over an optional time window.
     * Returns a single {@code Object[]} row:
     * [0] total count (Long), [1] error count (Long),
     * [2] avg response ms (Double), [3] min response ms (Long), [4] max response ms (Long).
     */
    @Query("SELECT COUNT(l), " +
           "SUM(CASE WHEN l.error = TRUE THEN 1 ELSE 0 END), " +
           "AVG(l.responseTimeMs), " +
           "MIN(l.responseTimeMs), " +
           "MAX(l.responseTimeMs) " +
           "FROM ApiUsageLogEntity l WHERE " +
           "l.appKeyId IN :appKeyIds AND " +
           "(:from IS NULL OR l.requestedAt >= :from) AND " +
           "(:to IS NULL OR l.requestedAt <= :to)")
    List<Object[]> getStatsByAppKeyIds(
            @Param("appKeyIds") List<Long> appKeyIds,
            @Param("from") Instant from,
            @Param("to") Instant to);

    /**
     * Aggregate stats for a set of app keys over a mandatory time window.
     * Both {@code from} and {@code to} must be non-null.
     * Used by the usage-stats endpoint where the period is always known.
     */
    @Query("SELECT COUNT(l), " +
           "SUM(CASE WHEN l.error = TRUE THEN 1 ELSE 0 END), " +
           "AVG(l.responseTimeMs), " +
           "MIN(l.responseTimeMs), " +
           "MAX(l.responseTimeMs) " +
           "FROM ApiUsageLogEntity l WHERE " +
           "l.appKeyId IN :appKeyIds AND " +
           "l.requestedAt >= :from AND l.requestedAt <= :to")
    List<Object[]> getStatsByAppKeyIdsAndPeriod(
            @Param("appKeyIds") List<Long> appKeyIds,
            @Param("from") Instant from,
            @Param("to") Instant to);

    /**
     * Counts all calls made by the given app keys since the start of the current billing period.
     * Used for enforcing the MONTHLY_OPERATIONS plan limit on every incoming GHL request.
     */
    @Query("SELECT COUNT(l) FROM ApiUsageLogEntity l WHERE " +
           "l.appKeyId IN :appKeyIds AND l.requestedAt >= :periodStart")
    long countByAppKeyIdsSincePeriodStart(
            @Param("appKeyIds") List<Long> appKeyIds,
            @Param("periodStart") Instant periodStart);

    /**
     * Per-app-key aggregation for a set of app keys over a fixed time window.
     * Returns one row per app key that has at least one log in the window:
     * [0] appKeyId (Long), [1] totalCalls (Long), [2] errorCalls (Long), [3] avgResponseMs (Double).
     * App keys with zero activity in the window are omitted — callers must fill gaps.
     */
    @Query("SELECT l.appKeyId, COUNT(l), " +
           "SUM(CASE WHEN l.error = TRUE THEN 1 ELSE 0 END), " +
           "AVG(l.responseTimeMs) " +
           "FROM ApiUsageLogEntity l WHERE " +
           "l.appKeyId IN :appKeyIds AND " +
           "l.requestedAt >= :from AND l.requestedAt <= :to " +
           "GROUP BY l.appKeyId")
    List<Object[]> getStatsByEachAppKey(
            @Param("appKeyIds") List<Long> appKeyIds,
            @Param("from") Instant from,
            @Param("to") Instant to);

    /**
     * Daily breakdown of call counts and error counts for a set of app keys.
     * Returns rows of: [year (int), month (int), day (int), totalCalls (long), errorCalls (long)].
     * Days with no calls are omitted — callers must fill gaps.
     */
    @Query("SELECT EXTRACT(YEAR FROM l.requestedAt), " +
           "EXTRACT(MONTH FROM l.requestedAt), " +
           "EXTRACT(DAY FROM l.requestedAt), " +
           "COUNT(l), " +
           "SUM(CASE WHEN l.error = TRUE THEN 1 ELSE 0 END) " +
           "FROM ApiUsageLogEntity l WHERE " +
           "l.appKeyId IN :appKeyIds AND " +
           "l.requestedAt >= :from AND l.requestedAt <= :to " +
           "GROUP BY EXTRACT(YEAR FROM l.requestedAt), " +
           "EXTRACT(MONTH FROM l.requestedAt), " +
           "EXTRACT(DAY FROM l.requestedAt) " +
           "ORDER BY EXTRACT(YEAR FROM l.requestedAt) ASC, " +
           "EXTRACT(MONTH FROM l.requestedAt) ASC, " +
           "EXTRACT(DAY FROM l.requestedAt) ASC")
    List<Object[]> getDailyStatsByAppKeyIds(
            @Param("appKeyIds") List<Long> appKeyIds,
            @Param("from") Instant from,
            @Param("to") Instant to);
}
