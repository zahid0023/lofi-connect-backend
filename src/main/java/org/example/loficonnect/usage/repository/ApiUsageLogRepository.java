package org.example.loficonnect.usage.repository;

import org.example.loficonnect.usage.model.entity.ApiUsageLogEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApiUsageLogRepository extends JpaRepository<ApiUsageLogEntity, Long> {
}
