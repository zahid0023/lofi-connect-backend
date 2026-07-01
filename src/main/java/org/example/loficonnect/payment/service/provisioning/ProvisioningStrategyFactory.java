package org.example.loficonnect.payment.service.provisioning;

import org.example.loficonnect.payment.model.enums.ProductType;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Resolves the correct {@link ProvisioningStrategy} for a given {@link ProductType}.
 * All strategies registered as Spring beans are auto-discovered.
 */
@Component
public class ProvisioningStrategyFactory {

    private final Map<ProductType, ProvisioningStrategy> strategies;

    public ProvisioningStrategyFactory(List<ProvisioningStrategy> strategyList) {
        this.strategies = strategyList.stream()
                .collect(Collectors.toMap(ProvisioningStrategy::supports, Function.identity()));
    }

    public ProvisioningStrategy get(ProductType productType) {
        ProvisioningStrategy strategy = strategies.get(productType);
        if (strategy == null) {
            throw new IllegalArgumentException("No ProvisioningStrategy registered for productType: " + productType);
        }
        return strategy;
    }
}
