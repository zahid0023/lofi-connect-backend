package org.example.loficonnect.dto.request.custom.fields;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;
import java.util.List;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CustomFieldUpdateRequest {
    private String name;
    private String placeholder;
    private List<String> acceptedFormat;
    private Boolean isMultipleFile;
    private Integer maxNumberOfFiles;
    private List<TextBoxListOptionRequest> textBoxListOptions;
    private Integer position;
    private String model;

    @Data
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class TextBoxListOptionRequest {
        private String label;
        private String prefillValue;
    }
}
