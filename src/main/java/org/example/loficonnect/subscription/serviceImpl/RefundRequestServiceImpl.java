package org.example.loficonnect.subscription.serviceImpl;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.example.loficonnect.subscription.dto.request.tenantsubscription.ReviewRefundRequest;
import org.example.loficonnect.subscription.dto.request.tenantsubscription.SubmitRefundRequest;
import org.example.loficonnect.subscription.dto.response.RefundRequestResponse;
import org.example.loficonnect.subscription.exception.NoActiveSubscriptionException;
import org.example.loficonnect.subscription.model.entity.RefundRequestEntity;
import org.example.loficonnect.subscription.model.entity.TenantSubscriptionEntity;
import org.example.loficonnect.subscription.model.enums.AuditEventType;
import org.example.loficonnect.subscription.model.enums.RefundRequestStatus;
import org.example.loficonnect.subscription.model.enums.TenantSubscriptionStatus;
import org.example.loficonnect.subscription.repository.RefundRequestRepository;
import org.example.loficonnect.subscription.repository.TenantSubscriptionRepository;
import org.example.loficonnect.subscription.service.AuditLogService;
import org.example.loficonnect.subscription.service.RefundRequestService;
import org.example.loficonnect.subscription.service.SubscriptionEmailService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class RefundRequestServiceImpl implements RefundRequestService {

    private static final List<TenantSubscriptionStatus> ACTIVE_ACCESS_STATUSES = List.of(
            TenantSubscriptionStatus.ACTIVE, TenantSubscriptionStatus.TRIAL,
            TenantSubscriptionStatus.GRACE_PERIOD, TenantSubscriptionStatus.READ_ONLY,
            TenantSubscriptionStatus.PAST_DUE
    );

    private static final List<TenantSubscriptionStatus> REFUNDABLE_STATUSES = List.of(
            TenantSubscriptionStatus.ACTIVE, TenantSubscriptionStatus.TRIAL,
            TenantSubscriptionStatus.PAST_DUE, TenantSubscriptionStatus.GRACE_PERIOD,
            TenantSubscriptionStatus.READ_ONLY, TenantSubscriptionStatus.CANCELLED
    );

    private final RefundRequestRepository refundRequestRepository;
    private final TenantSubscriptionRepository tenantSubscriptionRepository;
    private final AuditLogService auditLogService;
    private final SubscriptionEmailService emailService;

    public RefundRequestServiceImpl(
            RefundRequestRepository refundRequestRepository,
            TenantSubscriptionRepository tenantSubscriptionRepository,
            AuditLogService auditLogService,
            SubscriptionEmailService emailService) {
        this.refundRequestRepository = refundRequestRepository;
        this.tenantSubscriptionRepository = tenantSubscriptionRepository;
        this.auditLogService = auditLogService;
        this.emailService = emailService;
    }

    @Transactional
    @Override
    public RefundRequestResponse submit(Long userId, SubmitRefundRequest request) {
        TenantSubscriptionEntity sub = tenantSubscriptionRepository
                .findByUserIdAndStatusIn(userId, REFUNDABLE_STATUSES)
                .orElseThrow(() -> new NoActiveSubscriptionException(
                        "No eligible subscription found to request a refund for."));

        // Guard: only one pending request per subscription
        refundRequestRepository.findByTenantSubscriptionIdAndStatus(
                sub.getId(), RefundRequestStatus.PENDING)
                .ifPresent(existing -> {
                    throw new IllegalStateException(
                            "A refund request is already pending for this subscription.");
                });

        TenantSubscriptionStatus previousStatus = sub.getStatus();

        RefundRequestEntity entity = new RefundRequestEntity();
        entity.setTenantSubscriptionId(sub.getId());
        entity.setUserId(userId);
        entity.setReason(request.getReason());
        entity.setStatus(RefundRequestStatus.PENDING);
        entity.setPreviousStatus(previousStatus);
        refundRequestRepository.save(entity);

        // Do NOT change subscription status until admin approves — user retains access while pending
        auditLogService.logUser(sub.getId(), userId, AuditEventType.REFUND_REQUESTED,
                previousStatus.name(), "REFUND_REQUEST_PENDING");

        log.info("Refund request submitted: userId={}, subscriptionId={}", userId, sub.getId());
        return toResponse(entity);
    }

    @Transactional(readOnly = true)
    @Override
    public List<RefundRequestResponse> getMyRequests(Long userId) {
        return refundRequestRepository.findByUserId(userId).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public RefundRequestResponse approve(Long requestId, Long adminId, ReviewRefundRequest review) {
        RefundRequestEntity entity = getOrThrow(requestId);
        assertPending(entity);

        entity.setStatus(RefundRequestStatus.APPROVED);
        entity.setReviewedBy(adminId);
        entity.setReviewedAt(Instant.now());
        entity.setAdminNotes(review.getAdminNotes());
        refundRequestRepository.save(entity);

        // Approval: revoke access immediately (admin will process payment in Paddle)
        tenantSubscriptionRepository.findById(entity.getTenantSubscriptionId()).ifPresent(sub -> {
            TenantSubscriptionStatus previous = sub.getStatus();
            sub.setStatus(TenantSubscriptionStatus.REFUND_REQUESTED);
            sub.setIsActive(false);
            tenantSubscriptionRepository.save(sub);
            auditLogService.logAdmin(sub.getId(), adminId, AuditEventType.REFUND_APPROVED,
                    previous.name(), TenantSubscriptionStatus.REFUND_REQUESTED.name());
        });

        log.info("Refund request {} approved by admin {}", requestId, adminId);
        return toResponse(entity);
    }

    @Transactional
    @Override
    public RefundRequestResponse reject(Long requestId, Long adminId, ReviewRefundRequest review) {
        RefundRequestEntity entity = getOrThrow(requestId);
        assertPending(entity);

        entity.setStatus(RefundRequestStatus.REJECTED);
        entity.setReviewedBy(adminId);
        entity.setReviewedAt(Instant.now());
        entity.setAdminNotes(review.getAdminNotes());
        refundRequestRepository.save(entity);

        // Restore subscription to the status it had before the refund was submitted
        TenantSubscriptionStatus restoreStatus = entity.getPreviousStatus() != null
                ? entity.getPreviousStatus()
                : TenantSubscriptionStatus.ACTIVE;
        tenantSubscriptionRepository.findById(entity.getTenantSubscriptionId()).ifPresent(sub -> {
            sub.setStatus(restoreStatus);
            sub.setIsActive(ACTIVE_ACCESS_STATUSES.contains(restoreStatus));
            tenantSubscriptionRepository.save(sub);
            auditLogService.logAdmin(sub.getId(), adminId, AuditEventType.REFUND_REJECTED,
                    TenantSubscriptionStatus.REFUND_REQUESTED.name(), restoreStatus.name());
        });

        log.info("Refund request {} rejected by admin {}", requestId, adminId);
        return toResponse(entity);
    }

    @Transactional(readOnly = true)
    @Override
    public List<RefundRequestResponse> getAllPending() {
        return refundRequestRepository.findByStatus(RefundRequestStatus.PENDING,
                        org.springframework.data.domain.Pageable.unpaged())
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    // ─── Helpers ──────────────────────────────────────────────────────────────

    private RefundRequestEntity getOrThrow(Long id) {
        return refundRequestRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Refund request not found: " + id));
    }

    private void assertPending(RefundRequestEntity entity) {
        if (entity.getStatus() != RefundRequestStatus.PENDING) {
            throw new IllegalStateException(
                    "Refund request " + entity.getId() + " is already " + entity.getStatus());
        }
    }

    private RefundRequestResponse toResponse(RefundRequestEntity e) {
        RefundRequestResponse r = new RefundRequestResponse();
        r.setId(e.getId());
        r.setTenantSubscriptionId(e.getTenantSubscriptionId());
        r.setUserId(e.getUserId());
        r.setReason(e.getReason());
        r.setStatus(e.getStatus());
        r.setAdminNotes(e.getAdminNotes());
        r.setReviewedBy(e.getReviewedBy());
        r.setReviewedAt(e.getReviewedAt());
        r.setCreatedAt(e.getCreatedAt());
        return r;
    }
}
