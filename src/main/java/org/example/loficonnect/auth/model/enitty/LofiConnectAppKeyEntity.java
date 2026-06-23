package org.example.loficonnect.auth.model.enitty;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.example.loficonnect.commons.model.entity.AuditableEntity;
import org.example.loficonnect.model.entity.GoHighLevelTokenEntity;
import org.hibernate.annotations.ColumnDefault;

import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "lofi_connect_app_key")
public class LofiConnectAppKeyEntity extends AuditableEntity {

    @Column(name = "app_key", nullable = false, length = Integer.MAX_VALUE)
    private String appKey;

    @OneToMany(mappedBy = "appKeyEntity")
    private Set<GoHighLevelTokenEntity> goHighLevelTokens = new LinkedHashSet<>();

    @NotNull
    @ColumnDefault("''")
    @Column(name = "name", nullable = false, length = Integer.MAX_VALUE)
    private String name;

}