package org.example.loficonnect.model.mapper;

import lombok.experimental.UtilityClass;
import org.example.loficonnect.auth.dto.request.appkey.CreateAppKeyRequest;
import org.example.loficonnect.auth.util.AppKeyGenerator;
import org.example.loficonnect.auth.model.dto.LofiConnectAppKeyDTO;
import org.example.loficonnect.auth.model.enitty.LofiConnectAppKeyEntity;
import org.example.loficonnect.model.entity.GoHighLevelTokenEntity;

@UtilityClass
public class LofiConnectAppKeyMapper {
    public static LofiConnectAppKeyEntity toEntity(CreateAppKeyRequest request) {
        LofiConnectAppKeyEntity entity = new LofiConnectAppKeyEntity();
        entity.setAppKey(AppKeyGenerator.generateAppKey());
        entity.setName(request.getName());
        return entity;
    }

    public static LofiConnectAppKeyDTO toDto(LofiConnectAppKeyEntity entity) {
        LofiConnectAppKeyDTO dto = new LofiConnectAppKeyDTO();
        dto.setId(entity.getId());
        dto.setAppKey(entity.getAppKey());
        dto.setName(entity.getName());
        dto.setMaskedKey(entity.getAppKey().substring(0, 4) + "****" + entity.getAppKey().substring(entity.getAppKey().length() - 4));
        dto.setStatus("active");
        dto.setCreatedAt(entity.getCreatedAt().toString());
        dto.setUpdatedAt(entity.getUpdatedAt().toString());

        GoHighLevelTokenEntity goHighLevelTokenEntity = entity.getGoHighLevelTokens().stream().filter(GoHighLevelTokenEntity::getIsActive).findFirst().orElse(null);

        dto.setGhlConnection(goHighLevelTokenEntity != null ? GoHighLevelTokenMapper.toDTO(goHighLevelTokenEntity) : null);
        return dto;
    }

    public static void update(LofiConnectAppKeyEntity entity,
                              Boolean isActive) {
        entity.setIsActive(isActive);
    }
}
