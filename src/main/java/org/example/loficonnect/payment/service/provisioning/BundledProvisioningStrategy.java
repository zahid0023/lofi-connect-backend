package org.example.loficonnect.payment.service.provisioning;

import lombok.extern.slf4j.Slf4j;
import org.example.loficonnect.payment.model.enums.ProductType;
import org.example.loficonnect.payment.model.enums.ProvisioningStatus;
import org.example.loficonnect.subscription.repository.TenantSubscriptionRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Auto-provisioning for BUNDLED plans.
 * Users get their API key quota immediately on subscription activation.
 * GHL account connection is done separately by the user via the GHL OAuth flow.
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
            sub.setProvisioningStatus(ProvisioningStatus.PROVISIONED);
            tenantSubscriptionRepository.save(sub);
        });

        log.info("[BUNDLED] Provisioned — subscriptionId={}, userId={}. "
                        + "User can now generate API keys up to their plan limit. "
                        + "GHL connection is done separately via OAuth.",
                context.tenantSubscriptionId(), context.userId());
    }

    @Override
    @Transactional
    public void deprovision(ProvisioningContext context) {
        log.info("[BUNDLED] Deprovisioned — subscriptionId={}, userId={}.",
                context.tenantSubscriptionId(), context.userId());
    }

    @Override
    public ProductType supports() {
        return ProductType.BUNDLED;
    }
}
