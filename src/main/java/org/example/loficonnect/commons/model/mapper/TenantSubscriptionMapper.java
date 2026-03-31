package org.example.loficonnect.commons.model.mapper;

import lombok.experimental.UtilityClass;
import org.example.loficonnect.auth.model.enitty.UserEntity;
import org.example.loficonnect.commons.dto.request.TenantSubscriptionCreateRequest;
import org.example.loficonnect.commons.model.entity.SubscriptionPlanEntity;
import org.example.loficonnect.commons.model.entity.TenantSubscriptionEntity;

import java.time.OffsetDateTime;

@UtilityClass
public class TenantSubscriptionMapper {

    public static TenantSubscriptionEntity fromRequest(TenantSubscriptionCreateRequest request,
                                                       UserEntity userEntity,
                                                       SubscriptionPlanEntity subscriptionPlanEntity) {
        TenantSubscriptionEntity entity = new TenantSubscriptionEntity();
        entity.setTenantEntity(userEntity);
        entity.setSubscriptionPlanEntity(subscriptionPlanEntity);
        entity.setStatus("ACTIVE");
        entity.setStartAt(OffsetDateTime.now());

        // Calculate endAt based on subscription plan duration (in days)
        if (subscriptionPlanEntity.getDurationInDays() != null) {
            entity.setEndAt(OffsetDateTime.now().plusDays(subscriptionPlanEntity.getDurationInDays()));
        } else {
            // Fallback to 30 days if not defined
            entity.setEndAt(OffsetDateTime.now().plusDays(30));
        }

        entity.setAutoRenew(request.getAutoRenew() != null ? request.getAutoRenew() : true);
        return entity;
    }
}
