package org.example.loficonnect.dto.mapper.tags;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import org.example.loficonnect.dto.request.tags.ContactTagsRemoveRequest;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class GoHighLevelContactTagDeleteRequest {

    @JsonAlias({"tags"})
    private List<String> tags;

    private GoHighLevelContactTagDeleteRequest() {
    }

    public static GoHighLevelContactTagDeleteRequest fromRequest(ContactTagsRemoveRequest request, ObjectMapper objectMapper) {
        return objectMapper.convertValue(request, GoHighLevelContactTagDeleteRequest.class);
    }
}
