package org.example.loficonnect.dto.mapper.workflow;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import org.example.loficonnect.dto.request.workflow.ContactWorkflowAddRequest;
import org.example.loficonnect.util.DateTimeUtil;

import java.time.ZonedDateTime;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class GoHighLevelContactWorkflowAddRequest {

    private ZonedDateTime eventStartTime; // ISO string representation of ZonedDateTime

    private GoHighLevelContactWorkflowAddRequest() {
    }

    public static GoHighLevelContactWorkflowAddRequest fromRequest(ContactWorkflowAddRequest request, ObjectMapper objectMapper) {
        GoHighLevelContactWorkflowAddRequest ghl = objectMapper.convertValue(request, GoHighLevelContactWorkflowAddRequest.class);
        ghl.setEventStartTime(DateTimeUtil.toZonedDateTime(request.getDate(), request.getTime(), request.getTimeZone()));
        return ghl;
    }
}