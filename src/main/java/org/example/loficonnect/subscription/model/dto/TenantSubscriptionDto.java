package org.example.loficonnect.subscription.model.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.loficonnect.subscription.model.enums.BillingCycle;
import org.example.loficonnect.subscription.model.enums.TenantSubscriptionStatus;

import java.math.BigDecimal;
import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class TenantSubscriptionDto {
    private Long id;
    private Long userId;
    private Long planId;
    private String planCode;
    private String planName;
    private BillingCycle billingCycle;
    private BigDecimal price;
    private TenantSubscriptionStatus status;
    private Instant startDate;
    private Instant endDate;
    private Instant trialEndsAt;
}
