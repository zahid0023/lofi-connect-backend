package org.example.loficonnect.dto.mapper.task;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import org.example.loficonnect.dto.request.task.TaskCreateRequest;

import java.time.ZonedDateTime;
import java.time.ZoneId;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class GoHighLevelTaskCreateRequest {

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

    private GoHighLevelTaskCreateRequest() {
    }

    public static GoHighLevelTaskCreateRequest fromRequest(TaskCreateRequest request, ObjectMapper objectMapper) {
        GoHighLevelTaskCreateRequest ghl = objectMapper.convertValue(request, GoHighLevelTaskCreateRequest.class);

        if (request.getDueDate() != null && request.getDueTime() != null) {
            // You can replace "Asia/Dhaka" with the actual time zone you want
            ghl.setDueDate(ZonedDateTime.of(request.getDueDate(), request.getDueTime(), ZoneId.of(request.getTimeZone())));
        }
        return ghl;
    }
}
