package org.example.loficonnect.subscription.model.mapper;

import lombok.experimental.UtilityClass;
import org.example.loficonnect.subscription.model.dto.TenantSubscriptionDto;
import org.example.loficonnect.subscription.model.entity.TenantSubscriptionEntity;

@UtilityClass
public class TenantSubscriptionMapper {

    public TenantSubscriptionDto toDto(TenantSubscriptionEntity entity) {
        return TenantSubscriptionDto.builder()
                .id(entity.getId())
                .userId(entity.getUserId())
                .planId(entity.getSubscriptionPlan().getId())
                .planCode(entity.getSubscriptionPlan().getCode())
                .planName(entity.getSubscriptionPlan().getName())
                .billingCycle(entity.getSubscriptionPlan().getBillingCycle())
                .price(entity.getSubscriptionPlan().getPrice())
                .status(entity.getStatus())
                .startDate(entity.getStartDate())
                .endDate(entity.getEndDate())
                .trialEndsAt(entity.getTrialEndsAt())
                .build();
    }
}
