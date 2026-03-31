package org.example.loficonnect.commons.service;

import org.example.loficonnect.auth.model.enitty.LofiConnectAppKeyEntity;
import org.example.loficonnect.auth.model.enitty.UserEntity;
import org.example.loficonnect.commons.model.entity.TenantSubscriptionEntity;

public interface UsageEnforcementService {

    /**
     * Validates the app key, checks the tenant has an active subscription,
     * and enforces all quota limits. Throws on any failure.
     *
     * @throws org.example.loficonnect.commons.exception.AppKeyInvalidException     401 – key missing or inactive
     * @throws org.example.loficonnect.commons.exception.SubscriptionInvalidException 403 – no active / expired subscription
     * @throws org.example.loficonnect.commons.exception.QuotaExceededException      429 – a quota limit is exceeded
     */
    EnforcementResult enforce(String appKeyValue);

    /**
     * Increments usage counters after the request completes.
     * API logging is not yet implemented.
     */
    void recordUsage(EnforcementResult result, String endpoint, String method, int statusCode);

    record EnforcementResult(
            UserEntity tenant,
            LofiConnectAppKeyEntity appKey,
            TenantSubscriptionEntity subscription
    ) {}
}
