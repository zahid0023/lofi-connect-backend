package org.example.loficonnect.dto.mapper.task;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import org.example.loficonnect.dto.request.task.TaskCompletedRequest;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class GoHighLevelTaskCompletedRequest {

    @JsonAlias({"completed"})
    private Boolean completed;

    private GoHighLevelTaskCompletedRequest() {
    }

    public static GoHighLevelTaskCompletedRequest fromRequest(TaskCompletedRequest request, ObjectMapper objectMapper) {
        return objectMapper.convertValue(request, GoHighLevelTaskCompletedRequest.class);
    }
}