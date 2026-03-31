package org.example.loficonnect.commons.repository;

import org.example.loficonnect.commons.model.entity.SubscriptionPlanEntity;
import org.example.loficonnect.commons.model.entity.SubscriptionPlanLimitEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SubscriptionPlanLimitRepository extends JpaRepository<SubscriptionPlanLimitEntity, Long> {
    List<SubscriptionPlanLimitEntity> findAllBySubscriptionPlanEntityAndIsActiveAndIsDeleted(
            SubscriptionPlanEntity plan, Boolean isActive, Boolean isDeleted);

    void deleteAllBySubscriptionPlanEntity(SubscriptionPlanEntity plan);
}
