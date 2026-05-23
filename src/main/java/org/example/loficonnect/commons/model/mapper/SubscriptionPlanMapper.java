package org.example.loficonnect.commons.model.mapper;

import lombok.experimental.UtilityClass;
import org.example.loficonnect.commons.dto.request.SubscriptionPlanCreateRequest;
import org.example.loficonnect.commons.dto.request.SubscriptionPlanRequest;
import org.example.loficonnect.commons.dto.request.SubscriptionPlanUpdateRequest;
import org.example.loficonnect.commons.model.dto.SubscriptionPlanDto;
import org.example.loficonnect.commons.model.dto.SubscriptionPlanLimitDto;
import org.example.loficonnect.commons.model.entity.CurrencyEntity;
import org.example.loficonnect.commons.model.entity.LimitKeyEntity;
import org.example.loficonnect.commons.model.entity.SubscriptionPlanEntity;
import org.example.loficonnect.commons.model.entity.SubscriptionPlanLimitEntity;

import java.util.List;

@UtilityClass
public class SubscriptionPlanMapper {

    public SubscriptionPlanEntity create(SubscriptionPlanCreateRequest request, CurrencyEntity currency) {
        SubscriptionPlanEntity entity = new SubscriptionPlanEntity();
        entity.setCode(request.getCode());
        applyCommonFields(entity, request, currency);
        return entity;
    }

    public void update(SubscriptionPlanEntity entity, SubscriptionPlanUpdateRequest request, CurrencyEntity currency) {
        applyCommonFields(entity, request, currency);
    }

    private void applyCommonFields(SubscriptionPlanEntity entity, SubscriptionPlanRequest request, CurrencyEntity currency) {
        entity.setCurrencyEntity(currency);
        entity.setName(request.getName());
        entity.setPrice(request.getPrice());
        entity.setDescription(request.getDescription());
        entity.setSortOrder(request.getSortOrder());
        entity.setBillingCycle(request.getBillingCycle());
        entity.setDurationInDays(request.getDurationInDays());
    }

    public SubscriptionPlanLimitEntity toLimitEntity(SubscriptionPlanEntity plan, LimitKeyEntity limitKey, Long limitValue) {
        SubscriptionPlanLimitEntity entity = new SubscriptionPlanLimitEntity();
        entity.setSubscriptionPlanEntity(plan);
        entity.setLimitKeyEntity(limitKey);
        entity.setLimitValue(limitValue);
        return entity;
    }

    public SubscriptionPlanLimitDto toLimitDto(SubscriptionPlanLimitEntity entity) {
        SubscriptionPlanLimitDto dto = new SubscriptionPlanLimitDto();
        dto.setId(entity.getId());
        dto.setLimitKey(LimitKeyMapper.toDto(entity.getLimitKeyEntity()));
        dto.setLimitValue(entity.getLimitValue());
        return dto;
    }

    public SubscriptionPlanDto toDto(SubscriptionPlanEntity entity, List<SubscriptionPlanLimitEntity> limits) {
        SubscriptionPlanDto dto = new SubscriptionPlanDto();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setPrice(entity.getPrice());
        dto.setDescription(entity.getDescription());
        dto.setBillingCycle(entity.getBillingCycle());
        dto.setDurationInDays(entity.getDurationInDays());
        dto.setCurrency(CurrencyMapper.toDto(entity.getCurrencyEntity()));
        dto.setLimits(limits.stream().map(SubscriptionPlanMapper::toLimitDto).toList());
        return dto;
    }
}
