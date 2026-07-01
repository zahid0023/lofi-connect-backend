package org.example.loficonnect.payment.dto.paddle;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

/**
 * Minimal mapping of the Paddle POST /transactions response.
 * Only the fields needed by the checkout flow are mapped.
 */
@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class PaddleCreateTransactionResponse {

    private TransactionData data;

    @Data
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class TransactionData {

        /** Paddle transaction ID, e.g. "txn_01h..." */
        private String id;

        /** Checkout details returned by Paddle. */
        private CheckoutData checkout;
    }

    @Data
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class CheckoutData {

        /** Paddle-hosted checkout page URL. Frontend redirects the user here. */
        private String url;
    }
}
