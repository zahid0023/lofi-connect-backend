package org.example.loficonnect.commons.service;

import org.example.loficonnect.commons.dto.request.SubscriptionPlanCreateRequest;
import org.example.loficonnect.commons.dto.request.SubscriptionPlanUpdateRequest;
import org.example.loficonnect.commons.dto.response.SubscriptionPlanListResponse;
import org.example.loficonnect.commons.dto.response.SubscriptionPlanResponse;
import org.example.loficonnect.commons.dto.response.SuccessResponse;
import org.example.loficonnect.commons.model.entity.SubscriptionPlanEntity;

public interface SubscriptionPlanService {
    SuccessResponse createSubscriptionPlan(SubscriptionPlanCreateRequest request);

    SubscriptionPlanEntity getSubscriptionPlanEntityById(Long id);

    SubscriptionPlanResponse getSubscriptionPlanById(Long id);

    SubscriptionPlanListResponse getAllSubscriptionPlans();

    SuccessResponse updateSubscriptionPlan(Long id, SubscriptionPlanUpdateRequest request);

    SuccessResponse deleteSubscriptionPlan(Long id);
}
