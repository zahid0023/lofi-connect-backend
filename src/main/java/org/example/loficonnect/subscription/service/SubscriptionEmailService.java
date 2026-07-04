package org.example.loficonnect.subscription.service;

import org.example.loficonnect.subscription.model.entity.TenantSubscriptionEntity;

public interface SubscriptionEmailService {
    void sendTrialWelcome(String toEmail, TenantSubscriptionEntity subscription);
    void sendPaymentFailed(String toEmail, TenantSubscriptionEntity subscription);
    void sendGraceWarning(String toEmail, TenantSubscriptionEntity subscription);
    void sendAccessLimited(String toEmail, TenantSubscriptionEntity subscription);
    void sendSuspended(String toEmail, TenantSubscriptionEntity subscription);
    void sendCancellationConfirmed(String toEmail, TenantSubscriptionEntity subscription);
    void sendRefundRequested(String toEmail, TenantSubscriptionEntity subscription);
    void sendRefundDecision(String toEmail, TenantSubscriptionEntity subscription, boolean approved);
    void sendBundledSetupInProgress(String toEmail, TenantSubscriptionEntity subscription);
    void sendBundledSetupComplete(String toEmail, TenantSubscriptionEntity subscription);
    void sendPlanChanged(String toEmail, TenantSubscriptionEntity subscription,
                         String oldPlanName, String newPlanName);
    void sendCheckoutReminder(String toEmail, String planName);
}
