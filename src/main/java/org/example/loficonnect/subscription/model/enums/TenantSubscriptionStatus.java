package org.example.loficonnect.subscription.model.enums;

public enum TenantSubscriptionStatus {
    /** Pre-checkout state before the user opens Paddle Checkout. */
    DRAFT,
    /** User has opened Paddle Checkout but not yet completed payment. */
    CHECKOUT_STARTED,
    /** Subscription is in a free trial period. */
    TRIAL,
    /** Subscription is active and paid. */
    ACTIVE,
    /** Bundled plan paid; awaiting manual GHL subaccount setup by Admin/Finance. */
    PROVISIONING_REQUIRED,
    /** GHL subaccount setup is actively in progress. */
    PROVISIONING_IN_PROGRESS,
    /** Payment failed; subscription is past due and awaiting payment recovery. */
    PAST_DUE,
    /** Short grace window (3–7 days) after payment failure before access is limited. */
    GRACE_PERIOD,
    /** Dashboard access only; API and Zapier operations are blocked. */
    READ_ONLY,
    /** All API and Zapier access suspended after read-only period expires. */
    SUSPENDED,
    /** Subscription temporarily paused (Paddle-level pause). */
    PAUSED,
    /** Subscription cancelled; access continues until end_date. */
    CANCELLED,
    /** Access period ended after cancellation. */
    EXPIRED,
    /** User submitted a refund request; pending admin review. */
    REFUND_REQUESTED,
    /** Refund approved and processed via Paddle. */
    REFUNDED,
    /** Local state does not match Paddle state; admin review required. */
    SYNC_ERROR,
    /** Identity or payment mismatch detected; admin verification required. */
    REVIEW_REQUIRED
}
