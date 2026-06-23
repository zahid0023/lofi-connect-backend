package org.example.loficonnect.subscription.model.mapper;

import lombok.experimental.UtilityClass;
import org.example.loficonnect.subscription.dto.request.limitkey.CreateLimitKeyRequest;
import org.example.loficonnect.subscription.dto.request.limitkey.LimitKeyRequest;
import org.example.loficonnect.subscription.dto.request.limitkey.UpdateLimitKeyRequest;
import org.example.loficonnect.subscription.model.dto.LimitKeyDto;
import org.example.loficonnect.subscription.model.entity.LimitKeyEntity;

@UtilityClass
public class LimitKeyMapper {

    public LimitKeyEntity create(CreateLimitKeyRequest request) {
        LimitKeyEntity entity = new LimitKeyEntity();
        entity.setCode(request.getCode());
        applyCommonFields(entity, request);
        return entity;
    }

    public void update(LimitKeyEntity entity, UpdateLimitKeyRequest request) {
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
