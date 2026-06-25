package org.example.loficonnect.subscription.service;

import org.example.loficonnect.commons.dto.request.PaginatedRequest;
import org.example.loficonnect.commons.dto.response.PaginatedResponse;
import org.example.loficonnect.commons.dto.response.SuccessResponse;
import org.example.loficonnect.subscription.dto.request.tenantsubscription.SubscribeRequest;
import org.example.loficonnect.subscription.dto.request.tenantsubscription.UpgradePlanRequest;
import org.example.loficonnect.subscription.dto.response.TenantSubscriptionResponse;
import org.example.loficonnect.subscription.model.projection.TenantSubscriptionSummary;

public interface TenantSubscriptionService {

    SuccessResponse subscribe(Long userId, SubscribeRequest request);

    SuccessResponse upgrade(Long userId, UpgradePlanRequest request);

    TenantSubscriptionResponse getMyActiveSubscription(Long userId);

    SuccessResponse cancel(Long userId);

    PaginatedResponse<TenantSubscriptionSummary> getAll(PaginatedRequest request);
}
