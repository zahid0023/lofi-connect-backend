package org.example.loficonnect.subscription.repository;

import org.example.loficonnect.subscription.model.entity.SubscriptionPlanEntity;
import org.example.loficonnect.subscription.model.projection.SubscriptionPlanSummary;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface SubscriptionPlanRepository extends JpaRepository<SubscriptionPlanEntity, Long> {

    Optional<SubscriptionPlanEntity> findByIdAndIsActiveAndIsDeleted(Long id, Boolean isActive, Boolean isDeleted);

    Page<@NonNull SubscriptionPlanSummary> findAllByIsActiveAndIsDeleted(Boolean isActive, Boolean isDeleted, Pageable pageable);

    boolean existsByCodeAndIsDeleted(String code, Boolean isDeleted);

    @Query("""
            SELECT DISTINCT p FROM SubscriptionPlanEntity p
            LEFT JOIN FETCH p.limits l
            LEFT JOIN FETCH l.limitKey
            WHERE p.isActive = true AND p.isDeleted = false AND p.isPublic = true
            ORDER BY p.sortOrder ASC, p.id ASC
            """)
    List<SubscriptionPlanEntity> findAllPublicWithLimits();
}
