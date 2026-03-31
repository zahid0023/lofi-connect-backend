package org.example.loficonnect.commons.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.OffsetDateTime;

@Getter
@Setter
@Entity
@Table(name = "tenant_subscription_limits")
public class TenantSubscriptionLimitEntity extends AuditableEntity {

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "tenant_subscription_id", nullable = false)
    private TenantSubscriptionEntity tenantSubscriptionEntity;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "limit_key_id", nullable = false)
    private LimitKeyEntity limitKeyEntity;

    @NotNull
    @Column(name = "limit_value", nullable = false)
    private Long limitValue;

}