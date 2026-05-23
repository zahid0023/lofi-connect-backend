package org.example.loficonnect.commons.service;

import org.example.loficonnect.commons.dto.request.PaginatedRequest;
import org.example.loficonnect.commons.dto.request.SubscriptionPlanCreateRequest;
import org.example.loficonnect.commons.dto.request.SubscriptionPlanUpdateRequest;
import org.example.loficonnect.commons.dto.response.PaginatedResponse;
import org.example.loficonnect.commons.dto.response.SubscriptionPlanResponse;
import org.example.loficonnect.commons.dto.response.SuccessResponse;
import org.example.loficonnect.commons.model.entity.SubscriptionPlanEntity;
import org.example.loficonnect.commons.model.projection.SubscriptionPlanSummary;

public interface SubscriptionPlanService {
    SuccessResponse create(SubscriptionPlanCreateRequest request);

    SubscriptionPlanEntity getEntityById(Long id);

    SubscriptionPlanResponse getById(Long id);

    PaginatedResponse<SubscriptionPlanSummary> getAll(PaginatedRequest request);

    SuccessResponse update(SubscriptionPlanEntity entity, SubscriptionPlanUpdateRequest request);

    SuccessResponse delete(SubscriptionPlanEntity entity);
}
