package org.example.loficonnect.commons.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;
import org.example.loficonnect.commons.model.dto.SubscriptionPlanDto;

import java.util.List;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class SubscriptionPlanListResponse {
    private List<SubscriptionPlanDto> subscriptionPlans;

    public SubscriptionPlanListResponse(List<SubscriptionPlanDto> subscriptionPlans) {
        this.subscriptionPlans = subscriptionPlans;
    }
}
