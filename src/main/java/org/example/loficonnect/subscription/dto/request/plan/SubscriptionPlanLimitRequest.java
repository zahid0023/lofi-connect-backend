package org.example.loficonnect.subscription.dto.request.plan;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class SubscriptionPlanLimitRequest {

    @NotNull
    private Long limitKeyId;

    @NotNull
    @Min(value = -1, message = "limitValue must be -1 (unlimited) or a positive number")
    private Long limitValue;
}
