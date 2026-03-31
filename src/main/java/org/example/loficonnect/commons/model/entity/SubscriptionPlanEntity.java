package org.example.loficonnect.commons.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.math.BigDecimal;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "subscription_plans")
public class SubscriptionPlanEntity extends AuditableEntity {

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "currency_id", nullable = false)
    private CurrencyEntity currencyEntity;

    @Size(max = 100)
    @NotNull
    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @ColumnDefault("0")
    @Column(name = "price", precision = 10, scale = 2)
    private BigDecimal price;

    @NotNull
    @Column(name = "description", nullable = false)
    private List<String> description;

    @OneToMany(mappedBy = "subscriptionPlanEntity")
    private Set<SubscriptionPlanLimitEntity> subscriptionPlanLimitEntities = new LinkedHashSet<>();

    @Size(max = 20)
    @NotNull
    @ColumnDefault("'MONTHLY'")
    @Column(name = "billing_cycle", nullable = false, length = 20)
    private String billingCycle;

    @NotNull
    @ColumnDefault("30")
    @Column(name = "duration_in_days", nullable = false)
    private Integer durationInDays;

}