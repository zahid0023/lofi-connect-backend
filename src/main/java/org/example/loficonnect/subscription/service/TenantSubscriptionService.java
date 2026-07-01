package org.example.loficonnect.subscription.service;

import org.example.loficonnect.commons.dto.request.PaginatedRequest;
import org.example.loficonnect.commons.dto.response.PaginatedResponse;
import org.example.loficonnect.commons.dto.response.SuccessResponse;
import org.example.loficonnect.subscription.dto.request.tenantsubscription.UpgradePlanRequest;
import org.example.loficonnect.subscription.dto.response.TenantSubscriptionResponse;
import org.example.loficonnect.subscription.model.entity.TenantSubscriptionEntity;
import org.example.loficonnect.subscription.model.projection.TenantSubscriptionSummary;

import java.time.Instant;

public interface TenantSubscriptionService {

    /**
     * Creates and saves a new active subscription after a confirmed Paddle payment.
     * Called exclusively from the webhook processor — not from user-facing endpoints.
     *
     * @return the persisted {@link TenantSubscriptionEntity} so the caller can link payment details
     */
    TenantSubscriptionEntity subscribeFromPayment(Long userId, Long planId, Instant startDate, Instant endDate);

    SuccessResponse upgrade(Long userId, UpgradePlanRequest request);

    TenantSubscriptionResponse getMyActiveSubscription(Long userId);

    SuccessResponse cancel(Long userId);

    PaginatedResponse<TenantSubscriptionSummary> getAll(PaginatedRequest request);
}
