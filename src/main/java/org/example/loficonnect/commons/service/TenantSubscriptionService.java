package org.example.loficonnect.commons.service;

import org.example.loficonnect.auth.model.enitty.UserEntity;
import org.example.loficonnect.commons.dto.request.TenantSubscriptionCreateRequest;
import org.example.loficonnect.commons.dto.response.SuccessResponse;
import org.example.loficonnect.commons.model.entity.SubscriptionPlanEntity;

public interface TenantSubscriptionService {
    SuccessResponse subscribePlan(TenantSubscriptionCreateRequest request,
                                  UserEntity userEntity,
                                  SubscriptionPlanEntity subscriptionPlanEntity);

    SuccessResponse upgradePlan(TenantSubscriptionCreateRequest request,
                                UserEntity userEntity,
                                SubscriptionPlanEntity newPlanEntity);
}
