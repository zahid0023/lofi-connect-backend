package org.example.loficonnect.commons.serviceImpl;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.example.loficonnect.commons.dto.request.LimitKeyCreateRequest;
import org.example.loficonnect.commons.dto.request.LimitKeyUpdateRequest;
import org.example.loficonnect.commons.dto.request.PaginatedRequest;
import org.example.loficonnect.commons.dto.response.LimitKeyResponse;
import org.example.loficonnect.commons.dto.response.PaginatedResponse;
import org.example.loficonnect.commons.dto.response.SuccessResponse;
import org.example.loficonnect.commons.model.dto.LimitKeyDto;
import org.example.loficonnect.commons.model.entity.LimitKeyEntity;
import org.example.loficonnect.commons.model.enums.LimitKeySortField;
import org.example.loficonnect.commons.model.mapper.LimitKeyMapper;
import org.example.loficonnect.commons.model.projection.LimitKeySummary;
import org.example.loficonnect.commons.repository.LimitKeyRepository;
import org.example.loficonnect.commons.service.LimitKeyService;
import org.example.loficonnect.util.Pagination;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
@Slf4j
public class LimitKeyServiceImpl implements LimitKeyService {

    private static final Set<String> ALLOWED_SORT_FIELDS = LimitKeySortField.allowedFields();

    private final LimitKeyRepository limitKeyRepository;

    public LimitKeyServiceImpl(LimitKeyRepository limitKeyRepository) {
        this.limitKeyRepository = limitKeyRepository;
    }

    @Transactional
    @Override
    public SuccessResponse create(LimitKeyCreateRequest request) {
        LimitKeyEntity entity = LimitKeyMapper.create(request);
        limitKeyRepository.save(entity);
        log.info("LimitKey created with id: {}", entity.getId());
        return new SuccessResponse(true, entity.getId());
    }

    @Override
    public LimitKeyEntity getEntityById(Long id) {
        return limitKeyRepository.findByIdAndIsActiveAndIsDeleted(id, true, false)
                .orElseThrow(() -> new EntityNotFoundException("LimitKey not found with id: " + id));
    }

    @Override
    public LimitKeyResponse getById(Long id) {
        LimitKeyDto dto = LimitKeyMapper.toDto(getEntityById(id));
        return new LimitKeyResponse(dto);
    }

    @Override
    public PaginatedResponse<LimitKeySummary> getAll(PaginatedRequest request) {
        Page<@NonNull LimitKeySummary> page = limitKeyRepository.findAllByIsActiveAndIsDeleted(
                true, false, request.toPageable(ALLOWED_SORT_FIELDS)
        );
        return Pagination.buildPaginatedResponse(page);
    }

    @Transactional
    @Override
    public SuccessResponse update(LimitKeyEntity entity, LimitKeyUpdateRequest request) {
        LimitKeyMapper.update(entity, request);
        limitKeyRepository.save(entity);
        log.info("LimitKey updated with id: {}", entity.getId());
        return new SuccessResponse(true, entity.getId());
    }

    @Transactional
    @Override
    public SuccessResponse delete(LimitKeyEntity entity) {
        entity.setIsDeleted(true);
        entity.setIsActive(false);
        limitKeyRepository.save(entity);
        log.info("LimitKey soft-deleted with id: {}", entity.getId());
        return new SuccessResponse(true, entity.getId());
    }
}
