package org.example.loficonnect.commons.dto.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class SubscriptionPlanRequest {

    @NotNull
    private Long currencyId;

    private Integer sortOrder;

    @NotBlank
    @Size(max = 100)
    private String name;

    private BigDecimal price;

    @NotNull
    private List<String> description;

    @NotBlank
    @Size(max = 20)
    private String billingCycle;

    @NotNull
    private Integer durationInDays;

    private List<SubscriptionPlanLimitRequest> limits;
}
