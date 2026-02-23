package org.example.loficonnect.commons.serviceImpl;

import lombok.extern.slf4j.Slf4j;
import org.example.loficonnect.commons.dto.request.SubscriptionModelCreateRequest;
import org.example.loficonnect.commons.dto.response.SuccessResponse;
import org.example.loficonnect.commons.model.entity.CurrencyEntity;
import org.example.loficonnect.commons.model.entity.SubscriptionModelEntity;
import org.example.loficonnect.commons.model.mapper.SubscriptionModelMapper;
import org.example.loficonnect.commons.repository.SubscriptionModelRepository;
import org.example.loficonnect.commons.service.SubscriptionModelService;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class SubscriptionModelServiceImpl implements SubscriptionModelService {
    private final SubscriptionModelRepository subscriptionModelRepository;

    public SubscriptionModelServiceImpl(SubscriptionModelRepository subscriptionModelRepository) {
        this.subscriptionModelRepository = subscriptionModelRepository;
    }

    @Override
    public SuccessResponse createSubscriptionModel(SubscriptionModelCreateRequest request,
                                                   CurrencyEntity currencyEntity) {
        SubscriptionModelEntity subscriptionModelEntity = SubscriptionModelMapper.fromRequest(request, currencyEntity);
        subscriptionModelRepository.save(subscriptionModelEntity);
        return new SuccessResponse(true, subscriptionModelEntity.getId());
    }
}
