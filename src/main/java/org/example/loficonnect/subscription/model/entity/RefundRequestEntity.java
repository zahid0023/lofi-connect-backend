package org.example.loficonnect.subscription.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.example.loficonnect.subscription.model.enums.RefundRequestStatus;
import org.example.loficonnect.subscription.model.enums.TenantSubscriptionStatus;

import java.time.Instant;

/**
 * A user-submitted refund request pending admin review.
 * Admin approves/rejects; approved refunds are then processed manually in Paddle.
 */
@Getter
@Setter
@Entity
@Table(name = "refund_requests")
public class RefundRequestEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "tenant_subscription_id", nullable = false)
    private Long tenantSubscriptionId;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "reason", nullable = false, columnDefinition = "TEXT")
    private String reason;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private RefundRequestStatus status = RefundRequestStatus.PENDING;

    /**
     * Subscription status at the time the refund was submitted.
     * Used to restore the correct status if the refund is rejected.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "previous_status", length = 30)
    private TenantSubscriptionStatus previousStatus;

    /** Admin notes added during review. */
    @Column(name = "admin_notes", columnDefinition = "TEXT")
    private String adminNotes;

    /** ID of the admin who reviewed this request. */
    @Column(name = "reviewed_by")
    private Long reviewedBy;

    @Column(name = "reviewed_at")
    private Instant reviewedAt;

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
