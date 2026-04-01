package org.example.loficonnect.commons.service;

import org.example.loficonnect.auth.model.enitty.UserEntity;
import org.example.loficonnect.commons.model.entity.TenantSubscriptionEntity;

public interface SubscriptionValidationService {
    /**
     * Validates that the given user has an active subscription and has not reached
     * the plan's app-key generation limit.
     *
     * @return the user's current active {@link TenantSubscriptionEntity} so the caller
     *         can link the new app key to it without a second DB lookup.
     * @throws org.example.loficonnect.commons.exception.SubscriptionInvalidException if the user
     *                                                                                has no active subscription.
     * @throws org.example.loficonnect.commons.exception.PlanLimitExceededException   if the user
     *                                                                                has already reached their plan's app-key limit.
     */
    TenantSubscriptionEntity validateAppKeyCreation(UserEntity user);
}
