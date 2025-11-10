package org.example.loficonnect.dto.response.customfields;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public class CustomFieldResponse {
    @JsonAlias("id")
    private String id;

    @JsonAlias("fieldKey")
    private String key;

    private Boolean required = false;

    @JsonAlias("name")
    private String label;

    @JsonAlias("dataType")
    private String dataType;

    @JsonAlias("picklistOptions")
    private List<String> choices;

    private static final Map<String, String> DATA_TYPE_MAP = Map.of(
            "LARGE_TEXT", "text",   
            "TEXT", "string",
            "SINGLE_OPTIONS", "single_options",
            "DATE", "datetime",
            "MULTIPLE_OPTIONS", "multiple_options",
            "CHECKBOX", "checkbox"
    );

    private CustomFieldResponse() {
    }
                    
    public static CustomFieldResponse fromJson(JsonNode jsonNode, ObjectMapper mapper) {
        if (jsonNode == null || jsonNode.isNull()) return null;
        CustomFieldResponse customFieldResponse = mapper.convertValue(jsonNode, CustomFieldResponse.class);

        String mappedType = DATA_TYPE_MAP.getOrDefault(customFieldResponse.getDataType(), customFieldResponse.getDataType());
        customFieldResponse.setDataType(mappedType);

        return customFieldResponse;
    }
}
