package org.example.loficonnect.subscription.model.mapper;

import lombok.experimental.UtilityClass;
import org.example.loficonnect.subscription.dto.request.plan.SubscriptionPlanCreateRequest;
import org.example.loficonnect.subscription.dto.request.plan.SubscriptionPlanRequest;
import org.example.loficonnect.subscription.model.dto.SubscriptionPlanDto;
import org.example.loficonnect.subscription.model.dto.SubscriptionPlanLimitDto;
import org.example.loficonnect.subscription.model.entity.SubscriptionPlanEntity;
import org.example.loficonnect.subscription.model.entity.SubscriptionPlanLimitEntity;

import java.util.List;

@UtilityClass
public class SubscriptionPlanMapper {

    public SubscriptionPlanEntity create(SubscriptionPlanCreateRequest request) {
        SubscriptionPlanEntity entity = new SubscriptionPlanEntity();
        entity.setCode(request.getCode());
        applyCommonFields(entity, request);
        return entity;
    }

    public void update(SubscriptionPlanEntity entity, SubscriptionPlanRequest request) {
        applyCommonFields(entity, request);
    }

    private void applyCommonFields(SubscriptionPlanEntity entity, SubscriptionPlanRequest request) {
        entity.setCurrencyId(request.getCurrencyId());
        entity.setBillingCycle(request.getBillingCycle());
        entity.setTrialPeriodDays(request.getTrialPeriodDays() != null ? request.getTrialPeriodDays() : 0);
        entity.setName(request.getName());
        entity.setPrice(request.getPrice() != null ? request.getPrice() : java.math.BigDecimal.ZERO);
        entity.setDescription(request.getDescription());
        entity.setSortOrder(request.getSortOrder() != null ? request.getSortOrder() : 0);
        entity.setIsPublic(request.getIsPublic() != null ? request.getIsPublic() : Boolean.TRUE);
    }

    public SubscriptionPlanDto toDto(SubscriptionPlanEntity entity) {
        List<SubscriptionPlanLimitDto> limitDtos = entity.getLimits().stream()
                .map(SubscriptionPlanMapper::toLimitDto)
                .toList();

        return SubscriptionPlanDto.builder()
                .id(entity.getId())
                .currencyId(entity.getCurrencyId())
                .code(entity.getCode())
                .billingCycle(entity.getBillingCycle())
                .trialPeriodDays(entity.getTrialPeriodDays())
                .sortOrder(entity.getSortOrder())
                .name(entity.getName())
                .price(entity.getPrice())
                .description(entity.getDescription())
                .isPublic(entity.getIsPublic())
                .limits(limitDtos)
                .build();
    }

    public SubscriptionPlanLimitDto toLimitDto(SubscriptionPlanLimitEntity limit) {
        return SubscriptionPlanLimitDto.builder()
                .id(limit.getId())
                .limitKeyId(limit.getLimitKey().getId())
                .limitKeyCode(limit.getLimitKey().getCode())
                .limitKeyName(limit.getLimitKey().getName())
                .limitKeyUnit(limit.getLimitKey().getUnit())
                .limitValue(limit.getLimitValue())
                .build();
    }
}
