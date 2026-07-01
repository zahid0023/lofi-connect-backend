package org.example.loficonnect.payment.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.example.loficonnect.payment.model.enums.PaymentProvider;

import java.time.Instant;

/**
 * Stores payment-provider-specific data for a tenant subscription.
 * Kept separate from {@code TenantSubscriptionEntity} so the domain model
 * stays provider-agnostic.
 *
 * <p>One record per subscription. When a new payment provider is added,
 * only this entity (and its repository) needs to change.
 */
@Getter
@Setter
@Entity
@Table(
        name = "subscription_payment_details",
        indexes = {
                @Index(name = "idx_spd_paddle_subscription_id", columnList = "paddle_subscription_id", unique = true),
                @Index(name = "idx_spd_tenant_subscription_id", columnList = "tenant_subscription_id", unique = true)
        }
)
public class SubscriptionPaymentDetailsEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** FK to tenant_subscriptions. One-to-one relationship. */
    @Column(name = "tenant_subscription_id", nullable = false, unique = true)
    private Long tenantSubscriptionId;

    /** Which payment provider owns this subscription. */
    @Enumerated(EnumType.STRING)
    @Column(name = "payment_provider", nullable = false, length = 20)
    private PaymentProvider paymentProvider;

    /** Paddle subscription ID (e.g. "sub_01h..."). */
    @Column(name = "paddle_subscription_id", length = 100, unique = true)
    private String paddleSubscriptionId;

    /** Paddle customer ID (e.g. "ctm_01h..."). */
    @Column(name = "paddle_customer_id", length = 100)
    private String paddleCustomerId;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = Instant.now();
        updatedAt = Instant.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = Instant.now();
    }
}
