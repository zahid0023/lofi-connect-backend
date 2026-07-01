package org.example.loficonnect.payment.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

/**
 * Returned to the frontend after a checkout session is created.
 * The frontend uses {@code checkout_url} to redirect the user to Paddle's hosted checkout page.
 */
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record CheckoutResponse(

        /** Paddle-hosted checkout page URL. Redirect the user here to complete payment. */
        String checkoutUrl,

        /** Paddle transaction ID (e.g. "txn_01h..."). Can be used with Paddle.js overlay. */
        String transactionId
) {}
