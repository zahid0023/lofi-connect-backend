package org.example.loficonnect.dto.mapper.tags;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import org.example.loficonnect.dto.request.tags.TagCreateRequest;

@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class GoHighLevelTagCreateRequest {

    @JsonAlias("name")
    private String name;

    private GoHighLevelTagCreateRequest() {
        // private constructor
    }

    public static GoHighLevelTagCreateRequest fromRequest(TagCreateRequest request, ObjectMapper objectMapper) {
        return objectMapper.convertValue(request, GoHighLevelTagCreateRequest.class);
    }
}