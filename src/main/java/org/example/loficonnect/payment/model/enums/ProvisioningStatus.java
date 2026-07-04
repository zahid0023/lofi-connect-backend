package org.example.loficonnect.payment.model.enums;

public enum ProvisioningStatus {
    /** Waiting for provisioning (used for BUNDLED plans or before any processing). */
    PENDING,
    /** Admin has started manual GHL subaccount setup (BUNDLED only). */
    IN_PROGRESS,
    /** Provisioning completed successfully. */
    PROVISIONED,
    /** Provisioning failed and needs attention. */
    FAILED
}
