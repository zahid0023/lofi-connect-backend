package org.example.loficonnect.commons.model.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

@Getter
@Setter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class TenantSubscriptionDto {
    private Long id;
    private Long tenantId;
    private String tenantUsername;
    private Long subscriptionPlanId;
    private String planName;
    private String status;
    private OffsetDateTime startAt;
    private OffsetDateTime endAt;
    private Boolean autoRenew;
    private BigDecimal price;
    private Long currencyId;
    private List<TenantSubscriptionLimitDto> limits;
}
