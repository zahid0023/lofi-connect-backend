package org.example.loficonnect.dto.mapper.workflow;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.example.loficonnect.dto.request.workflow.ContactWorkflowDeleteRequest;
import org.example.loficonnect.util.DateTimeUtil;

import java.time.ZonedDateTime;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class GoHighLevelContactWorkflowDeleteRequest {

    private ZonedDateTime eventStartTime;

    private GoHighLevelContactWorkflowDeleteRequest() {
    }

    public static GoHighLevelContactWorkflowDeleteRequest fromRequest(ContactWorkflowDeleteRequest request) {
        GoHighLevelContactWorkflowDeleteRequest ghlRequest = new GoHighLevelContactWorkflowDeleteRequest();
        ghlRequest.setEventStartTime(DateTimeUtil.toZonedDateTime(request.getEventDate(), request.getEventTime(), request.getTimeZone()));
        return ghlRequest;
    }
}
