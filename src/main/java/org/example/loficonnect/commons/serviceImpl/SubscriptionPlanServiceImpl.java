package org.example.loficonnect.commons.serviceImpl;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.example.loficonnect.commons.dto.request.SubscriptionPlanCreateRequest;
import org.example.loficonnect.commons.dto.request.SubscriptionPlanLimitRequest;
import org.example.loficonnect.commons.dto.request.SubscriptionPlanUpdateRequest;
import org.example.loficonnect.commons.dto.response.SubscriptionPlanListResponse;
import org.example.loficonnect.commons.dto.response.SubscriptionPlanResponse;
import org.example.loficonnect.commons.dto.response.SuccessResponse;
import org.example.loficonnect.commons.model.dto.SubscriptionPlanDto;
import org.example.loficonnect.commons.model.entity.CurrencyEntity;
import org.example.loficonnect.commons.model.entity.LimitKeyEntity;
import org.example.loficonnect.commons.model.entity.SubscriptionPlanEntity;
import org.example.loficonnect.commons.model.entity.SubscriptionPlanLimitEntity;
import org.example.loficonnect.commons.model.mapper.SubscriptionPlanMapper;
import org.example.loficonnect.commons.repository.SubscriptionPlanLimitRepository;
import org.example.loficonnect.commons.repository.SubscriptionPlanRepository;
import org.example.loficonnect.commons.service.CurrencyService;
import org.example.loficonnect.commons.service.LimitKeyService;
import org.example.loficonnect.commons.service.SubscriptionPlanService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
public class SubscriptionPlanServiceImpl implements SubscriptionPlanService {
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

    @Override
    @Transactional
    public SuccessResponse createSubscriptionPlan(SubscriptionPlanCreateRequest request) {
        CurrencyEntity currency = currencyService.getCurrencyEntityById(request.getCurrencyId());
        SubscriptionPlanEntity plan = SubscriptionPlanMapper.fromRequest(request, currency);
        subscriptionPlanRepository.save(plan);
        saveLimits(plan, request.getLimits());
        return new SuccessResponse(true, plan.getId());
    }

    @Override
    public SubscriptionPlanEntity getSubscriptionPlanEntityById(Long id) {
        return subscriptionPlanRepository.findByIdAndIsActiveAndIsDeleted(id, true, false)
                .orElseThrow(() -> new EntityNotFoundException("SubscriptionPlan Entity not found"));
    }

    @Override
    public SubscriptionPlanResponse getSubscriptionPlanById(Long id) {
        SubscriptionPlanEntity plan = getSubscriptionPlanEntityById(id);
        SubscriptionPlanDto dto = toDto(plan);
        return new SubscriptionPlanResponse(dto);
    }

    @Override
    public SubscriptionPlanListResponse getAllSubscriptionPlans() {
        List<SubscriptionPlanDto> dtos = subscriptionPlanRepository
                .findAllByIsActiveAndIsDeleted(true, false)
                .stream()
                .map(this::toDto)
                .toList();
        return new SubscriptionPlanListResponse(dtos);
    }

    @Override
    @Transactional
    public SuccessResponse updateSubscriptionPlan(Long id, SubscriptionPlanUpdateRequest request) {
        SubscriptionPlanEntity plan = getSubscriptionPlanEntityById(id);
        CurrencyEntity currency = currencyService.getCurrencyEntityById(request.getCurrencyId());
        SubscriptionPlanMapper.update(request, plan, currency);
        subscriptionPlanRepository.save(plan);
        subscriptionPlanLimitRepository.deleteAllBySubscriptionPlanEntity(plan);
        saveLimits(plan, request.getLimits());
        return new SuccessResponse(true, plan.getId());
    }

    @Override
    @Transactional
    public SuccessResponse deleteSubscriptionPlan(Long id) {
        SubscriptionPlanEntity plan = getSubscriptionPlanEntityById(id);
        plan.setIsActive(false);
        plan.setIsDeleted(true);
        subscriptionPlanRepository.save(plan);
        return new SuccessResponse(true, plan.getId());
    }

    private void saveLimits(SubscriptionPlanEntity plan, List<SubscriptionPlanLimitRequest> limitRequests) {
        if (limitRequests == null || limitRequests.isEmpty()) {
            return;
        }
        List<SubscriptionPlanLimitEntity> limits = limitRequests.stream()
                .map(req -> {
                    LimitKeyEntity limitKey = limitKeyService.getLimitKeyEntityById(req.getLimitKeyId());
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
