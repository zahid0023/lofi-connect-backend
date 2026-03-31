package org.example.loficonnect.commons.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.example.loficonnect.auth.model.enitty.LofiConnectAppKeyEntity;
import org.example.loficonnect.auth.model.enitty.UserEntity;

@Getter
@Setter
@Entity
@Table(name = "tenant_api_logs")
public class TenantApiLogEntity extends AuditableEntity {
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "tenant_id", nullable = false)
    private UserEntity tenantEntity;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "app_key_id", nullable = false)
    private LofiConnectAppKeyEntity appKeyEntity;

    @Size(max = 255)
    @Column(name = "endpoint")
    private String endpoint;

    @Size(max = 10)
    @Column(name = "method", length = 10)
    private String method;

    @Column(name = "status_code")
    private Integer statusCode;

}