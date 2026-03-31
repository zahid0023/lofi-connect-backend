package org.example.loficonnect.commons.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.example.loficonnect.auth.model.enitty.UserEntity;
import org.hibernate.annotations.ColumnDefault;
import java.time.OffsetDateTime;

@Getter
@Setter
@Entity
@Table(name = "tenant_subscriptions")
public class TenantSubscriptionEntity extends AuditableEntity {
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "tenant_id", nullable = false)
    private UserEntity tenantEntity;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "subscription_plan_id", nullable = false)
    private SubscriptionPlanEntity subscriptionPlanEntity;

    @Size(max = 30)
    @NotNull
    @Column(name = "status", nullable = false, length = 30)
    private String status;

    @NotNull
    @Column(name = "start_at", nullable = false)
    private OffsetDateTime startAt;

    @NotNull
    @Column(name = "end_at", nullable = false)
    private OffsetDateTime endAt;

    @ColumnDefault("true")
    @Column(name = "auto_renew")
    private Boolean autoRenew;
}