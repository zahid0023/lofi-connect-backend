package org.example.loficonnect.subscription.model.projection;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import org.example.loficonnect.subscription.model.enums.BillingCycle;

import java.math.BigDecimal;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public interface SubscriptionPlanSummary {
    Long getId();
    String getCode();
    String getName();
    BigDecimal getPrice();
    BillingCycle getBillingCycle();
    Integer getTrialPeriodDays();
    Integer getSortOrder();
    Boolean getIsPublic();
}
