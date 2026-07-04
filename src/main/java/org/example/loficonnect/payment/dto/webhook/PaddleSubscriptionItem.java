package org.example.loficonnect.payment.dto.webhook;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

/**
 * A single line item on a Paddle subscription (price + quantity).
 * Used in subscription.updated events to detect plan changes.
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class PaddleSubscriptionItem {

    private PaddleSubscriptionItemPrice price;
    private Integer quantity;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class PaddleSubscriptionItemPrice {
        /** Paddle price ID, e.g. "pri_01h..." — matched against SubscriptionPlanEntity.paddlePriceId. */
        private String id;
    }
}
