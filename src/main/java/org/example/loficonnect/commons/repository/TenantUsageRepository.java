package org.example.loficonnect.commons.repository;

import org.example.loficonnect.commons.model.entity.LimitKeyEntity;
import org.example.loficonnect.commons.model.entity.TenantSubscriptionEntity;
import org.example.loficonnect.commons.model.entity.TenantUsageEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TenantUsageRepository extends JpaRepository<TenantUsageEntity, Long> {
    Optional<TenantUsageEntity> findByTenantSubscriptionEntityAndLimitKeyEntity(
            TenantSubscriptionEntity subscription, LimitKeyEntity limitKey);
}
