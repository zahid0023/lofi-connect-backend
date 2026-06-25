package org.example.loficonnect.subscription.service;

public interface UsageEnforcementService {

    /**
     * Throws {@link org.example.loficonnect.subscription.exception.UsageLimitExceededException}
     * if {@code currentUsage} has reached or exceeded the plan limit for the given key code.
     * If no limit is configured for the key, the call is a no-op.
     */
    void enforce(Long userId, String limitKeyCode, long currentUsage);
}
