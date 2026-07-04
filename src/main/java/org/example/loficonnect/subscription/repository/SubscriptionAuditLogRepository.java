package org.example.loficonnect.subscription.repository;

import org.example.loficonnect.subscription.model.entity.SubscriptionAuditLogEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubscriptionAuditLogRepository extends JpaRepository<SubscriptionAuditLogEntity, Long> {

    Page<SubscriptionAuditLogEntity> findByTenantSubscriptionId(Long tenantSubscriptionId, Pageable pageable);
}
