package org.example.loficonnect.commons.model.mapper;

import lombok.experimental.UtilityClass;
import org.example.loficonnect.commons.dto.request.CurrencyCreateRequest;
import org.example.loficonnect.commons.model.entity.CurrencyEntity;

@UtilityClass
public class CurrencyMapper {
    public static CurrencyEntity fromRequest(CurrencyCreateRequest request) {
        CurrencyEntity entity = new CurrencyEntity();
        entity.setCode(request.getCode());
        entity.setSymbol(request.getSymbol());
        return entity;
    }
}
