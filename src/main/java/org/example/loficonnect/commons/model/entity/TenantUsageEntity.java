package org.example.loficonnect.commons.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.example.loficonnect.auth.model.enitty.LofiConnectAppKeyEntity;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.OffsetDateTime;

@Getter
@Setter
@Entity
@Table(name = "tenant_usage")
public class TenantUsageEntity extends AuditableEntity {

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "app_key_id", nullable = false)
    private LofiConnectAppKeyEntity appKeyEntity;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "limit_key_id", nullable = false)
    private LimitKeyEntity limitKeyEntity;

    @NotNull
    @ColumnDefault("0")
    @Column(name = "usage_count", nullable = false)
    private Long usageCount;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "tenant_subscription_id", nullable = false)
    private TenantSubscriptionEntity tenantSubscriptionEntity;

    @NotNull
    @Column(name = "window_start", nullable = false)
    private OffsetDateTime windowStart;

    @NotNull
    @Column(name = "window_end", nullable = false)
    private OffsetDateTime windowEnd;

}