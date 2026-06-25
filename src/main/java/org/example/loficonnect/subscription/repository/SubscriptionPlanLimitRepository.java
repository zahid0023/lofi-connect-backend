package org.example.loficonnect.subscription.repository;

import org.example.loficonnect.subscription.model.entity.SubscriptionPlanLimitEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface SubscriptionPlanLimitRepository extends JpaRepository<SubscriptionPlanLimitEntity, Long> {

    @Modifying
    @Query("DELETE FROM SubscriptionPlanLimitEntity l WHERE l.subscriptionPlan.id = :planId")
    void deleteAllBySubscriptionPlanId(@Param("planId") Long planId);

    @Query("SELECT l FROM SubscriptionPlanLimitEntity l WHERE l.subscriptionPlan.id = :planId AND l.limitKey.code = :code")
    Optional<SubscriptionPlanLimitEntity> findBySubscriptionPlanIdAndLimitKeyCode(@Param("planId") Long planId, @Param("code") String code);
}
