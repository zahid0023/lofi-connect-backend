package org.example.loficonnect.subscription.serviceImpl;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.example.loficonnect.commons.dto.request.PaginatedRequest;
import org.example.loficonnect.commons.dto.response.PaginatedResponse;
import org.example.loficonnect.commons.dto.response.SuccessResponse;
import org.example.loficonnect.payment.model.enums.ProvisioningStatus;
import org.example.loficonnect.subscription.dto.request.tenantsubscription.UpgradePlanRequest;
import org.example.loficonnect.subscription.dto.response.TenantSubscriptionResponse;
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
            List.of(TenantSubscriptionStatus.ACTIVE, TenantSubscriptionStatus.TRIAL,
                    TenantSubscriptionStatus.GRACE_PERIOD, TenantSubscriptionStatus.READ_ONLY,
                    TenantSubscriptionStatus.REFUND_REQUESTED);

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
    public TenantSubscriptionEntity subscribeFromPayment(Long userId, Long planId, Instant startDate, Instant endDate) {
        SubscriptionPlanEntity plan = getActivePlan(planId);

        TenantSubscriptionEntity subscription = new TenantSubscriptionEntity();
        subscription.setUserId(userId);
        subscription.setSubscriptionPlan(plan);
        subscription.setStatus(TenantSubscriptionStatus.ACTIVE);
        subscription.setIsActive(true);
        subscription.setProvisioningStatus(ProvisioningStatus.PENDING);
        subscription.setStartDate(startDate);
        subscription.setEndDate(endDate);
        tenantSubscriptionRepository.save(subscription);

        log.info("Subscription created from payment: userId={}, planId={}, subscriptionId={}", userId, planId, subscription.getId());
        return subscription;
    }

    /**
     * Admin-only direct plan override — bypasses Paddle completely.
     * For user-initiated upgrades, use PaymentService.upgradePlan() which goes through Paddle.
     */
    @Transactional
    @Override
    public SuccessResponse adminOverridePlan(Long userId, UpgradePlanRequest request) {
        TenantSubscriptionEntity current = tenantSubscriptionRepository
                .findByUserIdAndStatusIn(userId, ACTIVE_STATUSES)
                .orElseThrow(() -> new NoActiveSubscriptionException(
                        "No active subscription found for user: " + userId));

        SubscriptionPlanEntity newPlan = getActivePlan(request.getNewPlanId());

        String oldPlanCode = current.getSubscriptionPlan().getCode();
        current.setSubscriptionPlan(newPlan);
        tenantSubscriptionRepository.save(current);

        log.info("Admin override: userId={} plan changed {} → {}", userId, oldPlanCode, newPlan.getCode());
        return new SuccessResponse(true, current.getId());
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

    /**
     * Admin-only direct cancel — sets CANCELLED immediately without going through Paddle.
     * For user-initiated cancellations, use PaymentService.cancelUserSubscription()
     * which cancels at period end via Paddle and lets the webhook drive the local status.
     */
    @Transactional
    @Override
    public SuccessResponse adminCancelSubscription(Long userId) {
        TenantSubscriptionEntity subscription = tenantSubscriptionRepository
                .findByUserIdAndStatusIn(userId, ACTIVE_STATUSES)
                .orElseThrow(() -> new NoActiveSubscriptionException(
                        "No active subscription found for user: " + userId));

        subscription.setStatus(TenantSubscriptionStatus.CANCELLED);
        subscription.setIsActive(false);
        subscription.setCancelledAt(java.time.Instant.now());
        tenantSubscriptionRepository.save(subscription);

        log.info("Admin cancelled subscription id={} for userId={}", subscription.getId(), userId);
        return new SuccessResponse(true, subscription.getId());
    }

    @Transactional(readOnly = true)
    @Override
    public PaginatedResponse<TenantSubscriptionSummary> getAll(PaginatedRequest request) {
        Page<TenantSubscriptionSummary> page = tenantSubscriptionRepository
                .findAllByIsDeleted(false, request.toPageable(ALLOWED_SORT_FIELDS));
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
