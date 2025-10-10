package org.example.loficonnect.dto.request.triggerlinks;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class LinkCreateRequest {
    private String location_id;
    private String name;
    private String redirect_to;
}
