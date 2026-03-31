package org.example.loficonnect.commons.repository;

import org.example.loficonnect.commons.model.entity.SubscriptionPlanEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SubscriptionPlanRepository extends JpaRepository<SubscriptionPlanEntity, Long> {
    Optional<SubscriptionPlanEntity> findByIdAndIsActiveAndIsDeleted(Long id, Boolean isActive, Boolean isDeleted);

    List<SubscriptionPlanEntity> findAllByIsActiveAndIsDeleted(Boolean isActive, Boolean isDeleted);
}
