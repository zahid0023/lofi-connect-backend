package org.example.loficonnect.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.example.loficonnect.commons.model.entity.AuditableEntity;
import org.hibernate.annotations.ColumnDefault;

import java.time.Instant;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "lofi_connect_app_key")
public class LofiConnectAppKeyEntity extends AuditableEntity {

    @Column(name = "app_key", nullable = false, length = Integer.MAX_VALUE)
    private String appKey;

    @NotNull
    @ColumnDefault("''")
    @Column(name = "connection_name", nullable = false, length = Integer.MAX_VALUE)
    private String connectionName;

    @NotNull
    @ColumnDefault("''")
    @Column(name = "company_id", nullable = false, length = Integer.MAX_VALUE)
    private String companyId;

    @NotNull
    @ColumnDefault("''")
    @Column(name = "subaccount_name", nullable = false, length = Integer.MAX_VALUE)
    private String subaccountName;

    @NotNull
    @ColumnDefault("ARRAY[]")
    @Column(name = "scopes", nullable = false)
    private List<String> scopes;

    @OneToMany(mappedBy = "appKeyEntity")
    private Set<GoHighLevelTokenEntity> goHighLevelTokens = new LinkedHashSet<>();

    @Size(max = 255)
    @NotNull
    @ColumnDefault("''")
    @Column(name = "user_type", nullable = false)
    private String userType;

    @NotNull
    @ColumnDefault("''")
    @Column(name = "user_id", nullable = false, length = Integer.MAX_VALUE)
    private String userId;

    @NotNull
    @ColumnDefault("''")
    @Column(name = "sub_account_id", nullable = false, length = Integer.MAX_VALUE)
    private String subAccountId;

    @NotNull
    @ColumnDefault("''")
    @Column(name = "code", nullable = false, length = Integer.MAX_VALUE)
    private String code;

}