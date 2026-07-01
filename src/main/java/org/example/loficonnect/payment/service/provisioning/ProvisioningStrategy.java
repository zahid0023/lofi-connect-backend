package org.example.loficonnect.payment.service.provisioning;

import org.example.loficonnect.payment.model.enums.ProductType;

/**
 * Strategy interface for post-payment provisioning.
 * Implement to add new provisioning behaviours for new product types.
 */
public interface ProvisioningStrategy {

    /**
     * Provisions access for a newly activated subscription.
     */
    void provision(ProvisioningContext context);

    /**
     * Revokes access for a cancelled or expired subscription.
     */
    void deprovision(ProvisioningContext context);

    /**
     * Returns the {@link ProductType} this strategy handles.
     */
    ProductType supports();
}
