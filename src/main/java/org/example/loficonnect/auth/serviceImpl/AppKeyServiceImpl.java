package org.example.loficonnect.auth.serviceImpl;

import lombok.extern.slf4j.Slf4j;
import org.example.loficonnect.auth.dto.request.appkey.CreateAppKeyRequest;
import org.example.loficonnect.auth.dto.response.appkey.AppKeyListResponse;
import org.example.loficonnect.auth.dto.response.appkey.GenerateAppKeyResponse;
import org.example.loficonnect.auth.service.AppKeyService;
import org.example.loficonnect.auth.model.dto.LofiConnectAppKeyDTO;
import org.example.loficonnect.auth.model.enitty.LofiConnectAppKeyEntity;
import org.example.loficonnect.model.mapper.LofiConnectAppKeyMapper;
import org.example.loficonnect.repository.LofiConnectAppKeyRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class AppKeyServiceImpl implements AppKeyService {
    private final LofiConnectAppKeyRepository lofiConnectAppKeyRepository;

    public AppKeyServiceImpl(LofiConnectAppKeyRepository lofiConnectAppKeyRepository) {
        this.lofiConnectAppKeyRepository = lofiConnectAppKeyRepository;
    }

    @Override
    public GenerateAppKeyResponse generateAppKey(CreateAppKeyRequest request) {
        LofiConnectAppKeyEntity lofiConnectAppKeyEntity = LofiConnectAppKeyMapper.toEntity(request);
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
