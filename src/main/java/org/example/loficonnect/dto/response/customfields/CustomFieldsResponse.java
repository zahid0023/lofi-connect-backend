package org.example.loficonnect.dto.response.customfields;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CustomFieldsResponse {
    @JsonAlias("customFields")
    private List<CustomFieldResponse> customFields;

    private CustomFieldsResponse() {
    }

    public static CustomFieldsResponse fromJson(JsonNode jsonNode, ObjectMapper mapper) {
        CustomFieldsResponse customFieldsResponse = new CustomFieldsResponse();
        if (jsonNode == null || jsonNode.isNull()) return null;
        List<CustomFieldResponse> customFields = new ArrayList<>();
        if (jsonNode.isArray()) {
            for (JsonNode customFieldNode : jsonNode) {
                customFields.add(CustomFieldResponse.fromJson(customFieldNode, mapper));
            }
        }
        customFieldsResponse.setCustomFields(customFields);
        return customFieldsResponse;
    }
}
