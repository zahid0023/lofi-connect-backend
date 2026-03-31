package org.example.loficonnect.commons.dto.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class TenantSubscriptionCreateRequest extends TenantSubscriptionRequest {

}
