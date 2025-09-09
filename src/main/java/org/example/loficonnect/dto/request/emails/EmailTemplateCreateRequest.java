package org.example.loficonnect.dto.request.emails;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class EmailTemplateCreateRequest {
    private String locationId;
    private String title;
    private String type;
    private String updatedBy;
    private String builderVersion;
    private String name;
    private String parentId;
    private String templateDataUrl;
    private String importProvider;
    private String importURL;
    private String templateSource;
    private Boolean isPlainText;
}
