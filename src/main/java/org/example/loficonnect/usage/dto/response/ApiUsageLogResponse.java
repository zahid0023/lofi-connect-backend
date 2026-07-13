package org.example.loficonnect.usage.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;
import org.example.loficonnect.usage.model.enums.ApiPlatform;

import java.time.Instant;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ApiUsageLogResponse {
    private Long id;
    private Long appKeyId;
    private ApiPlatform platform;
    private String requestId;
    private String httpMethod;
    private String endpoint;
    private String endpointPattern;
    private String ipAddress;
    private String userAgent;
    private Integer requestSizeBytes;
    private Integer responseStatus;
    private Integer responseSizeBytes;
    private Long responseTimeMs;
    private boolean error;
    private String errorCode;
    private Integer rateLimitRemaining;
    private Instant requestedAt;
}
