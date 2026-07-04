package org.example.loficonnect.payment.dto.webhook;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.time.Instant;
import java.util.List;

/**
 * Paddle subscription event data, shared across:
 * subscription.created / activated / updated / cancelled / past_due / paused / resumed
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class PaddleSubscriptionEventData {

    /** Paddle subscription ID, e.g. "sub_01h..." */
    private String id;

    /** Paddle customer ID, e.g. "ctm_01h..." */
    private String customerId;

    /**
     * Paddle subscription status.
     * Values: trialing | active | canceled | past_due | paused
     */
    private String status;

    /** Billing period of the current cycle. */
    private PaddleBillingPeriod currentBillingPeriod;

    /** Set when the subscription is in a trial. */
    private PaddleBillingPeriod trialDates;

    /** Custom metadata set at transaction creation time. */
    private PaddleCustomData customData;

    /** When the subscription was first created at Paddle. */
    private Instant createdAt;

    /**
     * Line items on the subscription — populated on subscription.updated events.
     * Used to detect plan changes: compare items[0].price.id against local paddlePriceId.
     */
    private List<PaddleSubscriptionItem> items;

    /**
     * A future change scheduled for next billing period (e.g. cancellation).
     * Present on subscription.updated when admin schedules an end-of-period action.
     */
    private PaddleScheduledChange scheduledChange;

    /** When the next payment is due. Present on subscription.updated. */
    private Instant nextBilledAt;
}
