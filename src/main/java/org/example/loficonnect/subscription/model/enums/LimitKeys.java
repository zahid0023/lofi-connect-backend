package org.example.loficonnect.subscription.model.enums;

public final class LimitKeys {
    private LimitKeys() {}

    /** Maximum number of active API keys a user may generate. */
    public static final String APP_KEYS = "APP_KEYS";

    /**
     * Maximum GHL API calls a user may make per calendar month.
     * Enforced by {@code AppKeyInterceptor} and displayed on the usage dashboard.
     * Set this limit on each plan via the admin limit-keys → plan limits UI.
     */
    public static final String MONTHLY_OPERATIONS = "MONTHLY_OPERATIONS";
}
