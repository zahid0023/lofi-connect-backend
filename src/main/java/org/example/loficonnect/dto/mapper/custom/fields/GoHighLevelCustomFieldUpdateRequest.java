package org.example.loficonnect.dto.mapper.custom.fields;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import org.example.loficonnect.dto.request.custom.fields.CustomFieldUpdateRequest;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class GoHighLevelCustomFieldUpdateRequest {

    @JsonAlias("name")
    private String name;

    @JsonAlias("placeholder")
    private String placeholder;

    @JsonAlias("accepted_format")
    private List<String> acceptedFormat;

    @JsonAlias("is_multiple_file")
    private Boolean isMultipleFile;

    @JsonAlias("max_number_of_files")
    private Integer maxNumberOfFiles;

    @JsonAlias("text_box_list_options")
    private List<TextBoxListOption> textBoxListOptions;

    @JsonAlias("position")
    private Integer position;

    @JsonAlias("model")
    private String model;

    @Data
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class TextBoxListOption {
        @JsonAlias("label")
        private String label;

        @JsonAlias("prefill_value")
        private String prefillValue;
    }

    private GoHighLevelCustomFieldUpdateRequest() {
        // private constructor
    }

    public static GoHighLevelCustomFieldUpdateRequest fromRequest(CustomFieldUpdateRequest request, ObjectMapper objectMapper) {
        return objectMapper.convertValue(request, GoHighLevelCustomFieldUpdateRequest.class);
    }
}