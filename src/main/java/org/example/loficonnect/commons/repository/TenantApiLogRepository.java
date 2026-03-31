package org.example.loficonnect.commons.repository;

import org.example.loficonnect.commons.model.entity.TenantApiLogEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TenantApiLogRepository extends JpaRepository<TenantApiLogEntity, Long> {
}
