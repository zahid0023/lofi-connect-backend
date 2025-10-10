package org.example.loficonnect.dto.request.schedule;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ScheduleCancelRequest {

    private String altId;
    private String altType;
}
