package org.example.loficonnect.auth.serviceImpl;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.example.loficonnect.auth.dto.request.appkey.CreateAppKeyRequest;
import org.example.loficonnect.auth.dto.response.appkey.AppKeyListResponse;
import org.example.loficonnect.auth.dto.response.appkey.GenerateAppKeyResponse;
import org.example.loficonnect.auth.service.AppKeyService;
import org.example.loficonnect.auth.model.dto.LofiConnectAppKeyDTO;
import org.example.loficonnect.auth.model.enitty.LofiConnectAppKeyEntity;
import org.example.loficonnect.model.mapper.LofiConnectAppKeyMapper;
import org.example.loficonnect.repository.LofiConnectAppKeyRepository;
import org.example.loficonnect.subscription.exception.NoActiveSubscriptionException;
import org.example.loficonnect.subscription.model.entity.TenantSubscriptionEntity;
import org.example.loficonnect.subscription.model.enums.LimitKeys;
import org.example.loficonnect.subscription.model.enums.TenantSubscriptionStatus;
import org.example.loficonnect.subscription.repository.TenantSubscriptionRepository;
import org.example.loficonnect.subscription.service.UsageEnforcementService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class AppKeyServiceImpl implements AppKeyService {
    private final LofiConnectAppKeyRepository lofiConnectAppKeyRepository;
    private final TenantSubscriptionRepository tenantSubscriptionRepository;
    private final UsageEnforcementService usageEnforcementService;

    private static final List<TenantSubscriptionStatus> ACTIVE_STATUSES =
            List.of(TenantSubscriptionStatus.ACTIVE, TenantSubscriptionStatus.TRIAL);

    public AppKeyServiceImpl(LofiConnectAppKeyRepository lofiConnectAppKeyRepository,
                             TenantSubscriptionRepository tenantSubscriptionRepository,
                             UsageEnforcementService usageEnforcementService) {
        this.lofiConnectAppKeyRepository = lofiConnectAppKeyRepository;
        this.tenantSubscriptionRepository = tenantSubscriptionRepository;
        this.usageEnforcementService = usageEnforcementService;
    }

    @Override
    public GenerateAppKeyResponse generateAppKey(Long userId, CreateAppKeyRequest request) {
        TenantSubscriptionEntity subscription = tenantSubscriptionRepository
                .findByUserIdAndStatusIn(userId, ACTIVE_STATUSES)
                .orElseThrow(() -> new NoActiveSubscriptionException(
                        "An active subscription is required to generate an App Key."));

        long currentCount = lofiConnectAppKeyRepository.countByCreatedByAndIsActiveAndIsDeleted(userId, true, false);
        usageEnforcementService.enforce(userId, LimitKeys.APP_KEYS, currentCount);

        LofiConnectAppKeyEntity lofiConnectAppKeyEntity = LofiConnectAppKeyMapper.fromRequest(request, subscription);
        lofiConnectAppKeyEntity = lofiConnectAppKeyRepository.save(lofiConnectAppKeyEntity);
        LofiConnectAppKeyDTO dto = LofiConnectAppKeyMapper.toDto(lofiConnectAppKeyEntity);
        return new GenerateAppKeyResponse(dto);
    }

    @Override
    public AppKeyListResponse getAllAppKeys(Long userId) {
        List<LofiConnectAppKeyEntity> entities = lofiConnectAppKeyRepository.findByCreatedByAndIsActiveAndIsDeleted(userId, true, false);
        List<LofiConnectAppKeyDTO> dtos = entities.stream().map(LofiConnectAppKeyMapper::toDto).toList();
        return new AppKeyListResponse(dtos);
    }

    @Override
    public LofiConnectAppKeyEntity getAppKeyEntity(String appKey) {
        return lofiConnectAppKeyRepository.findByAppKeyAndIsActiveAndIsDeleted(appKey, true, false)
                .orElseThrow(() -> new RuntimeException("App key not found"));
    }

    @Override
    public LofiConnectAppKeyEntity getAppKeyEntityById(Long id) {
        return lofiConnectAppKeyRepository.findByIdAndIsActiveAndIsDeleted(id, true, false)
                .orElseThrow(() -> new RuntimeException("App key not found"));
    }
}
