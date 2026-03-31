package org.example.loficonnect.commons.model.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class SubscriptionPlanLimitDto {
    private Long id;
    private LimitKeyDto limitKey;
    private Long limitValue;
}
