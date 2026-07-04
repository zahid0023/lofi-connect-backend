package org.example.loficonnect.usage.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.example.loficonnect.commons.model.entity.AuditableEntity;
import org.example.loficonnect.usage.model.enums.ApiPlatform;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "api_usage_logs")
public class ApiUsageLogEntity extends AuditableEntity {

    @Column(name = "app_key_id", nullable = false)
    private Long appKeyId;

    @Enumerated(EnumType.STRING)
    @Column(name = "platform", nullable = false, length = 50)
    private ApiPlatform platform;

    @Column(name = "request_id", length = 100)
    private String requestId;

    @Column(name = "http_method", nullable = false, length = 10)
    private String httpMethod;

    @Column(name = "endpoint", nullable = false, length = 500)
    private String endpoint;

    @Column(name = "endpoint_pattern", length = 500)
    private String endpointPattern;

    @Column(name = "ip_address", length = 45)
    private String ipAddress;

    @Column(name = "user_agent", length = 255)
    private String userAgent;

    @Column(name = "request_size_bytes")
    private Integer requestSizeBytes;

    @Column(name = "response_status")
    private Integer responseStatus;

    @Column(name = "response_size_bytes")
    private Integer responseSizeBytes;

    @Column(name = "response_time_ms")
    private Long responseTimeMs;

    @Column(name = "is_error", nullable = false)
    private boolean error;

    @Column(name = "error_code", length = 100)
    private String errorCode;

    @Column(name = "rate_limit_remaining")
    private Integer rateLimitRemaining;

    @Column(name = "requested_at", nullable = false)
    private Instant requestedAt;
}
