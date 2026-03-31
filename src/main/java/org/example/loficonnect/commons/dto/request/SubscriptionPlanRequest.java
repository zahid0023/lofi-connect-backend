package org.example.loficonnect.commons.dto.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class SubscriptionPlanRequest {
    private Long currencyId;
    private String name;
    private BigDecimal price;
    private List<String> description;
    private List<SubscriptionPlanLimitRequest> limits;
}
