package org.example.loficonnect.commons.dto.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class SubscriptionModelRequest {
    private String name;
    private String description;
    private List<String> details;
    private Integer maxAppKeys;
    private Long apiCallsQuota;
    private Integer quotaPeriodDays;
    private BigDecimal price;
    private Long currencyId;
}
