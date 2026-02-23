package org.example.loficonnect.commons.service;

import org.example.loficonnect.commons.dto.request.SubscriptionModelCreateRequest;
import org.example.loficonnect.commons.dto.response.SuccessResponse;
import org.example.loficonnect.commons.model.entity.CurrencyEntity;

public interface SubscriptionModelService {
    SuccessResponse createSubscriptionModel(SubscriptionModelCreateRequest request, CurrencyEntity currencyEntity);
}
