package org.example.loficonnect.commons.service;

import org.example.loficonnect.commons.dto.request.CurrencyCreateRequest;
import org.example.loficonnect.commons.dto.request.CurrencyUpdateRequest;
import org.example.loficonnect.commons.dto.request.PaginatedRequest;
import org.example.loficonnect.commons.dto.response.CurrencyResponse;
import org.example.loficonnect.commons.dto.response.PaginatedResponse;
import org.example.loficonnect.commons.dto.response.SuccessResponse;
import org.example.loficonnect.commons.model.entity.CurrencyEntity;
import org.example.loficonnect.commons.model.projection.CurrencySummary;

public interface CurrencyService {
    SuccessResponse create(CurrencyCreateRequest request);

    CurrencyEntity getEntityById(Long id);

    CurrencyResponse getById(Long id);

    PaginatedResponse<CurrencySummary> getAll(PaginatedRequest request);

    SuccessResponse update(CurrencyEntity entity, CurrencyUpdateRequest request);

    SuccessResponse delete(CurrencyEntity entity);
}
