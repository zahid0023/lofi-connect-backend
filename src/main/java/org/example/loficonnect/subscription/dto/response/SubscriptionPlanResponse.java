package org.example.loficonnect.subscription.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;
import org.example.loficonnect.subscription.model.dto.SubscriptionPlanDto;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class SubscriptionPlanResponse {
    private final SubscriptionPlanDto subscriptionPlan;

    public SubscriptionPlanResponse(SubscriptionPlanDto subscriptionPlan) {
        this.subscriptionPlan = subscriptionPlan;
    }
}
