package org.example.loficonnect.dto.mapper.workflow;

import lombok.Data;
import org.example.loficonnect.dto.request.workflow.ContactWorkflowAddRequest;
import org.example.loficonnect.util.DateTimeUtil;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

@Data
public class GoHighLevelContactWorkflowAddRequest {
    private String eventStartTime; // ISO string representation of ZonedDateTime

    public static GoHighLevelContactWorkflowAddRequest fromRequest(ContactWorkflowAddRequest request) {
        GoHighLevelContactWorkflowAddRequest ghlRequest = new GoHighLevelContactWorkflowAddRequest();

        ZonedDateTime zdt = DateTimeUtil.toZonedDateTime(
                LocalDateTime.of(request.getDate(), request.getTime()),
                request.getTimeZone()
        );

        //Format without zone name (just offset)
        ghlRequest.setEventStartTime(zdt.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME));
        return ghlRequest;
    }

}
