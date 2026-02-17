package org.example.loficonnect.model.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class GoHighLevelTokenDTO {
    private String companyId;
    private Boolean isAgency;
    private String subAccountId;
    private String subAccountName;
    private String scopes;
    private String userId;
}
