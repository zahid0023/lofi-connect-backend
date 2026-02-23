package org.example.loficonnect.commons.service;

import org.example.loficonnect.commons.dto.request.CurrencyCreateRequest;
import org.example.loficonnect.commons.dto.response.SuccessResponse;
import org.example.loficonnect.commons.model.entity.CurrencyEntity;

public interface CurrencyService {
    SuccessResponse createCurrency(CurrencyCreateRequest request);

    CurrencyEntity getCurrencyEntityById(Long id);
}
