package org.example.loficonnect.subscription.model.enums;

public enum TenantSubscriptionStatus {
    TRIAL,
    ACTIVE,
    CANCELLED,
    EXPIRED,
    /** Payment failed; subscription is in a grace period awaiting payment. */
    PAST_DUE,
    /** Subscription is temporarily paused (Paddle-level pause). */
    PAUSED
}
