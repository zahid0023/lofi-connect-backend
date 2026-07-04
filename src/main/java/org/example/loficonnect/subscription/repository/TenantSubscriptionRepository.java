package org.example.loficonnect.subscription.repository;

import org.example.loficonnect.payment.model.enums.ProvisioningStatus;
import org.example.loficonnect.payment.model.enums.ProductType;
import org.example.loficonnect.subscription.model.entity.TenantSubscriptionEntity;
import org.example.loficonnect.subscription.model.enums.TenantSubscriptionStatus;
import org.example.loficonnect.subscription.model.projection.TenantSubscriptionSummary;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface TenantSubscriptionRepository extends JpaRepository<TenantSubscriptionEntity, Long> {

    Optional<TenantSubscriptionEntity> findByUserIdAndStatusIn(Long userId, List<TenantSubscriptionStatus> statuses);

    boolean existsByUserIdAndStatusIn(Long userId, List<TenantSubscriptionStatus> statuses);

    /** Returns the most recent subscription for a user regardless of status. Used for payment status polling. */
    Optional<TenantSubscriptionEntity> findFirstByUserIdOrderByIdDesc(Long userId);

    Page<@NonNull TenantSubscriptionSummary> findAllByIsActiveAndIsDeleted(Boolean isActive, Boolean isDeleted, Pageable pageable);

    // ─── Admin dashboard count queries ────────────────────────────────────────

    long countByStatus(TenantSubscriptionStatus status);

    long countByStatusIn(List<TenantSubscriptionStatus> statuses);

    long countByCreatedAtAfterAndStatusIn(Instant after, List<TenantSubscriptionStatus> statuses);

    /** Count subscriptions that were cancelled after the given timestamp. */
    long countByCancelledAtAfterAndStatus(Instant after, TenantSubscriptionStatus status);

    /** Count active subscriptions by product type (STANDALONE vs BUNDLED). */
    @Query("""
            SELECT COUNT(ts) FROM TenantSubscriptionEntity ts
            WHERE ts.status IN :statuses
            AND ts.subscriptionPlan.productType = :productType
            AND ts.isActive = true AND ts.isDeleted = false
            """)
    long countByStatusInAndProductType(
            @Param("statuses") List<TenantSubscriptionStatus> statuses,
            @Param("productType") ProductType productType);

    /**
     * Estimates MRR by summing plan prices normalised to monthly:
     * MONTHLY → price, ANNUAL → price/12, QUARTERLY → price/3.
     */
    @Query("""
            SELECT COALESCE(SUM(
                CASE ts.subscriptionPlan.billingCycle
                    WHEN 'MONTHLY'   THEN ts.subscriptionPlan.price
                    WHEN 'ANNUAL'    THEN ts.subscriptionPlan.price / 12
                    WHEN 'QUARTERLY' THEN ts.subscriptionPlan.price / 3
                    ELSE 0
                END
            ), 0)
            FROM TenantSubscriptionEntity ts
            WHERE ts.status IN :statuses
            AND ts.isActive = true AND ts.isDeleted = false
            """)
    BigDecimal estimateMrr(@Param("statuses") List<TenantSubscriptionStatus> statuses);

    // ─── Provisioning queue (admin) ───────────────────────────────────────────

    /** Bundled subscriptions awaiting manual GHL provisioning. */
    @Query("""
            SELECT ts FROM TenantSubscriptionEntity ts
            WHERE ts.subscriptionPlan.productType = 'BUNDLED'
            AND ts.provisioningStatus IN :provisioningStatuses
            AND ts.isDeleted = false
            ORDER BY ts.createdAt ASC
            """)
    List<TenantSubscriptionEntity> findBundledAwaitingProvisioning(
            @Param("provisioningStatuses") List<ProvisioningStatus> provisioningStatuses);

    /** Count of bundled subscriptions awaiting manual GHL provisioning. */
    @Query("""
            SELECT COUNT(ts) FROM TenantSubscriptionEntity ts
            WHERE ts.subscriptionPlan.productType = 'BUNDLED'
            AND ts.provisioningStatus IN :provisioningStatuses
            AND ts.isDeleted = false
            """)
    long countBundledAwaitingProvisioning(
            @Param("provisioningStatuses") List<ProvisioningStatus> provisioningStatuses);

    /** All non-deleted subscriptions — admin list that shows all statuses. */
    Page<TenantSubscriptionSummary> findAllByIsDeleted(Boolean isDeleted, Pageable pageable);

    // ─── Lifecycle scheduler queries ──────────────────────────────────────────

    /** PAST_DUE subscriptions to move to GRACE_PERIOD. */
    List<TenantSubscriptionEntity> findByStatus(TenantSubscriptionStatus status);

    /** GRACE_PERIOD subscriptions whose grace period started before the given cutoff. */
    List<TenantSubscriptionEntity> findByStatusAndGracePeriodStartsAtBefore(
            TenantSubscriptionStatus status, Instant cutoff);

    /** READ_ONLY subscriptions whose read-only period started before the given cutoff. */
    List<TenantSubscriptionEntity> findByStatusAndReadOnlyStartsAtBefore(
            TenantSubscriptionStatus status, Instant cutoff);
}
