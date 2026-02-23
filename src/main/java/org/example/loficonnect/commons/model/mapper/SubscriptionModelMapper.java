package org.example.loficonnect.commons.model.mapper;

import lombok.experimental.UtilityClass;
import org.example.loficonnect.commons.dto.request.SubscriptionModelCreateRequest;
import org.example.loficonnect.commons.model.entity.CurrencyEntity;
import org.example.loficonnect.commons.model.entity.SubscriptionModelEntity;

@UtilityClass
public class SubscriptionModelMapper {
    public static SubscriptionModelEntity fromRequest(
            SubscriptionModelCreateRequest request,
            CurrencyEntity currencyEntity
    ) {
        SubscriptionModelEntity entity = new SubscriptionModelEntity();
        entity.setName(request.getName());
        entity.setDescription(request.getDescription());
        entity.setDetails(request.getDetails());
        entity.setMaxAppKeys(request.getMaxAppKeys());
        entity.setApiCallsQuota(request.getApiCallsQuota());
        entity.setQuotaPeriodDays(request.getQuotaPeriodDays());
        entity.setPrice(request.getPrice());
        entity.setCurrencyEntity(currencyEntity);
        return entity;
    }
}
