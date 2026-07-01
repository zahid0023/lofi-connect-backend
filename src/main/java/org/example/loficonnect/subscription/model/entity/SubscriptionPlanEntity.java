package org.example.loficonnect.subscription.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.example.loficonnect.commons.model.entity.AuditableEntity;
import org.example.loficonnect.currency.model.entity.CurrencyEntity;
import org.example.loficonnect.payment.model.enums.ProductType;
import org.example.loficonnect.subscription.model.enums.BillingCycle;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "subscription_plans")
public class SubscriptionPlanEntity extends AuditableEntity {
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "currency_id", nullable = false)
    private CurrencyEntity currencyEntity;

    @NotBlank
    @Size(max = 100)
    @Column(name = "code", nullable = false, length = 100, unique = true)
    private String code;

    @Column(name = "sort_order", nullable = false)
    private Integer sortOrder = 0;

    @NotBlank
    @Size(max = 100)
    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "price", precision = 10, scale = 2)
    private BigDecimal price = BigDecimal.ZERO;

    @NotNull
    @Column(name = "description", columnDefinition = "text[]", nullable = false)
    private String[] description;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "billing_cycle", nullable = false, length = 20)
    private BillingCycle billingCycle;

    @Column(name = "trial_period_days", nullable = false)
    private Integer trialPeriodDays = 0;

    @Column(name = "is_public", nullable = false)
    private Boolean isPublic = Boolean.TRUE;

    /**
     * Determines automated (STANDALONE) vs manual (BUNDLED) provisioning.
     */
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "product_type", nullable = false, length = 20)
    private ProductType productType = ProductType.STANDALONE;

    /**
     * Paddle Price ID for this plan (e.g. "pri_01h...").
     * Required for all paid plans. Set by admin after creating the price in Paddle.
     */
    @Column(name = "paddle_price_id", length = 100)
    private String paddlePriceId;

    @OneToMany(mappedBy = "subscriptionPlan", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SubscriptionPlanLimitEntity> limits = new ArrayList<>();

}
