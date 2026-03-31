package org.example.loficonnect.commons.serviceImpl;

import lombok.extern.slf4j.Slf4j;
import org.example.loficonnect.auth.model.enitty.UserEntity;
import org.example.loficonnect.commons.dto.request.TenantSubscriptionCreateRequest;
import org.example.loficonnect.commons.dto.response.SuccessResponse;
import org.example.loficonnect.commons.model.entity.SubscriptionPlanEntity;
import org.example.loficonnect.commons.model.entity.TenantSubscriptionEntity;
import org.example.loficonnect.commons.model.mapper.TenantSubscriptionMapper;
import org.example.loficonnect.commons.repository.TenantSubscriptionRepository;
import org.example.loficonnect.commons.service.TenantSubscriptionService;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class TenantSubscriptionServiceImpl implements TenantSubscriptionService {

    private final TenantSubscriptionRepository tenantSubscriptionRepository;

    public TenantSubscriptionServiceImpl(TenantSubscriptionRepository tenantSubscriptionRepository) {
        this.tenantSubscriptionRepository = tenantSubscriptionRepository;
    }

    @Override
    public SuccessResponse subscribePlan(TenantSubscriptionCreateRequest request, UserEntity userEntity, SubscriptionPlanEntity subscriptionPlanEntity) {
        TenantSubscriptionEntity tenantSubscriptionEntity = TenantSubscriptionMapper.fromRequest(request, userEntity, subscriptionPlanEntity);
        tenantSubscriptionEntity = tenantSubscriptionRepository.save(tenantSubscriptionEntity);
        return new SuccessResponse(true, tenantSubscriptionEntity.getId());
    }
}
