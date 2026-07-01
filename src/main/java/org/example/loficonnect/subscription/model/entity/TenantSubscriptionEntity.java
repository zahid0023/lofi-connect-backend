package org.example.loficonnect.subscription.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.example.loficonnect.commons.model.entity.AuditableEntity;
import org.example.loficonnect.payment.model.enums.ProvisioningStatus;
import org.example.loficonnect.subscription.model.enums.TenantSubscriptionStatus;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "tenant_subscriptions")
public class TenantSubscriptionEntity extends AuditableEntity {

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "subscription_plan_id", nullable = false)
    private SubscriptionPlanEntity subscriptionPlan;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private TenantSubscriptionStatus status;

    @Column(name = "start_date", nullable = false)
    private Instant startDate;

    @Column(name = "end_date")
    private Instant endDate;

    @Column(name = "trial_ends_at")
    private Instant trialEndsAt;

    /**
     * Whether product access has been provisioned for this subscription.
     * Updated by the provisioning strategy after payment is confirmed.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "provisioning_status", nullable = false, length = 20)
    private ProvisioningStatus provisioningStatus = ProvisioningStatus.PENDING;
}
