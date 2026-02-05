package org.example.loficonnect.model.mapper;

import lombok.experimental.UtilityClass;
import org.example.loficonnect.model.dto.LofiConnectAppKeyDTO;
import org.example.loficonnect.model.entity.LofiConnectAppKeyEntity;

import java.util.List;

@UtilityClass
public class LofiConnectAppKeyMapper {
    public static LofiConnectAppKeyEntity toEntity(String appKey,
                                                   String code,
                                                   String companyId,
                                                   String subAccountId,
                                                   String scopes,
                                                   String userType,
                                                   String userId) {
        LofiConnectAppKeyEntity entity = new LofiConnectAppKeyEntity();
        entity.setAppKey(appKey);
        entity.setCode(code);
        entity.setConnectionName("");
        entity.setCompanyId(companyId);
        entity.setSubAccountId(subAccountId);
        entity.setSubaccountName("");
        entity.setScopes(scopes);
        entity.setUserType(userType);
        entity.setUserId(userId);
        entity.setIsActive(false);
        entity.setIsDeleted(false);
        return entity;
    }

    public static LofiConnectAppKeyDTO toDto(LofiConnectAppKeyEntity entity) {
        LofiConnectAppKeyDTO dto = new LofiConnectAppKeyDTO();
        dto.setId(entity.getId());
        dto.setAppKey(entity.getAppKey());
        dto.setConnectionName(entity.getConnectionName());
        dto.setSubAccountName(entity.getSubaccountName());
        dto.setScopes(entity.getScopes().toString());
        dto.setSubAccountId(entity.getSubAccountId());
        return dto;
    }

    public static void update(LofiConnectAppKeyEntity entity,
                              String connectionName,
                              String subAccountName,
                              Boolean isActive) {
        entity.setConnectionName(connectionName);
        entity.setSubaccountName(subAccountName);
        entity.setIsActive(isActive);
    }
}
