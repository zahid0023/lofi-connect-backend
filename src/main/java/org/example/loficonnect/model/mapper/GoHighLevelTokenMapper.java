package org.example.loficonnect.model.mapper;

import lombok.experimental.UtilityClass;
import org.example.loficonnect.model.entity.GoHighLevelTokenEntity;
import org.example.loficonnect.model.entity.LofiConnectAppKeyEntity;

import java.time.OffsetDateTime;

@UtilityClass
public class GoHighLevelTokenMapper {
    public static GoHighLevelTokenEntity toEntity(LofiConnectAppKeyEntity appKeyEntity,
                                                  String accessToken,
                                                  String tokenType,
                                                  Integer expiresIn,
                                                  String refreshToken,
                                                  String refreshTokenId) {
        GoHighLevelTokenEntity entity = new GoHighLevelTokenEntity();
        entity.setAppKeyEntity(appKeyEntity);
        entity.setAccessToken(accessToken);
        entity.setTokenType(tokenType);
        entity.setExpiresIn(expiresIn);
        entity.setRefreshToken(refreshToken);
        entity.setRefreshTokenId(refreshTokenId);
        entity.setIsActive(true);
        entity.setIsDeleted(false);
        entity.setCreatedAt(OffsetDateTime.now());
        return entity;
    }
}
