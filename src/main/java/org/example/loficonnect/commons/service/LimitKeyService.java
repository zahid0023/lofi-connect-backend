package org.example.loficonnect.commons.service;

import org.example.loficonnect.commons.dto.request.LimitKeyCreateRequest;
import org.example.loficonnect.commons.dto.request.LimitKeyUpdateRequest;
import org.example.loficonnect.commons.dto.request.PaginatedRequest;
import org.example.loficonnect.commons.dto.response.LimitKeyResponse;
import org.example.loficonnect.commons.dto.response.PaginatedResponse;
import org.example.loficonnect.commons.dto.response.SuccessResponse;
import org.example.loficonnect.commons.model.entity.LimitKeyEntity;
import org.example.loficonnect.commons.model.projection.LimitKeySummary;

public interface LimitKeyService {
    SuccessResponse create(LimitKeyCreateRequest request);

    LimitKeyEntity getEntityById(Long id);

    LimitKeyResponse getById(Long id);

    PaginatedResponse<LimitKeySummary> getAll(PaginatedRequest request);

    SuccessResponse update(LimitKeyEntity entity, LimitKeyUpdateRequest request);

    SuccessResponse delete(LimitKeyEntity entity);
}
