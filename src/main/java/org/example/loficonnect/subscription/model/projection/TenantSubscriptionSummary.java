package org.example.loficonnect.subscription.model.projection;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import org.example.loficonnect.subscription.model.enums.TenantSubscriptionStatus;

import java.time.Instant;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public interface TenantSubscriptionSummary {
    Long getId();
    Long getUserId();
    Instant getStartDate();
    Instant getEndDate();
    TenantSubscriptionStatus getStatus();
}
