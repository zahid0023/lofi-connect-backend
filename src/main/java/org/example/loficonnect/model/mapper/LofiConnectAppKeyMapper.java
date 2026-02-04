package org.example.loficonnect.model.mapper;

import lombok.experimental.UtilityClass;
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
        entity.setScopes(List.of(scopes));
        entity.setUserType(userType);
        entity.setUserId(userId);
        entity.setIsActive(true);
        entity.setIsDeleted(false);
        return entity;
    }
}
