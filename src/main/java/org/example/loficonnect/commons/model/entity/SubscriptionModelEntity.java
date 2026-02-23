package org.example.loficonnect.commons.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "subscription_models")
public class SubscriptionModelEntity extends AuditableEntity {
    @Size(max = 100)
    @NotNull
    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @NotNull
    @Column(name = "description", nullable = false, length = Integer.MAX_VALUE)
    private String description;

    @NotNull
    @Column(name = "max_app_keys", nullable = false)
    private Integer maxAppKeys;

    @NotNull
    @Column(name = "api_calls_quota", nullable = false)
    private Long apiCallsQuota;

    @NotNull
    @Column(name = "quota_period_days", nullable = false)
    private Integer quotaPeriodDays;

    @NotNull
    @ColumnDefault("0")
    @Column(name = "price", nullable = false)
    private BigDecimal price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "currency_id")
    private CurrencyEntity currencyEntity;

    @NotNull
    @ColumnDefault("'{}'")
    @Column(name = "details", nullable = false)
    private List<String> details;

}