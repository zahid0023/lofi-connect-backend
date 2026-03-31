package org.example.loficonnect.commons.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Getter
@Setter
@Entity
@Table(name = "subscription_plan_limits")
public class SubscriptionPlanLimitEntity extends AuditableEntity {

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "subscription_plan_id", nullable = false)
    private SubscriptionPlanEntity subscriptionPlanEntity;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "limit_key_id", nullable = false)
    private LimitKeyEntity limitKeyEntity;

    @NotNull
    @Column(name = "limit_value", nullable = false)
    private Long limitValue;
}