package org.example.loficonnect.commons.serviceImpl;

import lombok.extern.slf4j.Slf4j;
import org.example.loficonnect.auth.model.enitty.UserEntity;
import org.example.loficonnect.commons.dto.request.TenantSubscriptionCreateRequest;
import org.example.loficonnect.commons.dto.response.SuccessResponse;
import org.example.loficonnect.commons.exception.ActiveSubscriptionExistsException;
import org.example.loficonnect.commons.exception.NoActiveSubscriptionException;
import org.example.loficonnect.commons.model.entity.SubscriptionPlanEntity;
import org.example.loficonnect.commons.model.entity.TenantSubscriptionEntity;
import org.example.loficonnect.commons.model.mapper.TenantSubscriptionMapper;
import org.example.loficonnect.commons.repository.TenantSubscriptionRepository;
import org.example.loficonnect.commons.service.TenantSubscriptionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;

@Service
@Slf4j
public class TenantSubscriptionServiceImpl implements TenantSubscriptionService {

    private final TenantSubscriptionRepository tenantSubscriptionRepository;

    public TenantSubscriptionServiceImpl(TenantSubscriptionRepository tenantSubscriptionRepository) {
        this.tenantSubscriptionRepository = tenantSubscriptionRepository;
    }

    @Override
    @Transactional
    public SuccessResponse subscribePlan(TenantSubscriptionCreateRequest request, UserEntity userEntity, SubscriptionPlanEntity subscriptionPlanEntity) {
        boolean hasActiveSubscription = tenantSubscriptionRepository
                .existsByTenantEntityAndStatusAndEndAtAfterAndIsActiveAndIsDeleted(
                        userEntity, "ACTIVE", OffsetDateTime.now(), true, false);

        if (hasActiveSubscription) {
            throw new ActiveSubscriptionExistsException(
                    "User already has an active subscription. Please cancel the existing subscription before subscribing to a new plan.");
        }

        TenantSubscriptionEntity tenantSubscriptionEntity = TenantSubscriptionMapper.fromRequest(request, userEntity, subscriptionPlanEntity);
        tenantSubscriptionEntity = tenantSubscriptionRepository.save(tenantSubscriptionEntity);
        return new SuccessResponse(true, tenantSubscriptionEntity.getId());
    }

    @Override
    @Transactional
    public SuccessResponse upgradePlan(TenantSubscriptionCreateRequest request, UserEntity userEntity, SubscriptionPlanEntity newPlanEntity) {
        TenantSubscriptionEntity current = tenantSubscriptionRepository
                .findFirstByTenantEntityAndStatusAndEndAtAfterAndIsActiveAndIsDeletedOrderByStartAtDesc(
                        userEntity, "ACTIVE", OffsetDateTime.now(), true, false)
                .orElseThrow(() -> new NoActiveSubscriptionException(
                        "No active subscription found. Please subscribe to a plan before attempting an upgrade."));

        // Cancel the current subscription immediately
        current.setStatus("CANCELLED");
        current.setEndAt(OffsetDateTime.now());
        tenantSubscriptionRepository.save(current);

        // Create the new subscription
        TenantSubscriptionEntity newSubscription = TenantSubscriptionMapper.fromRequest(request, userEntity, newPlanEntity);
        newSubscription = tenantSubscriptionRepository.save(newSubscription);

        log.info("User {} upgraded from plan {} to plan {}",
                userEntity.getId(), current.getSubscriptionPlanEntity().getId(), newPlanEntity.getId());

        return new SuccessResponse(true, newSubscription.getId());
    }
}
