package org.example.loficonnect.payment.dto.webhook;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

/**
 * Paddle transaction event data for transaction.completed events.
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class PaddleTransactionEventData {

    /** Paddle transaction ID, e.g. "txn_01h..." */
    private String id;

    /** Associated Paddle subscription ID (present for recurring transactions). */
    private String subscriptionId;

    /** Paddle customer ID. */
    private String customerId;

    /**
     * The billing period this transaction covers.
     * Present on subscription transactions; used to set {@code end_date} on the local subscription.
     */
    private PaddleBillingPeriod billingPeriod;

    /** Custom metadata set at transaction creation time. Contains user_id and plan_id. */
    private PaddleCustomData customData;
}
