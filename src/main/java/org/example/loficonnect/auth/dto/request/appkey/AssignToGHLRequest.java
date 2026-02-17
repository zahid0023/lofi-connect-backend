package org.example.loficonnect.auth.dto.request.appkey;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class AssignToGHLRequest {
    private Long appKeyId;
    private Boolean isAgency;
    private Long ghlId;
    private String code;
}
