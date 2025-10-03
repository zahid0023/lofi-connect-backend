package org.example.loficonnect.dto.request.redirect;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class RedirectUpdateRequest {
    private String target;
    private String action;
    private String location_id;
}
