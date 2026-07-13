package org.example.loficonnect.usage.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ConnectionUsageItem {
    private Long appKeyId;
    private String appKeyName;
    private boolean connected;
    /** Null when the app key has no active GHL connection. */
    private ConnectionInfo connection;
    private long totalCalls;
    private long errors;
    private double errorRate;
    /** Null when there are no recorded calls for this key in the period. */
    private Double avgResponseTimeMs;
}
