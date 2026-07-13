package org.example.loficonnect.usage.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Data
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ApiUsageStatsResponse {
    private long totalRequests;
    private long errorRequests;
    private double errorRate;
    private Double avgResponseTimeMs;
    private Long minResponseTimeMs;
    private Long maxResponseTimeMs;
    private Instant from;
    private Instant to;
}
