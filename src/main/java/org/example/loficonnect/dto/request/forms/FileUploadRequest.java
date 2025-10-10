package org.example.loficonnect.dto.request.forms;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.util.Map;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class FileUploadRequest {
    private String contactId;
    private String locationId;
    private Map<String, Object> files;
}
