package org.example.loficonnect.subscription.service;

import org.example.loficonnect.commons.dto.response.SuccessResponse;
import org.example.loficonnect.subscription.dto.response.AdminDashboardStatsResponse;
import org.example.loficonnect.subscription.dto.response.ProvisioningQueueItemResponse;

import java.util.List;

public interface AdminDashboardService {

    /** Returns aggregate stats for the admin dashboard. */
    AdminDashboardStatsResponse getStats();

    /** Returns bundled subscriptions awaiting or in-progress GHL provisioning. */
    List<ProvisioningQueueItemResponse> getProvisioningQueue();

    /** Admin marks a bundled subscription's provisioning as started (IN_PROGRESS). */
    SuccessResponse markProvisioningInProgress(Long subscriptionId, Long adminId);

    /** Admin marks a bundled subscription's provisioning as complete. */
    SuccessResponse completeProvisioning(Long subscriptionId, Long adminId);
}
