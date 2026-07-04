package org.example.loficonnect.subscription.service;

import org.example.loficonnect.subscription.model.enums.AuditEventType;

public interface AuditLogService {

    void log(Long subscriptionId, String actorType, String actorId, AuditEventType eventType,
             String oldValue, String newValue, String paddleEventId, String ipAddress);

    default void logSystem(Long subscriptionId, AuditEventType eventType, String oldValue, String newValue) {
        log(subscriptionId, "SYSTEM", null, eventType, oldValue, newValue, null, null);
    }

    default void logPaddle(Long subscriptionId, AuditEventType eventType,
                           String oldValue, String newValue, String paddleEventId) {
        log(subscriptionId, "PADDLE", null, eventType, oldValue, newValue, paddleEventId, null);
    }

    default void logUser(Long subscriptionId, Long userId, AuditEventType eventType,
                         String oldValue, String newValue) {
        log(subscriptionId, "USER", userId != null ? userId.toString() : null,
                eventType, oldValue, newValue, null, null);
    }

    default void logAdmin(Long subscriptionId, Long adminId, AuditEventType eventType,
                          String oldValue, String newValue) {
        log(subscriptionId, "ADMIN", adminId != null ? adminId.toString() : null,
                eventType, oldValue, newValue, null, null);
    }
}
