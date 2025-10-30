package org.example.loficonnect.dto.mapper.notes;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import org.example.loficonnect.dto.request.notes.ContactNoteUpdateRequest;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class GoHighLevelContactNoteUpdateRequest {

    @JsonAlias({"user_id"})
    private String userId;

    @JsonAlias({"body"})
    private String body;

    private GoHighLevelContactNoteUpdateRequest() {
    }

    public static GoHighLevelContactNoteUpdateRequest fromRequest(ContactNoteUpdateRequest request, ObjectMapper objectMapper) {
        return objectMapper.convertValue(request, GoHighLevelContactNoteUpdateRequest.class);
    }
}