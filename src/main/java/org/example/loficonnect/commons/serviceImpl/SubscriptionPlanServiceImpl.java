package org.example.loficonnect.commons.serviceImpl;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.example.loficonnect.commons.dto.request.PaginatedRequest;
import org.example.loficonnect.commons.dto.request.SubscriptionPlanCreateRequest;
import org.example.loficonnect.commons.dto.request.SubscriptionPlanLimitRequest;
import org.example.loficonnect.commons.dto.request.SubscriptionPlanUpdateRequest;
import org.example.loficonnect.commons.dto.response.PaginatedResponse;
import org.example.loficonnect.commons.dto.response.SubscriptionPlanResponse;
import org.example.loficonnect.commons.dto.response.SuccessResponse;
import org.example.loficonnect.commons.model.dto.SubscriptionPlanDto;
import org.example.loficonnect.commons.model.entity.CurrencyEntity;
import org.example.loficonnect.commons.model.entity.LimitKeyEntity;
import org.example.loficonnect.commons.model.entity.SubscriptionPlanEntity;
import org.example.loficonnect.commons.model.entity.SubscriptionPlanLimitEntity;
import org.example.loficonnect.commons.model.enums.SubscriptionPlanSortField;
import org.example.loficonnect.commons.model.mapper.SubscriptionPlanMapper;
import org.example.loficonnect.commons.model.projection.SubscriptionPlanSummary;
import org.example.loficonnect.commons.repository.SubscriptionPlanLimitRepository;
import org.example.loficonnect.commons.repository.SubscriptionPlanRepository;
import org.example.loficonnect.commons.service.CurrencyService;
import org.example.loficonnect.commons.service.LimitKeyService;
import org.example.loficonnect.commons.service.SubscriptionPlanService;
import org.example.loficonnect.util.Pagination;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Service
@Slf4j
public class SubscriptionPlanServiceImpl implements SubscriptionPlanService {

    private static final Set<String> ALLOWED_SORT_FIELDS = SubscriptionPlanSortField.allowedFields();

    private final SubscriptionPlanRepository subscriptionPlanRepository;
    private final SubscriptionPlanLimitRepository subscriptionPlanLimitRepository;
    private final CurrencyService currencyService;
    private final LimitKeyService limitKeyService;

    public SubscriptionPlanServiceImpl(SubscriptionPlanRepository subscriptionPlanRepository,
                                       SubscriptionPlanLimitRepository subscriptionPlanLimitRepository,
                                       CurrencyService currencyService,
                                       LimitKeyService limitKeyService) {
        this.subscriptionPlanRepository = subscriptionPlanRepository;
        this.subscriptionPlanLimitRepository = subscriptionPlanLimitRepository;
        this.currencyService = currencyService;
        this.limitKeyService = limitKeyService;
    }

    @Transactional
    @Override
    public SuccessResponse create(SubscriptionPlanCreateRequest request) {
        CurrencyEntity currency = currencyService.getEntityById(request.getCurrencyId());
        SubscriptionPlanEntity entity = SubscriptionPlanMapper.create(request, currency);
        subscriptionPlanRepository.save(entity);
        saveLimits(entity, request.getLimits());
        log.info("SubscriptionPlan created with id: {}", entity.getId());
        return new SuccessResponse(true, entity.getId());
    }

    @Override
    public SubscriptionPlanEntity getEntityById(Long id) {
        return subscriptionPlanRepository.findByIdAndIsActiveAndIsDeleted(id, true, false)
                .orElseThrow(() -> new EntityNotFoundException("SubscriptionPlan not found with id: " + id));
    }

    @Override
    public SubscriptionPlanResponse getById(Long id) {
        SubscriptionPlanEntity entity = getEntityById(id);
        SubscriptionPlanDto dto = toDto(entity);
        return new SubscriptionPlanResponse(dto);
    }

    @Override
    public PaginatedResponse<SubscriptionPlanSummary> getAll(PaginatedRequest request) {
        Page<@NonNull SubscriptionPlanSummary> page = subscriptionPlanRepository.findAllByIsActiveAndIsDeleted(
                true, false, request.toPageable(ALLOWED_SORT_FIELDS)
        );
        return Pagination.buildPaginatedResponse(page);
    }

    @Transactional
    @Override
    public SuccessResponse update(SubscriptionPlanEntity entity, SubscriptionPlanUpdateRequest request) {
        CurrencyEntity currency = currencyService.getEntityById(request.getCurrencyId());
        SubscriptionPlanMapper.update(entity, request, currency);
        subscriptionPlanRepository.save(entity);
        subscriptionPlanLimitRepository.deleteAllBySubscriptionPlanEntity(entity);
        saveLimits(entity, request.getLimits());
        log.info("SubscriptionPlan updated with id: {}", entity.getId());
        return new SuccessResponse(true, entity.getId());
    }

    @Transactional
    @Override
    public SuccessResponse delete(SubscriptionPlanEntity entity) {
        entity.setIsDeleted(true);
        entity.setIsActive(false);
        subscriptionPlanRepository.save(entity);
        log.info("SubscriptionPlan soft-deleted with id: {}", entity.getId());
        return new SuccessResponse(true, entity.getId());
    }

    private void saveLimits(SubscriptionPlanEntity plan, List<SubscriptionPlanLimitRequest> limitRequests) {
        if (limitRequests == null || limitRequests.isEmpty()) {
            return;
        }
        List<SubscriptionPlanLimitEntity> limits = limitRequests.stream()
                .map(req -> {
                    LimitKeyEntity limitKey = limitKeyService.getEntityById(req.getLimitKeyId());
                    return SubscriptionPlanMapper.toLimitEntity(plan, limitKey, req.getLimitValue());
                })
                .toList();
        subscriptionPlanLimitRepository.saveAll(limits);
    }

    private SubscriptionPlanDto toDto(SubscriptionPlanEntity plan) {
        List<SubscriptionPlanLimitEntity> limits = subscriptionPlanLimitRepository
                .findAllBySubscriptionPlanEntityAndIsActiveAndIsDeleted(plan, true, false);
        return SubscriptionPlanMapper.toDto(plan, limits);
    }
}
