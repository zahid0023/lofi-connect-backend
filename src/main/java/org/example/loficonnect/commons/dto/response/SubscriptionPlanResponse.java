package org.example.loficonnect.commons.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;
import org.example.loficonnect.commons.model.dto.SubscriptionPlanDto;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class SubscriptionPlanResponse {
    private SubscriptionPlanDto data;

    public SubscriptionPlanResponse(SubscriptionPlanDto plan) {
        this.data = plan;
    }
}
