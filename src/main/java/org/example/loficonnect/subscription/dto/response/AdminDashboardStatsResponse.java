package org.example.loficonnect.subscription.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class AdminDashboardStatsResponse {

    /** Estimated monthly recurring revenue from active/trial subscriptions. */
    private BigDecimal estimatedMrr;

    /** Total active subscriptions (ACTIVE status). */
    private long activeSubscriptions;

    /** Subscriptions in free trial. */
    private long trialingSubscriptions;

    /** Subscriptions with past-due payments. */
    private long pastDueSubscriptions;

    /** Subscriptions in grace period. */
    private long gracePeriodSubscriptions;

    /** Subscriptions in read-only mode. */
    private long readOnlySubscriptions;

    /** Subscriptions fully suspended. */
    private long suspendedSubscriptions;

    /** Subscriptions cancelled this calendar month. */
    private long cancelledThisMonth;

    /** New customers (subscriptions created) this calendar month. */
    private long newCustomersThisMonth;

    /** Active standalone plan subscriptions. */
    private long standaloneActive;

    /** Active bundled plan subscriptions. */
    private long bundledActive;

    /** Bundled subscriptions awaiting manual GHL provisioning. */
    private long pendingProvisioning;

    /** Refund requests currently under review. */
    private long pendingRefundRequests;
}
