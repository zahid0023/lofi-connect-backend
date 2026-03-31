package org.example.loficonnect.commons.model.mapper;

import lombok.experimental.UtilityClass;
import org.example.loficonnect.commons.dto.request.LimitKeyCreateRequest;
import org.example.loficonnect.commons.dto.request.LimitKeyUpdateRequest;
import org.example.loficonnect.commons.model.dto.LimitKeyDto;
import org.example.loficonnect.commons.model.entity.LimitKeyEntity;

@UtilityClass
public class LimitKeyMapper {
    public static LimitKeyEntity fromRequest(LimitKeyCreateRequest request) {
        LimitKeyEntity entity = new LimitKeyEntity();
        entity.setLimitKey(request.getLimitKey());
        entity.setDescription(request.getDescription());
        entity.setUnit(request.getUnit());
        return entity;
    }

    public static void update(LimitKeyUpdateRequest request, LimitKeyEntity entity) {
        entity.setLimitKey(request.getLimitKey());
        entity.setDescription(request.getDescription());
        entity.setUnit(request.getUnit());
    }

    public static LimitKeyDto toDto(LimitKeyEntity entity) {
        LimitKeyDto dto = new LimitKeyDto();
        dto.setId(entity.getId());
        dto.setLimitKey(entity.getLimitKey());
        dto.setDescription(entity.getDescription());
        dto.setUnit(entity.getUnit());
        return dto;
    }
}
