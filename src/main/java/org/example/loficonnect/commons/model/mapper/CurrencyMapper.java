package org.example.loficonnect.commons.model.mapper;

import lombok.experimental.UtilityClass;
import org.example.loficonnect.commons.dto.request.CurrencyCreateRequest;
import org.example.loficonnect.commons.dto.request.CurrencyUpdateRequest;
import org.example.loficonnect.commons.model.dto.CurrencyDto;
import org.example.loficonnect.commons.model.entity.CurrencyEntity;

@UtilityClass
public class CurrencyMapper {
    public static CurrencyEntity fromRequest(CurrencyCreateRequest request) {
        CurrencyEntity entity = new CurrencyEntity();
        entity.setCode(request.getCode());
        entity.setSymbol(request.getSymbol());
        return entity;
    }

    public static void update(CurrencyUpdateRequest request, CurrencyEntity entity) {
        entity.setCode(request.getCode());
        entity.setSymbol(request.getSymbol());
    }

    public static CurrencyDto toDto(CurrencyEntity entity) {
        CurrencyDto dto = new CurrencyDto();
        dto.setCode(entity.getCode());
        dto.setSymbol(entity.getSymbol());
        return dto;
    }
}
