package org.example.loficonnect.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "go_high_level_tokens")
public class GoHighLevelTokenEntity {
    @Id
    @ColumnDefault("nextval('go_high_level_tokens_id_seq')")
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "app_key_id", nullable = false)
    private org.example.loficonnect.model.entity.LofiConnectAppKeyEntity appKey;

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

}