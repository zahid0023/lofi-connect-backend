package org.example.loficonnect.subscription.repository;

import org.example.loficonnect.subscription.model.entity.SubscriptionPlanEntity;
import org.example.loficonnect.subscription.model.projection.SubscriptionPlanSummary;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SubscriptionPlanRepository extends JpaRepository<SubscriptionPlanEntity, Long> {

    Optional<SubscriptionPlanEntity> findByIdAndIsActiveAndIsDeleted(Long id, Boolean isActive, Boolean isDeleted);

    Page<@NonNull SubscriptionPlanSummary> findAllByIsActiveAndIsDeleted(Boolean isActive, Boolean isDeleted, Pageable pageable);

    Page<@NonNull SubscriptionPlanSummary> findAllByIsActiveAndIsDeletedAndIsPublic(Boolean isActive, Boolean isDeleted, Boolean isPublic, Pageable pageable);

    boolean existsByCodeAndIsDeleted(String code, Boolean isDeleted);
}
