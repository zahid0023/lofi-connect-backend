package org.example.loficonnect.payment.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import org.example.loficonnect.payment.model.enums.ProvisioningStatus;
import org.example.loficonnect.subscription.model.enums.TenantSubscriptionStatus;

/**
 * Returned by GET /api/v1/payments/status so the frontend can confirm payment
 * was processed after the Paddle success redirect.
 */
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record PaymentStatusResponse(

        /** Current subscription status, or {@code null} if the user has no subscription. */
        TenantSubscriptionStatus subscriptionStatus,

        /** Whether product access has been provisioned. */
        ProvisioningStatus provisioningStatus,

        /** Convenience flag — true when subscription is ACTIVE or TRIAL and is_active = true. */
        boolean active
) {}
