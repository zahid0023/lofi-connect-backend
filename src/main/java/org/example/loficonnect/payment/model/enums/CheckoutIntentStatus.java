package org.example.loficonnect.payment.model.enums;

public enum CheckoutIntentStatus {
    /** Checkout opened, awaiting user payment. */
    PENDING,
    /** Subscription was created successfully after payment. */
    COMPLETED,
    /** User did not complete checkout within the expiry window (48–72h). */
    EXPIRED,
    /** User closed checkout without completing (best-effort detection). */
    ABANDONED
}
