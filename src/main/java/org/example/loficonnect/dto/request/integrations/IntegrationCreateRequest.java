package org.example.loficonnect.dto.request.integrations;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class IntegrationCreateRequest {
    private String altId;
    private String altType;
    private String uniqueName;
    private String title;
    private String provider;
    private String description;
    private String imageUrl;
}
