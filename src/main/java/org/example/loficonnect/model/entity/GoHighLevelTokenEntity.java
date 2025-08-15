package org.example.loficonnect.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.Instant;
import java.util.Map;

@Getter
@Setter
@Entity
@Table(name = "go_high_level_tokens")
public class GoHighLevelTokenEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "go_high_level_tokens_id_gen")
    @SequenceGenerator(name = "go_high_level_tokens_id_gen", sequenceName = "go_high_level_tokens_id_seq", allocationSize = 1)
    @Column(name = "id", nullable = false)
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

    @Column(name = "scopes", nullable = false, length = Integer.MAX_VALUE)
    private String scopes;

    @Column(name = "company_id", nullable = false)
    private String companyId;

    @Column(name = "location_id")
    private String locationId;

    @Column(name = "user_id", nullable = false)
    private String userId;

    @ColumnDefault("(CURRENT_TIMESTAMP AT TIME ZONE 'UTC')")
    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @ColumnDefault("(CURRENT_TIMESTAMP AT TIME ZONE 'UTC')")
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @Column(name = "user_type", nullable = false)
    private String userType;

    @PrePersist
    protected void onCreate() {
        createdAt = Instant.now();
        updatedAt = Instant.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = Instant.now();
    }

    public static GoHighLevelTokenEntity from(LofiConnectAppKeyEntity appKeyEntity, Map<String, Object> parameters) {
        GoHighLevelTokenEntity entity = new GoHighLevelTokenEntity();
        entity.setAppKeyEntity(appKeyEntity);
        entity.setAccessToken(parameters.get("access_token").toString());
        entity.setTokenType(parameters.get("token_type").toString());
        entity.setExpiresIn(Integer.parseInt(parameters.get("expires_in").toString()));
        entity.setRefreshToken(parameters.get("refresh_token").toString());
        entity.setRefreshTokenId(parameters.get("refreshTokenId").toString());
        entity.setUserType(parameters.get("userType").toString());
        entity.setScopes(parameters.get("scope").toString());
        entity.setCompanyId(parameters.get("companyId").toString());
        entity.setLocationId(parameters.get("locationId").toString());
        entity.setUserId(parameters.get("userId").toString());
        return entity;
    }

}