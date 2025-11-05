package org.example.loficonnect.dto.mapper.calendarevents;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import org.example.loficonnect.dto.request.calendarevents.BlockSlotCreateRequest;
import org.example.loficonnect.util.DateTimeUtil;

import java.time.ZonedDateTime;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class GoHighLevelBlockSlotCreateRequest {

    private String title;

    @JsonAlias("calendar_id")
    private String calendarId;

    @JsonAlias("assigned_user_id")
    private String assignedUserId;

    @JsonAlias("location_id")
    private String locationId;

    private ZonedDateTime startTime;
    private ZonedDateTime endTime;

    private GoHighLevelBlockSlotCreateRequest() {}

    /**
     * Converts BlockSlotCreateRequest -> GoHighLevelBlockSlotCreateRequest using ObjectMapper
     */
    public static GoHighLevelBlockSlotCreateRequest fromRequest(BlockSlotCreateRequest request, ObjectMapper objectMapper) {
        GoHighLevelBlockSlotCreateRequest ghl = objectMapper.convertValue(request, GoHighLevelBlockSlotCreateRequest.class);

        // convert LocalDate + LocalTime + timeZone to ZonedDateTime
        if (request.getStartDate() != null && request.getStartTime() != null) {
            ghl.setStartTime(DateTimeUtil.toZonedDateTime(request.getStartDate(), request.getStartTime(), request.getTimeZone()));
        }
        if (request.getEndDate() != null && request.getEndTime() != null) {
            ghl.setEndTime(DateTimeUtil.toZonedDateTime(request.getEndDate(), request.getEndTime(), request.getTimeZone()));
        }

        return ghl;
    }
}