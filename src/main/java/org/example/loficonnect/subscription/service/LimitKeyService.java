package org.example.loficonnect.subscription.service;

import org.example.loficonnect.commons.dto.request.PaginatedRequest;
import org.example.loficonnect.commons.dto.response.PaginatedResponse;
import org.example.loficonnect.commons.dto.response.SuccessResponse;
import org.example.loficonnect.subscription.dto.request.limitkey.CreateLimitKeyRequest;
import org.example.loficonnect.subscription.dto.request.limitkey.UpdateLimitKeyRequest;
import org.example.loficonnect.subscription.dto.response.LimitKeyResponse;
import org.example.loficonnect.subscription.model.entity.LimitKeyEntity;
import org.example.loficonnect.subscription.model.projection.LimitKeySummary;

public interface LimitKeyService {

    SuccessResponse create(CreateLimitKeyRequest request);

    LimitKeyEntity getEntityById(Long id);

    LimitKeyResponse getById(Long id);

    PaginatedResponse<LimitKeySummary> getAll(PaginatedRequest request);

    SuccessResponse update(LimitKeyEntity entity, UpdateLimitKeyRequest request);

    SuccessResponse delete(LimitKeyEntity entity);
}
