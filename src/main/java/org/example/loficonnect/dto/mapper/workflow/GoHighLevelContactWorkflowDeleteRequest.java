package org.example.loficonnect.dto.mapper.workflow;

import lombok.Data;
import org.example.loficonnect.dto.request.workflow.ContactWorkflowDeleteRequest;
import org.example.loficonnect.util.DateTimeUtil;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;

@Data
public class GoHighLevelContactWorkflowDeleteRequest {
    private ZonedDateTime eventStartTime;

    public static GoHighLevelContactWorkflowDeleteRequest fromRequest(ContactWorkflowDeleteRequest request) {
        GoHighLevelContactWorkflowDeleteRequest ghlRequest = new GoHighLevelContactWorkflowDeleteRequest();
        ghlRequest.setEventStartTime(
                DateTimeUtil.toZonedDateTime(LocalDateTime.of(request.getEventDate(), request.getEventTime()), request.getTimeZone())
        );
        return ghlRequest;
    }
}
