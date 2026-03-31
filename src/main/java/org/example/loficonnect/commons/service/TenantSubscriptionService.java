package org.example.loficonnect.commons.service;

import org.example.loficonnect.auth.model.enitty.UserEntity;
import org.example.loficonnect.commons.dto.request.TenantSubscriptionCreateRequest;
import org.example.loficonnect.commons.dto.request.TenantSubscriptionUpdateRequest;
import org.example.loficonnect.commons.dto.response.SuccessResponse;
import org.example.loficonnect.commons.dto.response.TenantSubscriptionListResponse;
import org.example.loficonnect.commons.dto.response.TenantSubscriptionResponse;
import org.example.loficonnect.commons.model.entity.SubscriptionPlanEntity;
import org.example.loficonnect.commons.model.entity.TenantSubscriptionEntity;

public interface TenantSubscriptionService {
    SuccessResponse subscribePlan(TenantSubscriptionCreateRequest request,
                                  UserEntity userEntity,
                                  SubscriptionPlanEntity subscriptionPlanEntity);
}
