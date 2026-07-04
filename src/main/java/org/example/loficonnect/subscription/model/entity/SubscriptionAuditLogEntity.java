package org.example.loficonnect.subscription.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.example.loficonnect.subscription.model.enums.AuditEventType;

import java.time.Instant;

/**
 * Immutable audit log entry for all subscription lifecycle events.
 * Written asynchronously; never updated after creation.
 */
@Getter
@Setter
@Entity
@Table(name = "subscription_audit_logs")
public class SubscriptionAuditLogEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** The subscription this event relates to. Nullable for global events (e.g., webhook signature failure). */
    @Column(name = "tenant_subscription_id")
    private Long tenantSubscriptionId;

    /** Who triggered the event: USER | ADMIN | SYSTEM | PADDLE */
    @Column(name = "actor_type", nullable = false, length = 20)
    private String actorType;

    /** ID of the actor (user ID, admin ID, or null for SYSTEM/PADDLE). */
    @Column(name = "actor_id", length = 100)
    private String actorId;

    @Enumerated(EnumType.STRING)
    @Column(name = "event_type", nullable = false, length = 100)
    private AuditEventType eventType;

    @Column(name = "old_value", columnDefinition = "TEXT")
    private String oldValue;

    @Column(name = "new_value", columnDefinition = "TEXT")
    private String newValue;

    /** Paddle event ID for traceability back to webhook events. */
    @Column(name = "paddle_event_id", length = 100)
    private String paddleEventId;

    @Column(name = "ip_address", length = 50)
    private String ipAddress;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = Instant.now();
    }
}
