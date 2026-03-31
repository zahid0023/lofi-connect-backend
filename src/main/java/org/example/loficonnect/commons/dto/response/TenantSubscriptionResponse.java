package org.example.loficonnect.commons.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;
import org.example.loficonnect.commons.model.dto.TenantSubscriptionDto;

@Getter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class TenantSubscriptionResponse {
    private final TenantSubscriptionDto data;

    public TenantSubscriptionResponse(TenantSubscriptionDto data) {
        this.data = data;
    }
}
