package org.example.loficonnect.subscription.repository;

import org.example.loficonnect.subscription.model.entity.TenantSubscriptionEntity;
import org.example.loficonnect.subscription.model.enums.TenantSubscriptionStatus;
import org.example.loficonnect.subscription.model.projection.TenantSubscriptionSummary;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TenantSubscriptionRepository extends JpaRepository<TenantSubscriptionEntity, Long> {

    Optional<TenantSubscriptionEntity> findByUserIdAndStatusIn(Long userId, List<TenantSubscriptionStatus> statuses);

    boolean existsByUserIdAndStatusIn(Long userId, List<TenantSubscriptionStatus> statuses);

    /** Returns the most recent subscription for a user regardless of status. Used for payment status polling. */
    Optional<TenantSubscriptionEntity> findFirstByUserIdOrderByIdDesc(Long userId);

    Page<@NonNull TenantSubscriptionSummary> findAllByIsActiveAndIsDeleted(Boolean isActive, Boolean isDeleted, Pageable pageable);
}
