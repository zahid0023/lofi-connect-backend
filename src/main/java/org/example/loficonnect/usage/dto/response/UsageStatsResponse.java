package org.example.loficonnect.usage.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class UsageStatsResponse {
    private StatCard totalCalls;
    private StatCard successRate;
    private StatCard errors;
    private StatCard avgCallsPerDay;
}
