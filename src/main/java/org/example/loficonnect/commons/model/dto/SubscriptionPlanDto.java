package org.example.loficonnect.commons.model.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class SubscriptionPlanDto {
    private Long id;
    private String name;
    private BigDecimal price;
    private List<String> description;
    private CurrencyDto currency;
    private List<SubscriptionPlanLimitDto> limits;
}
