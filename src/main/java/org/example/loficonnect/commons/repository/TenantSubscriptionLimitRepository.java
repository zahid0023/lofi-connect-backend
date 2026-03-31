package org.example.loficonnect.commons.repository;

import org.example.loficonnect.commons.model.entity.TenantSubscriptionEntity;
import org.example.loficonnect.commons.model.entity.TenantSubscriptionLimitEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TenantSubscriptionLimitRepository extends JpaRepository<TenantSubscriptionLimitEntity, Long> {
    List<TenantSubscriptionLimitEntity> findAllByTenantSubscriptionEntityAndIsActiveAndIsDeleted(
            TenantSubscriptionEntity subscription, Boolean isActive, Boolean isDeleted);
}
