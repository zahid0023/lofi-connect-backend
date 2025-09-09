package org.example.loficonnect.dto.request.emails;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class EmailTemplateUpdateRequest {

    private String locationId;
    private String templateId;
    private String updatedBy;
    private Dnd dnd;
    private String html;
    private String editorType;
    private String previewText;
    private Boolean isPlainText;

    @Data
    public static class Dnd {
        private List<String> elements;
        private Map<String, Object> attrs;
        private Map<String, Object> templateSettings;
    }
}
