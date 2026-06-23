package org.example.loficonnect.subscription.service;

import org.example.loficonnect.commons.dto.request.PaginatedRequest;
import org.example.loficonnect.commons.dto.response.PaginatedResponse;
import org.example.loficonnect.commons.dto.response.SuccessResponse;
import org.example.loficonnect.subscription.dto.request.plan.SubscriptionPlanCreateRequest;
import org.example.loficonnect.subscription.dto.request.plan.SubscriptionPlanUpdateRequest;
import org.example.loficonnect.subscription.dto.response.SubscriptionPlanResponse;
import org.example.loficonnect.subscription.model.entity.SubscriptionPlanEntity;
import org.example.loficonnect.subscription.model.projection.SubscriptionPlanSummary;

public interface SubscriptionPlanService {

    SuccessResponse create(SubscriptionPlanCreateRequest request);

    SubscriptionPlanEntity getEntityById(Long id);

    SubscriptionPlanResponse getById(Long id);

    PaginatedResponse<SubscriptionPlanSummary> getAll(PaginatedRequest request);

    PaginatedResponse<SubscriptionPlanSummary> getPublicPlans(PaginatedRequest request);

    SuccessResponse update(SubscriptionPlanEntity entity, SubscriptionPlanUpdateRequest request);

    SuccessResponse delete(SubscriptionPlanEntity entity);
}
