package org.example.loficonnect.payment.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.example.loficonnect.payment.model.enums.CheckoutIntentStatus;

import java.time.Instant;

/**
 * Tracks a user's intent to subscribe, from checkout open through payment completion or expiry.
 * Created when the user calls POST /api/v1/payments/checkout.
 * Expires automatically 48 hours after creation if the subscription is not created.
 */
@Getter
@Setter
@Entity
@Table(name = "checkout_intents")
public class CheckoutIntentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "plan_id", nullable = false)
    private Long planId;

    /** Paddle transaction ID returned when checkout was created. */
    @Column(name = "paddle_transaction_id", length = 100)
    private String paddleTransactionId;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private CheckoutIntentStatus status = CheckoutIntentStatus.PENDING;

    /** When this intent expires (48 hours after creation). */
    @Column(name = "expires_at", nullable = false)
    private Instant expiresAt;

    /** When the 24-hour reminder email was sent. Null if not yet sent. */
    @Column(name = "reminder_sent_at")
    private Instant reminderSentAt;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @PrePersist
    protected void onCreate() {
        Instant now = Instant.now();
        this.createdAt = now;
        this.updatedAt = now;
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = Instant.now();
    }
}
