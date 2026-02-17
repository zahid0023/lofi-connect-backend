package org.example.loficonnect.auth.model.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;
import org.example.loficonnect.model.dto.GoHighLevelTokenDTO;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class LofiConnectAppKeyDTO {
    private Long id;
    private String appKey;
    private String name;
    private String maskedKey;
    private String status;
    private String createdAt;
    private String updatedAt;
    private GoHighLevelTokenDTO ghlConnection;
}
