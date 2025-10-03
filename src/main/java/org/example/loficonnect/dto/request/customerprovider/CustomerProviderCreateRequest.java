package org.example.loficonnect.dto.request.customerprovider;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CustomerProviderCreateRequest {
    private String name;
    private String description;
    private String paymentsUrl;
    private String queryUrl;
    private String imageUrl;
}
