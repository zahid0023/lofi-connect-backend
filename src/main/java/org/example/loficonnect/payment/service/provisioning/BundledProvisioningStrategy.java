package org.example.loficonnect.payment.service.provisioning;

import lombok.extern.slf4j.Slf4j;
import org.example.loficonnect.payment.model.enums.ProductType;
import org.example.loficonnect.payment.model.enums.ProvisioningStatus;
import org.example.loficonnect.subscription.repository.TenantSubscriptionRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Manual provisioning for BUNDLED plans.
 * GHL subaccount setup requires Admin/Finance action.
 * This strategy marks the subscription as PENDING and emits an alert for the ops team.
 */
@Slf4j
@Component
public class BundledProvisioningStrategy implements ProvisioningStrategy {

    private final TenantSubscriptionRepository tenantSubscriptionRepository;

    public BundledProvisioningStrategy(TenantSubscriptionRepository tenantSubscriptionRepository) {
        this.tenantSubscriptionRepository = tenantSubscriptionRepository;
    }

    @Override
    @Transactional
    public void provision(ProvisioningContext context) {
        tenantSubscriptionRepository.findById(context.tenantSubscriptionId()).ifPresent(sub -> {
            sub.setProvisioningStatus(ProvisioningStatus.PENDING);
            tenantSubscriptionRepository.save(sub);
        });

        // TODO: dispatch an admin notification (email / Slack / ticket) to trigger manual GHL subaccount setup
        log.warn("[BUNDLED] Manual provisioning required — subscriptionId={}, userId={}, paddleSubId={}. "
                        + "Admin/Finance must set up the GHL subaccount.",
                context.tenantSubscriptionId(), context.userId(), context.paddleSubscriptionId());
    }

    @Override
    @Transactional
    public void deprovision(ProvisioningContext context) {
        log.warn("[BUNDLED] Manual deprovisioning required — subscriptionId={}, userId={}. "
                        + "Admin/Finance must deactivate the GHL subaccount.",
                context.tenantSubscriptionId(), context.userId());
    }

    @Override
    public ProductType supports() {
        return ProductType.BUNDLED;
    }
}
