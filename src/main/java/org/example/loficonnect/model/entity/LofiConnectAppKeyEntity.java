package org.example.loficonnect.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.time.Instant;
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "lofi_connect_app_key")
public class LofiConnectAppKeyEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "lofi_connect_app_key_id_gen")
    @SequenceGenerator(name = "lofi_connect_app_key_id_gen", sequenceName = "lofi_connect_app_key_id_seq", allocationSize = 1)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "app_key", nullable = false, length = Integer.MAX_VALUE)
    private String appKey;

    @Column(name = "created_by", nullable = false)
    private String createdBy;

    @Column(name = "updated_by", nullable = false)
    private String updatedBy;

    @ColumnDefault("(CURRENT_TIMESTAMP AT TIME ZONE 'UTC')")
    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @ColumnDefault("(CURRENT_TIMESTAMP AT TIME ZONE 'UTC')")
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @ColumnDefault("true")
    @Column(name = "is_active")
    private Boolean isActive;

    @ColumnDefault("true")
    @Column(name = "is_sub_account", nullable = false)
    private Boolean isSubAccount = false;

    @OneToMany(mappedBy = "appKey")
    private Set<GoHighLevelTokenEntity> goHighLevelTokens = new LinkedHashSet<>();

}