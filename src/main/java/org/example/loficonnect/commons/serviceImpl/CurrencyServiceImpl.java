package org.example.loficonnect.commons.serviceImpl;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.example.loficonnect.commons.dto.request.CurrencyCreateRequest;
import org.example.loficonnect.commons.dto.request.CurrencyUpdateRequest;
import org.example.loficonnect.commons.dto.request.PaginatedRequest;
import org.example.loficonnect.commons.dto.response.CurrencyResponse;
import org.example.loficonnect.commons.dto.response.PaginatedResponse;
import org.example.loficonnect.commons.dto.response.SuccessResponse;
import org.example.loficonnect.commons.model.dto.CurrencyDto;
import org.example.loficonnect.commons.model.entity.CurrencyEntity;
import org.example.loficonnect.commons.model.enums.CurrencySortField;
import org.example.loficonnect.commons.model.mapper.CurrencyMapper;
import org.example.loficonnect.commons.model.projection.CurrencySummary;
import org.example.loficonnect.commons.repository.CurrencyRepository;
import org.example.loficonnect.commons.service.CurrencyService;
import org.example.loficonnect.util.Pagination;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
@Slf4j
public class CurrencyServiceImpl implements CurrencyService {

    private static final Set<String> ALLOWED_SORT_FIELDS = CurrencySortField.allowedFields();

    private final CurrencyRepository currencyRepository;

    public CurrencyServiceImpl(CurrencyRepository currencyRepository) {
        this.currencyRepository = currencyRepository;
    }

    @Transactional
    @Override
    public SuccessResponse create(CurrencyCreateRequest request) {
        CurrencyEntity entity = CurrencyMapper.create(request);
        currencyRepository.save(entity);
        log.info("Currency created with id: {}", entity.getId());
        return new SuccessResponse(true, entity.getId());
    }

    @Override
    public CurrencyEntity getEntityById(Long id) {
        return currencyRepository.findByIdAndIsActiveAndIsDeleted(id, true, false)
                .orElseThrow(() -> new EntityNotFoundException("Currency not found with id: " + id));
    }

    @Override
    public CurrencyResponse getById(Long id) {
        CurrencyEntity entity = getEntityById(id);
        CurrencyDto dto = CurrencyMapper.toDto(entity);
        return new CurrencyResponse(dto);
    }

    @Override
    public PaginatedResponse<CurrencySummary> getAll(PaginatedRequest request) {
        Page<@NonNull CurrencySummary> page = currencyRepository.findAllByIsActiveAndIsDeleted(
                true, false, request.toPageable(ALLOWED_SORT_FIELDS)
        );
        return Pagination.buildPaginatedResponse(page);
    }

    @Transactional
    @Override
    public SuccessResponse update(CurrencyEntity entity, CurrencyUpdateRequest request) {
        CurrencyMapper.update(entity, request);
        currencyRepository.save(entity);
        log.info("Currency updated with id: {}", entity.getId());
        return new SuccessResponse(true, entity.getId());
    }

    @Transactional
    @Override
    public SuccessResponse delete(CurrencyEntity entity) {
        entity.setIsDeleted(true);
        entity.setIsActive(false);
        currencyRepository.save(entity);
        log.info("Currency soft-deleted with id: {}", entity.getId());
        return new SuccessResponse(true, entity.getId());
    }
}
