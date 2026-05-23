package org.example.loficonnect.commons.model.projection;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import java.math.BigDecimal;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public interface SubscriptionPlanSummary {
    Long getId();

    String getName();

    BigDecimal getPrice();

    String getBillingCycle();

    Integer getDurationInDays();
}
