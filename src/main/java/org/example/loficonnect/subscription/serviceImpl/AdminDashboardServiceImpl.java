package org.example.loficonnect.subscription.serviceImpl;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.example.loficonnect.commons.dto.response.SuccessResponse;
import org.example.loficonnect.payment.model.enums.ProductType;
import org.example.loficonnect.payment.model.enums.ProvisioningStatus;
import org.example.loficonnect.subscription.dto.response.AdminDashboardStatsResponse;
import org.example.loficonnect.subscription.dto.response.ProvisioningQueueItemResponse;
import org.example.loficonnect.subscription.model.entity.TenantSubscriptionEntity;
import org.example.loficonnect.subscription.model.enums.AuditEventType;
import org.example.loficonnect.subscription.model.enums.RefundRequestStatus;
import org.example.loficonnect.subscription.model.enums.TenantSubscriptionStatus;
import org.example.loficonnect.subscription.repository.RefundRequestRepository;
import org.example.loficonnect.subscription.repository.TenantSubscriptionRepository;
import org.example.loficonnect.subscription.service.AdminDashboardService;
import org.example.loficonnect.subscription.service.AuditLogService;
import org.example.loficonnect.subscription.service.SubscriptionEmailService;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class AdminDashboardServiceImpl implements AdminDashboardService {

    private static final List<TenantSubscriptionStatus> ACTIVE_STATUSES =
            List.of(TenantSubscriptionStatus.ACTIVE, TenantSubscriptionStatus.TRIAL,
                    TenantSubscriptionStatus.GRACE_PERIOD);

    private final TenantSubscriptionRepository subscriptionRepository;
    private final RefundRequestRepository refundRequestRepository;
    private final AuditLogService auditLogService;
    private final SubscriptionEmailService emailService;

    public AdminDashboardServiceImpl(
            TenantSubscriptionRepository subscriptionRepository,
            RefundRequestRepository refundRequestRepository,
            AuditLogService auditLogService,
            SubscriptionEmailService emailService) {
        this.subscriptionRepository = subscriptionRepository;
        this.refundRequestRepository = refundRequestRepository;
        this.auditLogService = auditLogService;
        this.emailService = emailService;
    }

    @Transactional(readOnly = true)
    @Override
    public AdminDashboardStatsResponse getStats() {
        ZonedDateTime nowUtc = ZonedDateTime.now(ZoneOffset.UTC);
        Instant startOfMonth = nowUtc.withDayOfMonth(1)
                .truncatedTo(ChronoUnit.DAYS)
                .toInstant();

        return AdminDashboardStatsResponse.builder()
                .estimatedMrr(subscriptionRepository.estimateMrr(ACTIVE_STATUSES))
                .activeSubscriptions(subscriptionRepository.countByStatus(TenantSubscriptionStatus.ACTIVE))
                .trialingSubscriptions(subscriptionRepository.countByStatus(TenantSubscriptionStatus.TRIAL))
                .pastDueSubscriptions(subscriptionRepository.countByStatus(TenantSubscriptionStatus.PAST_DUE))
                .gracePeriodSubscriptions(subscriptionRepository.countByStatus(TenantSubscriptionStatus.GRACE_PERIOD))
                .readOnlySubscriptions(subscriptionRepository.countByStatus(TenantSubscriptionStatus.READ_ONLY))
                .suspendedSubscriptions(subscriptionRepository.countByStatus(TenantSubscriptionStatus.SUSPENDED))
                .cancelledThisMonth(subscriptionRepository.countByCancelledAtAfterAndStatus(
                        startOfMonth, TenantSubscriptionStatus.CANCELLED))
                .newCustomersThisMonth(subscriptionRepository.countByCreatedAtAfterAndStatusIn(
                        startOfMonth, ACTIVE_STATUSES))
                .standaloneActive(subscriptionRepository.countByStatusInAndProductType(
                        ACTIVE_STATUSES, ProductType.STANDALONE))
                .bundledActive(subscriptionRepository.countByStatusInAndProductType(
                        ACTIVE_STATUSES, ProductType.BUNDLED))
                .pendingProvisioning(subscriptionRepository
                        .countBundledAwaitingProvisioning(List.of(ProvisioningStatus.PENDING, ProvisioningStatus.IN_PROGRESS)))
                .pendingRefundRequests(refundRequestRepository
                        .findByStatus(RefundRequestStatus.PENDING, Pageable.unpaged())
                        .getTotalElements())
                .build();
    }

    @Transactional(readOnly = true)
    @Override
    public List<ProvisioningQueueItemResponse> getProvisioningQueue() {
        return subscriptionRepository
                .findBundledAwaitingProvisioning(List.of(ProvisioningStatus.PENDING, ProvisioningStatus.IN_PROGRESS))
                .stream()
                .map(this::toQueueItem)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public SuccessResponse markProvisioningInProgress(Long subscriptionId, Long adminId) {
        TenantSubscriptionEntity sub = getOrThrow(subscriptionId);
        sub.setProvisioningStatus(ProvisioningStatus.IN_PROGRESS);
        sub.setStatus(TenantSubscriptionStatus.PROVISIONING_IN_PROGRESS);
        subscriptionRepository.save(sub);

        auditLogService.logAdmin(subscriptionId, adminId, AuditEventType.BUNDLED_PROVISIONING_STARTED,
                ProvisioningStatus.PENDING.name(), ProvisioningStatus.IN_PROGRESS.name());

        log.info("Provisioning started for subscription {} by admin {}", subscriptionId, adminId);
        return new SuccessResponse(true, subscriptionId);
    }

    @Transactional
    @Override
    public SuccessResponse completeProvisioning(Long subscriptionId, Long adminId) {
        TenantSubscriptionEntity sub = getOrThrow(subscriptionId);
        sub.setProvisioningStatus(ProvisioningStatus.PROVISIONED);
        sub.setStatus(TenantSubscriptionStatus.ACTIVE);
        sub.setIsActive(true);
        subscriptionRepository.save(sub);

        auditLogService.logAdmin(subscriptionId, adminId, AuditEventType.BUNDLED_PROVISIONING_COMPLETED,
                ProvisioningStatus.IN_PROGRESS.name(), ProvisioningStatus.PROVISIONED.name());

        log.info("Provisioning completed for subscription {} by admin {}", subscriptionId, adminId);
        return new SuccessResponse(true, subscriptionId);
    }

    // ─── Helpers ──────────────────────────────────────────────────────────────

    private TenantSubscriptionEntity getOrThrow(Long id) {
        return subscriptionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Subscription not found: " + id));
    }

    private ProvisioningQueueItemResponse toQueueItem(TenantSubscriptionEntity sub) {
        ProvisioningQueueItemResponse r = new ProvisioningQueueItemResponse();
        r.setSubscriptionId(sub.getId());
        r.setUserId(sub.getUserId());
        r.setPlanId(sub.getSubscriptionPlan().getId());
        r.setPlanName(sub.getSubscriptionPlan().getName());
        r.setPlanCode(sub.getSubscriptionPlan().getCode());
        r.setSubscriptionStatus(sub.getStatus());
        r.setProvisioningStatus(sub.getProvisioningStatus());
        r.setStartDate(sub.getStartDate());
        r.setCreatedAt(sub.getCreatedAt());
        return r;
    }
}
