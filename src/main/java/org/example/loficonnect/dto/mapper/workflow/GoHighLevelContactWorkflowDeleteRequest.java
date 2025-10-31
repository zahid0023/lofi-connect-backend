package org.example.loficonnect.dto.mapper.workflow;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
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

    public static GoHighLevelContactWorkflowDeleteRequest fromRequest(ContactWorkflowDeleteRequest request, ObjectMapper objectMapper) {
        GoHighLevelContactWorkflowDeleteRequest ghlRequest = objectMapper.convertValue(request, GoHighLevelContactWorkflowDeleteRequest.class);
        ghlRequest.setEventStartTime(DateTimeUtil.toZonedDateTime(request.getDate(), request.getTime(), request.getTimeZone()));
        return ghlRequest;
    }
}
