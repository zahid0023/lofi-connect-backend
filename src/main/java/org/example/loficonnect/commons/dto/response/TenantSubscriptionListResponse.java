package org.example.loficonnect.commons.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;
import org.example.loficonnect.commons.model.dto.TenantSubscriptionDto;

import java.util.List;

@Getter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class TenantSubscriptionListResponse {
    private final List<TenantSubscriptionDto> tenantSubscriptions;

    public TenantSubscriptionListResponse(List<TenantSubscriptionDto> tenantSubscriptions) {
        this.tenantSubscriptions = tenantSubscriptions;
    }
}
