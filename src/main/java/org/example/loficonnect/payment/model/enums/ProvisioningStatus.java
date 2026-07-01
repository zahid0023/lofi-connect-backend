package org.example.loficonnect.payment.model.enums;

public enum ProvisioningStatus {
    /** Waiting for provisioning (used for BUNDLED plans or before any processing). */
    PENDING,
    /** Provisioning completed successfully. */
    PROVISIONED,
    /** Provisioning failed and needs attention. */
    FAILED
}
