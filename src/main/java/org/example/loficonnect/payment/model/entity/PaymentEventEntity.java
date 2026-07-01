package org.example.loficonnect.payment.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.example.loficonnect.payment.model.enums.PaymentProvider;

import java.time.Instant;

/**
 * Persists every processed Paddle webhook event for idempotency and audit.
 * Before processing any event, the processor checks whether its {@code eventId} already exists here.
 */
@Getter
@Setter
@Entity
@Table(name = "payment_events", indexes = {
        @Index(name = "idx_payment_events_event_id", columnList = "event_id", unique = true)
})
public class PaymentEventEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Provider-assigned event ID (e.g. Paddle's evt_xxx). Used for deduplication. */
    @Column(name = "event_id", nullable = false, unique = true, length = 100)
    private String eventId;

    @Enumerated(EnumType.STRING)
    @Column(name = "provider", nullable = false, length = 20)
    private PaymentProvider provider;

    /** Paddle event type string, e.g. "subscription.created". */
    @Column(name = "event_type", nullable = false, length = 100)
    private String eventType;

    /** Raw webhook payload stored for debugging / replay. */
    @Column(name = "payload", columnDefinition = "text", nullable = false)
    private String payload;

    @Column(name = "processed_at", nullable = false)
    private Instant processedAt;
}
