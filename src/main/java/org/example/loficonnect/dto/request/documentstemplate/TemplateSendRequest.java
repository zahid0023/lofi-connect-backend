package org.example.loficonnect.dto.request.documentstemplate;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class TemplateSendRequest {
    private String templateId;
    private String userId;
    private Boolean sendDocument;
    private String locationId;
    private String contactId;
    private String opportunityId;
}
