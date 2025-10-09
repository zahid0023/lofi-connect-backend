package org.example.loficonnect.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class SuccessResponse {
    private Boolean success;

    public SuccessResponse(Boolean success) {
        this.success = success;
    }
}
