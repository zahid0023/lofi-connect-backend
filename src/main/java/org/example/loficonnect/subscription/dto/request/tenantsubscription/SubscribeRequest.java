package org.example.loficonnect.subscription.dto.request.tenantsubscription;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class SubscribeRequest {

    @NotNull(message = "plan_id is required")
    private Long planId;
}
