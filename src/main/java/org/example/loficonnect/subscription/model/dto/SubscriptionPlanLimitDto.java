package org.example.loficonnect.subscription.model.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.loficonnect.subscription.model.enums.LimitKeyUnit;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class SubscriptionPlanLimitDto {
    private Long id;
    private Long limitKeyId;
    private String limitKeyCode;
    private String limitKeyName;
    private LimitKeyUnit limitKeyUnit;
    private Long limitValue;
}
