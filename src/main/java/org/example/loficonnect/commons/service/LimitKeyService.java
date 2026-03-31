package org.example.loficonnect.commons.service;

import org.example.loficonnect.commons.dto.request.LimitKeyCreateRequest;
import org.example.loficonnect.commons.dto.request.LimitKeyUpdateRequest;
import org.example.loficonnect.commons.dto.response.LimitKeyListResponse;
import org.example.loficonnect.commons.dto.response.LimitKeyResponse;
import org.example.loficonnect.commons.dto.response.SuccessResponse;
import org.example.loficonnect.commons.model.entity.LimitKeyEntity;

public interface LimitKeyService {
    SuccessResponse createLimitKey(LimitKeyCreateRequest request);

    LimitKeyEntity getLimitKeyEntityById(Long id);

    LimitKeyResponse getLimitKeyById(Long id);

    LimitKeyListResponse getAllLimitKeys();

    SuccessResponse updateLimitKey(Long id, LimitKeyUpdateRequest request);

    SuccessResponse deleteLimitKey(Long id);
}
