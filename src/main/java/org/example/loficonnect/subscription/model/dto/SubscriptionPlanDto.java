package org.example.loficonnect.subscription.model.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.loficonnect.subscription.model.enums.BillingCycle;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class SubscriptionPlanDto {
    private Long id;
    private Long currencyId;
    private String code;
    private BillingCycle billingCycle;
    private Integer trialPeriodDays;
    private Integer sortOrder;
    private String name;
    private BigDecimal price;
    private String[] description;
    private Boolean isPublic;
    private List<SubscriptionPlanLimitDto> limits;
}
