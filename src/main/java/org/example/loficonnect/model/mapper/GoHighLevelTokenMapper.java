package org.example.loficonnect.model.mapper;

import lombok.experimental.UtilityClass;
import org.example.loficonnect.model.dto.GoHighLevelTokenDTO;
import org.example.loficonnect.model.entity.GoHighLevelTokenEntity;
import org.example.loficonnect.auth.model.enitty.LofiConnectAppKeyEntity;

@UtilityClass
public class GoHighLevelTokenMapper {
    public static GoHighLevelTokenEntity toEntity(LofiConnectAppKeyEntity appKeyEntity,
                                                  String accessToken,
                                                  String tokenType,
                                                  Integer expiresIn,
                                                  String refreshToken,
                                                  String refreshTokenId,
                                                  String companyId,
                                                  String locationId,
                                                  String subAccountName,
                                                  String scopes,
                                                  String userType,
                                                  String userId) {
        GoHighLevelTokenEntity entity = new GoHighLevelTokenEntity();
        entity.setAppKeyEntity(appKeyEntity);
        entity.setAccessToken(accessToken);
        entity.setTokenType(tokenType);
        entity.setExpiresIn(expiresIn);
        entity.setRefreshToken(refreshToken);
        entity.setRefreshTokenId(refreshTokenId);
        entity.setIsActive(true);
        entity.setIsDeleted(false);
        entity.setCompanyId(companyId);
        entity.setLocationId(locationId);
        entity.setSubaccountName(subAccountName);
        entity.setScopes(scopes);
        entity.setUserType(userType);
        entity.setUserId(userId);
        return entity;
    }

    public static GoHighLevelTokenDTO toDTO(GoHighLevelTokenEntity entity) {
        GoHighLevelTokenDTO dto = new GoHighLevelTokenDTO();
        dto.setCompanyId(entity.getCompanyId());
        dto.setIsAgency(false);
        dto.setSubAccountId("");
        dto.setSubAccountName(entity.getSubaccountName());
        dto.setScopes(entity.getScopes());
        dto.setUserId(entity.getUserId());
        return dto;
    }
}
