package org.example.loficonnect.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.example.loficonnect.commons.model.entity.AuditableEntity;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.util.Map;

@Getter
@Setter
@Entity
@Table(name = "go_high_level_tokens")
public class GoHighLevelTokenEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "app_key_id", nullable = false)
    private LofiConnectAppKeyEntity appKeyEntity;

    @Column(name = "access_token", nullable = false, length = Integer.MAX_VALUE)
    private String accessToken;

    @Column(name = "token_type", nullable = false, length = 50)
    private String tokenType;

    @Column(name = "expires_in", nullable = false)
    private Integer expiresIn;

    @Column(name = "refresh_token", nullable = false, length = Integer.MAX_VALUE)
    private String refreshToken;

    @Column(name = "refresh_token_id", nullable = false, length = Integer.MAX_VALUE)
    private String refreshTokenId;

    @NotNull
    @ColumnDefault("1")
    @Column(name = "version", nullable = false)
    private Long version = 1L;

    @NotNull
    @ColumnDefault("true")
    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    @NotNull
    @ColumnDefault("false")
    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted = false;

    @NotNull
    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

}