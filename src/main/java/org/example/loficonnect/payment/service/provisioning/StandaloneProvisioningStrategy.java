package org.example.loficonnect.payment.service.provisioning;

import lombok.extern.slf4j.Slf4j;
import org.example.loficonnect.payment.model.enums.ProductType;
import org.example.loficonnect.payment.model.enums.ProvisioningStatus;
import org.example.loficonnect.subscription.repository.TenantSubscriptionRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Automated provisioning for STANDALONE plans.
 * Access is granted immediately after a valid Paddle webhook confirms the subscription.
 */
@Slf4j
@Component
public class StandaloneProvisioningStrategy implements ProvisioningStrategy {

    private final TenantSubscriptionRepository tenantSubscriptionRepository;

    public StandaloneProvisioningStrategy(TenantSubscriptionRepository tenantSubscriptionRepository) {
        this.tenantSubscriptionRepository = tenantSubscriptionRepository;
    }

    @Override
    @Transactional
    public void provision(ProvisioningContext context) {
        tenantSubscriptionRepository.findById(context.tenantSubscriptionId()).ifPresent(sub -> {
            sub.setProvisioningStatus(ProvisioningStatus.PROVISIONED);
            tenantSubscriptionRepository.save(sub);
        });
        log.info("[STANDALONE] Provisioned subscription id={} for userId={}",
                context.tenantSubscriptionId(), context.userId());
    }

    @Override
    @Transactional
    public void deprovision(ProvisioningContext context) {
        log.info("[STANDALONE] Deprovisioned subscription id={} for userId={}",
                context.tenantSubscriptionId(), context.userId());
        // Feature-flag revocation / access cleanup goes here
    }

    @Override
    public ProductType supports() {
        return ProductType.STANDALONE;
    }
}
