package org.example.loficonnect.subscription.serviceImpl;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.example.loficonnect.commons.dto.request.PaginatedRequest;
import org.example.loficonnect.commons.dto.response.PaginatedResponse;
import org.example.loficonnect.commons.dto.response.SuccessResponse;
import org.example.loficonnect.subscription.dto.request.tenantsubscription.SubscribeRequest;
import org.example.loficonnect.subscription.dto.request.tenantsubscription.UpgradePlanRequest;
import org.example.loficonnect.subscription.dto.response.TenantSubscriptionResponse;
import org.example.loficonnect.subscription.exception.ActiveSubscriptionExistsException;
import org.example.loficonnect.subscription.exception.NoActiveSubscriptionException;
import org.example.loficonnect.subscription.model.dto.TenantSubscriptionDto;
import org.example.loficonnect.subscription.model.entity.SubscriptionPlanEntity;
import org.example.loficonnect.subscription.model.entity.TenantSubscriptionEntity;
import org.example.loficonnect.subscription.model.enums.BillingCycle;
import org.example.loficonnect.subscription.model.enums.TenantSubscriptionSortField;
import org.example.loficonnect.subscription.model.enums.TenantSubscriptionStatus;
import org.example.loficonnect.subscription.model.mapper.TenantSubscriptionMapper;
import org.example.loficonnect.subscription.model.projection.TenantSubscriptionSummary;
import org.example.loficonnect.subscription.repository.SubscriptionPlanRepository;
import org.example.loficonnect.subscription.repository.TenantSubscriptionRepository;
import org.example.loficonnect.subscription.service.TenantSubscriptionService;
import org.example.loficonnect.util.Pagination;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Set;

@Service
@Slf4j
public class TenantSubscriptionServiceImpl implements TenantSubscriptionService {

    private static final List<TenantSubscriptionStatus> ACTIVE_STATUSES =
            List.of(TenantSubscriptionStatus.ACTIVE, TenantSubscriptionStatus.TRIAL);

    private static final Set<String> ALLOWED_SORT_FIELDS = TenantSubscriptionSortField.allowedFields();

    private final TenantSubscriptionRepository tenantSubscriptionRepository;
    private final SubscriptionPlanRepository subscriptionPlanRepository;

    public TenantSubscriptionServiceImpl(
            TenantSubscriptionRepository tenantSubscriptionRepository,
            SubscriptionPlanRepository subscriptionPlanRepository) {
        this.tenantSubscriptionRepository = tenantSubscriptionRepository;
        this.subscriptionPlanRepository = subscriptionPlanRepository;
    }

    @Transactional
    @Override
    public SuccessResponse subscribe(Long userId, SubscribeRequest request) {
        if (tenantSubscriptionRepository.existsByUserIdAndStatusIn(userId, ACTIVE_STATUSES)) {
            throw new ActiveSubscriptionExistsException(
                    "An active subscription already exists. Use the upgrade endpoint to switch plans.");
        }

        SubscriptionPlanEntity plan = getActivePlan(request.getPlanId());

        TenantSubscriptionEntity subscription = buildSubscription(userId, plan);
        tenantSubscriptionRepository.save(subscription);

        log.info("User {} subscribed to plan {} (id: {})", userId, plan.getCode(), subscription.getId());
        return new SuccessResponse(true, subscription.getId());
    }

    @Transactional
    @Override
    public SuccessResponse upgrade(Long userId, UpgradePlanRequest request) {
        TenantSubscriptionEntity current = tenantSubscriptionRepository
                .findByUserIdAndStatusIn(userId, ACTIVE_STATUSES)
                .orElseThrow(() -> new NoActiveSubscriptionException(
                        "No active subscription found. Subscribe first before upgrading."));

        SubscriptionPlanEntity newPlan = getActivePlan(request.getNewPlanId());

        current.setStatus(TenantSubscriptionStatus.CANCELLED);
        tenantSubscriptionRepository.save(current);

        TenantSubscriptionEntity upgraded = buildSubscription(userId, newPlan);
        tenantSubscriptionRepository.save(upgraded);

        log.info("User {} upgraded from plan {} to plan {}", userId,
                current.getSubscriptionPlan().getCode(), newPlan.getCode());
        return new SuccessResponse(true, upgraded.getId());
    }

    @Transactional(readOnly = true)
    @Override
    public TenantSubscriptionResponse getMyActiveSubscription(Long userId) {
        TenantSubscriptionEntity subscription = tenantSubscriptionRepository
                .findByUserIdAndStatusIn(userId, ACTIVE_STATUSES)
                .orElseThrow(() -> new NoActiveSubscriptionException(
                        "No active subscription found for user: " + userId));

        TenantSubscriptionDto dto = TenantSubscriptionMapper.toDto(subscription);
        return new TenantSubscriptionResponse(dto);
    }

    @Transactional
    @Override
    public SuccessResponse cancel(Long userId) {
        TenantSubscriptionEntity subscription = tenantSubscriptionRepository
                .findByUserIdAndStatusIn(userId, ACTIVE_STATUSES)
                .orElseThrow(() -> new NoActiveSubscriptionException(
                        "No active subscription found to cancel."));

        subscription.setStatus(TenantSubscriptionStatus.CANCELLED);
        subscription.setIsActive(false);
        tenantSubscriptionRepository.save(subscription);

        log.info("User {} cancelled subscription id: {}", userId, subscription.getId());
        return new SuccessResponse(true, subscription.getId());
    }

    @Transactional(readOnly = true)
    @Override
    public PaginatedResponse<TenantSubscriptionSummary> getAll(PaginatedRequest request) {
        Page<@NonNull TenantSubscriptionSummary> page = tenantSubscriptionRepository
                .findAllByIsActiveAndIsDeleted(true, false, request.toPageable(ALLOWED_SORT_FIELDS));
        return Pagination.buildPaginatedResponse(page);
    }

    private SubscriptionPlanEntity getActivePlan(Long planId) {
        return subscriptionPlanRepository.findByIdAndIsActiveAndIsDeleted(planId, true, false)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Subscription plan not found with id: " + planId));
    }

    private TenantSubscriptionEntity buildSubscription(Long userId, SubscriptionPlanEntity plan) {
        Instant now = Instant.now();
        TenantSubscriptionEntity subscription = new TenantSubscriptionEntity();
        subscription.setUserId(userId);
        subscription.setSubscriptionPlan(plan);
        subscription.setStartDate(now);
        subscription.setEndDate(calculateEndDate(now, plan.getBillingCycle()));

        if (plan.getTrialPeriodDays() > 0) {
            subscription.setStatus(TenantSubscriptionStatus.TRIAL);
            subscription.setTrialEndsAt(now.plus(plan.getTrialPeriodDays(), ChronoUnit.DAYS));
        } else {
            subscription.setStatus(TenantSubscriptionStatus.ACTIVE);
        }

        return subscription;
    }

    private Instant calculateEndDate(Instant start, BillingCycle billingCycle) {
        return switch (billingCycle) {
            case MONTHLY -> start.plus(30, ChronoUnit.DAYS);
            case QUARTERLY -> start.plus(90, ChronoUnit.DAYS);
            case ANNUAL -> start.plus(365, ChronoUnit.DAYS);
            case LIFETIME -> null;
        };
    }
}
