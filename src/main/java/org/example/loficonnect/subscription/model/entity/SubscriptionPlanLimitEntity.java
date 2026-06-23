package org.example.loficonnect.subscription.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.example.loficonnect.commons.model.entity.AuditableEntity;

@Getter
@Setter
@Entity
@Table(name = "subscription_plan_limits")
public class SubscriptionPlanLimitEntity extends AuditableEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "subscription_plan_id", nullable = false)
    private SubscriptionPlanEntity subscriptionPlan;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "limit_key_id", nullable = false)
    private LimitKeyEntity limitKey;

    @Column(name = "limit_value", nullable = false)
    private Long limitValue;
}
