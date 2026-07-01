package org.example.loficonnect.payment.service.provisioning;

import org.example.loficonnect.payment.model.enums.ProductType;

/**
 * Immutable context passed to {@link ProvisioningStrategy}.
 */
public record ProvisioningContext(
        Long userId,
        Long tenantSubscriptionId,
        ProductType productType,
        String paddleSubscriptionId,
        String paddleCustomerId
) {}
