package org.example.loficonnect.commons.model.mapper;

import lombok.experimental.UtilityClass;
import org.example.loficonnect.commons.dto.request.LimitKeyCreateRequest;
import org.example.loficonnect.commons.dto.request.LimitKeyRequest;
import org.example.loficonnect.commons.dto.request.LimitKeyUpdateRequest;
import org.example.loficonnect.commons.model.dto.LimitKeyDto;
import org.example.loficonnect.commons.model.entity.LimitKeyEntity;

@UtilityClass
public class LimitKeyMapper {

    public LimitKeyEntity create(LimitKeyCreateRequest request) {
        LimitKeyEntity entity = new LimitKeyEntity();
        entity.setCode(request.getCode());
        applyCommonFields(entity, request);
        return entity;
    }

    public void update(LimitKeyEntity entity, LimitKeyUpdateRequest request) {
        applyCommonFields(entity, request);
    }

    private void applyCommonFields(LimitKeyEntity entity, LimitKeyRequest request) {
        entity.setName(request.getName());
        entity.setDescription(request.getDescription());
        entity.setDataType(request.getDataType());
        entity.setCategory(request.getCategory());
        entity.setUnit(request.getUnit());
    }

    public LimitKeyDto toDto(LimitKeyEntity entity) {
        return LimitKeyDto.builder()
                .id(entity.getId())
                .code(entity.getCode())
                .name(entity.getName())
                .description(entity.getDescription())
                .dataType(entity.getDataType())
                .category(entity.getCategory())
                .unit(entity.getUnit())
                .build();
    }
}
