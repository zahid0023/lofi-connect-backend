package org.example.loficonnect.currency.model.mapper;

import lombok.experimental.UtilityClass;
import org.example.loficonnect.currency.dto.request.CurrencyCreateRequest;
import org.example.loficonnect.currency.dto.request.CurrencyRequest;
import org.example.loficonnect.currency.dto.request.CurrencyUpdateRequest;
import org.example.loficonnect.currency.model.dto.CurrencyDto;
import org.example.loficonnect.currency.model.entity.CurrencyEntity;

@UtilityClass
public class CurrencyMapper {

    public CurrencyEntity create(CurrencyCreateRequest request) {
        CurrencyEntity entity = new CurrencyEntity();
        entity.setCode(request.getCode());
        applyCommonFields(entity, request);
        return entity;
    }

    public void update(CurrencyEntity entity, CurrencyUpdateRequest request) {
        applyCommonFields(entity, request);
    }

    private void applyCommonFields(CurrencyEntity entity, CurrencyRequest request) {
        entity.setName(request.getName());
        entity.setDescription(request.getDescription());
        entity.setSymbol(request.getSymbol());
    }

    public CurrencyDto toDto(CurrencyEntity entity) {
        return CurrencyDto.builder()
                .id(entity.getId())
                .code(entity.getCode())
                .name(entity.getName())
                .description(entity.getDescription())
                .symbol(entity.getSymbol())
                .build();
    }
}
