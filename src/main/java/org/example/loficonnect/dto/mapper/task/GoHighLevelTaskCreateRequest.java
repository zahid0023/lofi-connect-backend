package org.example.loficonnect.dto.mapper.task;

import lombok.Data;
import org.example.loficonnect.dto.request.task.TaskCreateRequest;
import java.time.ZonedDateTime;
import java.time.ZoneId;

@Data
public class GoHighLevelTaskCreateRequest {
    private String title;
    private String body;
    private ZonedDateTime dueDate;
    private Boolean completed;
    private String assignedTo;

    public static GoHighLevelTaskCreateRequest fromRequest(TaskCreateRequest request, String timeZone) {
        GoHighLevelTaskCreateRequest ghl = new GoHighLevelTaskCreateRequest();
        ghl.setTitle(request.getTitle());
        ghl.setBody(request.getBody());
        ghl.setCompleted(request.getCompleted());
        ghl.setAssignedTo(request.getAssigned_to());

        if (request.getDue_date() != null && request.getDue_time() != null) {
            ghl.setDueDate(ZonedDateTime.of(request.getDue_date(), request.getDue_time(), ZoneId.of(timeZone)));
        }

        return ghl;
    }
}
