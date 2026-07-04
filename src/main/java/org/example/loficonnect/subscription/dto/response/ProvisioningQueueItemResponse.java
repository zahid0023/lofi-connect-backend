package org.example.loficonnect.subscription.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;
import org.example.loficonnect.payment.model.enums.ProvisioningStatus;
import org.example.loficonnect.subscription.model.enums.TenantSubscriptionStatus;

import java.time.Instant;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ProvisioningQueueItemResponse {
    private Long subscriptionId;
    private Long userId;
    private Long planId;
    private String planName;
    private String planCode;
    private TenantSubscriptionStatus subscriptionStatus;
    private ProvisioningStatus provisioningStatus;
    private Instant startDate;
    private Instant createdAt;
}
