package org.example.loficonnect.commons.serviceImpl;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.example.loficonnect.commons.dto.request.LimitKeyCreateRequest;
import org.example.loficonnect.commons.dto.request.LimitKeyUpdateRequest;
import org.example.loficonnect.commons.dto.response.LimitKeyListResponse;
import org.example.loficonnect.commons.dto.response.LimitKeyResponse;
import org.example.loficonnect.commons.dto.response.SuccessResponse;
import org.example.loficonnect.commons.model.dto.LimitKeyDto;
import org.example.loficonnect.commons.model.entity.LimitKeyEntity;
import org.example.loficonnect.commons.model.mapper.LimitKeyMapper;
import org.example.loficonnect.commons.repository.LimitKeyRepository;
import org.example.loficonnect.commons.service.LimitKeyService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
public class LimitKeyServiceImpl implements LimitKeyService {
    private final LimitKeyRepository limitKeyRepository;

    public LimitKeyServiceImpl(LimitKeyRepository limitKeyRepository) {
        this.limitKeyRepository = limitKeyRepository;
    }

    @Override
    public SuccessResponse createLimitKey(LimitKeyCreateRequest request) {
        LimitKeyEntity entity = LimitKeyMapper.fromRequest(request);
        limitKeyRepository.save(entity);
        return new SuccessResponse(true, entity.getId());
    }

    @Override
    public LimitKeyEntity getLimitKeyEntityById(Long id) {
        return limitKeyRepository.findByIdAndIsActiveAndIsDeleted(id, true, false)
                .orElseThrow(() -> new EntityNotFoundException("LimitKey Entity not found"));
    }

    @Override
    public LimitKeyResponse getLimitKeyById(Long id) {
        LimitKeyDto dto = LimitKeyMapper.toDto(getLimitKeyEntityById(id));
        return new LimitKeyResponse(dto);
    }

    @Override
    public LimitKeyListResponse getAllLimitKeys() {
        List<LimitKeyDto> dtos = limitKeyRepository.findAllByIsActiveAndIsDeleted(true, false)
                .stream()
                .map(LimitKeyMapper::toDto)
                .toList();
        return new LimitKeyListResponse(dtos);
    }

    @Override
    @Transactional
    public SuccessResponse updateLimitKey(Long id, LimitKeyUpdateRequest request) {
        LimitKeyEntity entity = getLimitKeyEntityById(id);
        LimitKeyMapper.update(request, entity);
        limitKeyRepository.save(entity);
        return new SuccessResponse(true, entity.getId());
    }

    @Override
    @Transactional
    public SuccessResponse deleteLimitKey(Long id) {
        LimitKeyEntity entity = getLimitKeyEntityById(id);
        entity.setIsActive(false);
        entity.setIsDeleted(true);
        limitKeyRepository.save(entity);
        return new SuccessResponse(true, entity.getId());
    }
}
