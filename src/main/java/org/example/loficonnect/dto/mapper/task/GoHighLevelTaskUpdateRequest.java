package org.example.loficonnect.dto.mapper.task;

import lombok.Data;
import org.example.loficonnect.dto.request.task.TaskUpdateRequest;
import java.time.ZonedDateTime;
import java.time.ZoneId;

@Data
public class GoHighLevelTaskUpdateRequest {
    private String title;
    private String body;
    private ZonedDateTime dueDate;
    private Boolean completed;
    private String assignedTo;

    public static GoHighLevelTaskUpdateRequest fromRequest(TaskUpdateRequest request, String timeZone) {
        GoHighLevelTaskUpdateRequest ghl = new GoHighLevelTaskUpdateRequest();
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
