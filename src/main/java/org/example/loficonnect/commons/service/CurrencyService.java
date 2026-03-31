package org.example.loficonnect.commons.service;

import org.example.loficonnect.commons.dto.request.CurrencyCreateRequest;
import org.example.loficonnect.commons.dto.request.CurrencyUpdateRequest;
import org.example.loficonnect.commons.dto.response.CurrencyListResponse;
import org.example.loficonnect.commons.dto.response.CurrencyResponse;
import org.example.loficonnect.commons.dto.response.SuccessResponse;
import org.example.loficonnect.commons.model.entity.CurrencyEntity;

import java.util.List;

public interface CurrencyService {
    SuccessResponse createCurrency(CurrencyCreateRequest request);

    CurrencyEntity getCurrencyEntityById(Long id);

    CurrencyResponse getCurrencyById(Long id);

    CurrencyListResponse getAllCurrencies();

    SuccessResponse updateCurrency(Long id, CurrencyUpdateRequest request);

    SuccessResponse deleteCurrency(Long id);
}
