package org.example.loficonnect.subscription.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;
import org.example.loficonnect.subscription.model.dto.TenantSubscriptionDto;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class TenantSubscriptionResponse {
    private final TenantSubscriptionDto subscription;

    public TenantSubscriptionResponse(TenantSubscriptionDto subscription) {
        this.subscription = subscription;
    }
}
