package org.example.loficonnect.usage.serviceImpl;

import lombok.extern.slf4j.Slf4j;
import org.example.loficonnect.usage.model.entity.ApiUsageLogEntity;
import org.example.loficonnect.usage.repository.ApiUsageLogRepository;
import org.example.loficonnect.usage.service.ApiUsageLogService;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ApiUsageLogServiceImpl implements ApiUsageLogService {

    private final ApiUsageLogRepository apiUsageLogRepository;

    public ApiUsageLogServiceImpl(ApiUsageLogRepository apiUsageLogRepository) {
        this.apiUsageLogRepository = apiUsageLogRepository;
    }

    @Async
    @Override
    public void save(ApiUsageLogEntity entry) {
        try {
            apiUsageLogRepository.save(entry);
        } catch (Exception e) {
            log.error("Failed to write API usage log: appKeyId={}, endpoint={}, error={}",
                    entry.getAppKeyId(), entry.getEndpoint(), e.getMessage());
        }
    }
}
