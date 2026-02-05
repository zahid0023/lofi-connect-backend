package org.example.loficonnect.model.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class LofiConnectAppKeyDTO {
    private Long id;
    private String appKey;
    private String connectionName;
    private String subAccountName;
    private String scopes;
    private String subAccountId;
}
