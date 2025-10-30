package org.example.loficonnect.dto.mapper.task;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import org.example.loficonnect.dto.request.task.TaskUpdateRequest;

import java.time.ZonedDateTime;
import java.time.ZoneId;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class GoHighLevelTaskUpdateRequest {

    @JsonAlias({"task_title"})
    private String title;

    @JsonAlias({"description"})
    private String body;

    @JsonAlias({"due_date"})
    private ZonedDateTime dueDate;

    @JsonAlias({"is_completed"})
    private Boolean completed;

    @JsonAlias({"assigned_to"})
    private String assignedTo;

    private GoHighLevelTaskUpdateRequest() {
    }

    public static GoHighLevelTaskUpdateRequest fromRequest(TaskUpdateRequest request, ObjectMapper objectMapper) {
        GoHighLevelTaskUpdateRequest ghl = objectMapper.convertValue(request, GoHighLevelTaskUpdateRequest.class);

        if (request.getDueDate() != null && request.getDueTime() != null) {
            ghl.setDueDate(ZonedDateTime.of(request.getDueDate(), request.getDueTime(), ZoneId.of(request.getTimeZone())));
        }

        return ghl;
    }
}