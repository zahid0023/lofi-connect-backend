package org.example.loficonnect.subscription.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;
import org.example.loficonnect.subscription.model.enums.RefundRequestStatus;

import java.time.Instant;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class RefundRequestResponse {
    private Long id;
    private Long tenantSubscriptionId;
    private Long userId;
    private String reason;
    private RefundRequestStatus status;
    private String adminNotes;
    private Long reviewedBy;
    private Instant reviewedAt;
    private Instant createdAt;
}
