package org.example.loficonnect.subscription.serviceImpl;

import lombok.extern.slf4j.Slf4j;
import org.example.loficonnect.subscription.model.entity.SubscriptionAuditLogEntity;
import org.example.loficonnect.subscription.model.enums.AuditEventType;
import org.example.loficonnect.subscription.repository.SubscriptionAuditLogRepository;
import org.example.loficonnect.subscription.service.AuditLogService;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class AuditLogServiceImpl implements AuditLogService {

    private final SubscriptionAuditLogRepository repository;

    public AuditLogServiceImpl(SubscriptionAuditLogRepository repository) {
        this.repository = repository;
    }

    @Async
    @Override
    public void log(Long subscriptionId, String actorType, String actorId, AuditEventType eventType,
                    String oldValue, String newValue, String paddleEventId, String ipAddress) {
        try {
            SubscriptionAuditLogEntity entry = new SubscriptionAuditLogEntity();
            entry.setTenantSubscriptionId(subscriptionId);
            entry.setActorType(actorType);
            entry.setActorId(actorId);
            entry.setEventType(eventType);
            entry.setOldValue(oldValue);
            entry.setNewValue(newValue);
            entry.setPaddleEventId(paddleEventId);
            entry.setIpAddress(ipAddress);
            repository.save(entry);
        } catch (Exception e) {
            log.error("Failed to persist audit log: event={}, subscriptionId={}: {}",
                    eventType, subscriptionId, e.getMessage());
        }
    }
}
