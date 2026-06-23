package org.example.loficonnect.subscription.repository;

import org.example.loficonnect.subscription.model.entity.SubscriptionPlanLimitEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface SubscriptionPlanLimitRepository extends JpaRepository<SubscriptionPlanLimitEntity, Long> {

    @Modifying
    @Query("DELETE FROM SubscriptionPlanLimitEntity l WHERE l.subscriptionPlan.id = :planId")
    void deleteAllBySubscriptionPlanId(@Param("planId") Long planId);
}
