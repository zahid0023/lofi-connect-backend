package org.example.loficonnect.dto.mapper.task;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import org.example.loficonnect.dto.request.task.TaskCreateRequest;
import org.example.loficonnect.util.DateTimeUtil;

import java.time.ZonedDateTime;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class GoHighLevelTaskCreateRequest {

    @JsonAlias({"title"})
    private String title;

    @JsonAlias({"body"})
    private String body;

    private ZonedDateTime dueDate;

    @JsonAlias({"completed"})
    private Boolean completed;

    @JsonAlias({"assigned_to"})
    private String assignedTo;

    private GoHighLevelTaskCreateRequest() {
    }

    public static GoHighLevelTaskCreateRequest fromRequest(TaskCreateRequest request, ObjectMapper objectMapper) {
        GoHighLevelTaskCreateRequest ghl = objectMapper.convertValue(request, GoHighLevelTaskCreateRequest.class);

        if (request.getDueDate() != null && request.getDueTime() != null) {
            ghl.setDueDate(DateTimeUtil.toZonedDateTime(request.getDueDate(), request.getDueTime(), request.getTimeZone()));
        }
        return ghl;
    }
}
