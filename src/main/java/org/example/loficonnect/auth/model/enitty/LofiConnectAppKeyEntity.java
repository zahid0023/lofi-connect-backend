package org.example.loficonnect.auth.model.enitty;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.example.loficonnect.commons.model.entity.AuditableEntity;
import org.example.loficonnect.model.entity.GoHighLevelTokenEntity;
import org.example.loficonnect.subscription.model.entity.TenantSubscriptionEntity;
import org.hibernate.annotations.ColumnDefault;

import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "lofi_connect_app_keys")
public class LofiConnectAppKeyEntity extends AuditableEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "tenant_subscription_id", nullable = false)
    private TenantSubscriptionEntity tenantSubscription;

    @Column(name = "app_key", nullable = false, length = Integer.MAX_VALUE)
    private String appKey;

    @OneToMany(mappedBy = "appKeyEntity")
    private Set<GoHighLevelTokenEntity> goHighLevelTokens = new LinkedHashSet<>();

    @NotNull
    @ColumnDefault("''")
    @Column(name = "name", nullable = false, length = Integer.MAX_VALUE)
    private String name;

}