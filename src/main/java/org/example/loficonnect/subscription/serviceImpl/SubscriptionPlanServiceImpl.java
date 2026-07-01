package org.example.loficonnect.subscription.serviceImpl;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.example.loficonnect.commons.dto.request.PaginatedRequest;
import org.example.loficonnect.commons.dto.response.PaginatedResponse;
import org.example.loficonnect.commons.dto.response.SuccessResponse;
import org.example.loficonnect.currency.model.entity.CurrencyEntity;
import org.example.loficonnect.subscription.dto.request.plan.SubscriptionPlanCreateRequest;
import org.example.loficonnect.subscription.dto.request.plan.SubscriptionPlanLimitRequest;
import org.example.loficonnect.subscription.dto.request.plan.SubscriptionPlanUpdateRequest;
import org.example.loficonnect.subscription.dto.response.SubscriptionPlanResponse;
import org.example.loficonnect.subscription.model.dto.SubscriptionPlanDto;
import org.example.loficonnect.subscription.model.entity.LimitKeyEntity;
import org.example.loficonnect.subscription.model.entity.SubscriptionPlanEntity;
import org.example.loficonnect.subscription.model.entity.SubscriptionPlanLimitEntity;
import org.example.loficonnect.subscription.model.enums.SubscriptionPlanSortField;
import org.example.loficonnect.subscription.model.mapper.SubscriptionPlanMapper;
import org.example.loficonnect.subscription.model.projection.SubscriptionPlanSummary;
import org.example.loficonnect.subscription.repository.LimitKeyRepository;
import org.example.loficonnect.subscription.repository.SubscriptionPlanLimitRepository;
import org.example.loficonnect.subscription.repository.SubscriptionPlanRepository;
import org.example.loficonnect.subscription.service.SubscriptionPlanService;
import org.example.loficonnect.util.Pagination;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Slf4j
public class SubscriptionPlanServiceImpl implements SubscriptionPlanService {

    private static final Set<String> ALLOWED_SORT_FIELDS = SubscriptionPlanSortField.allowedFields();

    private final SubscriptionPlanRepository subscriptionPlanRepository;
    private final SubscriptionPlanLimitRepository subscriptionPlanLimitRepository;
    private final LimitKeyRepository limitKeyRepository;

    public SubscriptionPlanServiceImpl(
            SubscriptionPlanRepository subscriptionPlanRepository,
            SubscriptionPlanLimitRepository subscriptionPlanLimitRepository,
            LimitKeyRepository limitKeyRepository) {
        this.subscriptionPlanRepository = subscriptionPlanRepository;
        this.subscriptionPlanLimitRepository = subscriptionPlanLimitRepository;
        this.limitKeyRepository = limitKeyRepository;
    }

    @Transactional
    @Override
    public SuccessResponse create(SubscriptionPlanCreateRequest request,
                                  CurrencyEntity currencyEntity,
                                  String paddlePriceId) {
        if (subscriptionPlanRepository.existsByCodeAndIsDeleted(request.getCode(), false)) {
            throw new IllegalArgumentException("Subscription plan with code '" + request.getCode() + "' already exists");
        }

        SubscriptionPlanEntity subscriptionPlanEntity = SubscriptionPlanMapper.create(request, currencyEntity, paddlePriceId);
        applyLimits(subscriptionPlanEntity, request.getLimits());

        subscriptionPlanRepository.save(subscriptionPlanEntity);

        log.info("SubscriptionPlan created: id={}, paddlePriceId={}", subscriptionPlanEntity.getId(), paddlePriceId);
        return new SuccessResponse(true, subscriptionPlanEntity.getId());
    }

    @Transactional(readOnly = true)
    @Override
    public SubscriptionPlanEntity getEntityById(Long id) {
        return subscriptionPlanRepository.findByIdAndIsActiveAndIsDeleted(id, true, false)
                .orElseThrow(() -> new EntityNotFoundException("SubscriptionPlan not found with id: " + id));
    }

    @Transactional(readOnly = true)
    @Override
    public SubscriptionPlanResponse getById(Long id) {
        SubscriptionPlanEntity entity = getEntityById(id);
        SubscriptionPlanDto dto = SubscriptionPlanMapper.toDto(entity);
        return new SubscriptionPlanResponse(dto);
    }

    @Transactional(readOnly = true)
    @Override
    public PaginatedResponse<SubscriptionPlanSummary> getAll(PaginatedRequest request) {
        Page<@NonNull SubscriptionPlanSummary> page = subscriptionPlanRepository.findAllByIsActiveAndIsDeleted(
                true, false, request.toPageable(ALLOWED_SORT_FIELDS)
        );
        return Pagination.buildPaginatedResponse(page);
    }

    @Transactional(readOnly = true)
    @Override
    public List<SubscriptionPlanDto> getPublicPlans() {
        return subscriptionPlanRepository.findAllPublicWithLimits()
                .stream()
                .map(SubscriptionPlanMapper::toDto)
                .toList();
    }

    @Transactional
    @Override
    public SuccessResponse update(SubscriptionPlanEntity entity, SubscriptionPlanUpdateRequest request) {

        SubscriptionPlanEntity managed = subscriptionPlanRepository.findByIdAndIsActiveAndIsDeleted(
                        entity.getId(), true, false)
                .orElseThrow(() -> new EntityNotFoundException("SubscriptionPlan not found with id: " + entity.getId()));

        SubscriptionPlanMapper.update(managed, request);

        subscriptionPlanLimitRepository.deleteAllBySubscriptionPlanId(managed.getId());
        subscriptionPlanLimitRepository.flush();

        managed.getLimits().clear();
        applyLimits(managed, request.getLimits());
        subscriptionPlanRepository.save(managed);
        log.info("SubscriptionPlan updated with id: {}", managed.getId());
        return new SuccessResponse(true, managed.getId());
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

    private void applyLimits(SubscriptionPlanEntity entity, List<SubscriptionPlanLimitRequest> limitRequests) {
        Set<Long> seen = new HashSet<>();
        for (SubscriptionPlanLimitRequest limitReq : limitRequests) {
            if (!seen.add(limitReq.getLimitKeyId())) {
                throw new IllegalArgumentException(
                        "Duplicate limit key id in request: " + limitReq.getLimitKeyId());
            }
            LimitKeyEntity limitKey = limitKeyRepository
                    .findByIdAndIsActiveAndIsDeleted(limitReq.getLimitKeyId(), true, false)
                    .orElseThrow(() -> new EntityNotFoundException(
                            "LimitKey not found with id: " + limitReq.getLimitKeyId()));

            SubscriptionPlanLimitEntity limit = new SubscriptionPlanLimitEntity();
            limit.setSubscriptionPlan(entity);
            limit.setLimitKey(limitKey);
            limit.setLimitValue(limitReq.getLimitValue());
            entity.getLimits().add(limit);
        }
    }
}
