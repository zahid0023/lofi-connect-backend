package org.example.loficonnect.dto.mapper.tags;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import org.example.loficonnect.dto.request.tags.TagUpdateRequest;

@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class GoHighLevelTagUpdateRequest {

    @JsonAlias("name")
    private String name;

    private GoHighLevelTagUpdateRequest() {
        // private constructor
    }

    public static GoHighLevelTagUpdateRequest fromRequest(TagUpdateRequest request, ObjectMapper objectMapper) {
        return objectMapper.convertValue(request, GoHighLevelTagUpdateRequest.class);
    }
}
