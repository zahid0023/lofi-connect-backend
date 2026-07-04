package org.example.loficonnect.usage.service;

import org.example.loficonnect.usage.model.entity.ApiUsageLogEntity;

public interface ApiUsageLogService {

    /** Persists a usage log entry. Called asynchronously from the interceptor. */
    void save(ApiUsageLogEntity entry);
}
