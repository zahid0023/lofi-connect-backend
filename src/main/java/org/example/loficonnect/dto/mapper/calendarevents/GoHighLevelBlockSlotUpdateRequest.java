package org.example.loficonnect.dto.mapper.calendarevents;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import org.example.loficonnect.dto.request.calendarevents.BlockSlotUpdateRequest;
import org.example.loficonnect.util.DateTimeUtil;

import java.time.ZonedDateTime;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class GoHighLevelBlockSlotUpdateRequest {

    private String title;

    @JsonAlias("calendar_id")
    private String calendarId;

    @JsonAlias("assigned_user_id")
    private String assignedUserId;

    @JsonAlias("location_id")
    private String locationId;

    @JsonAlias("start_time")
    private ZonedDateTime startTime;

    @JsonAlias("end_time")
    private ZonedDateTime endTime;

    private GoHighLevelBlockSlotUpdateRequest() {}

    /**
     * Converts BlockSlotUpdateRequest -> GoHighLevelBlockSlotUpdateRequest using ObjectMapper
     */
    public static GoHighLevelBlockSlotUpdateRequest fromRequest(BlockSlotUpdateRequest request, ObjectMapper objectMapper) {
        GoHighLevelBlockSlotUpdateRequest ghl = objectMapper.convertValue(request, GoHighLevelBlockSlotUpdateRequest.class);

        if (request.getStartDate() != null && request.getStartTime() != null && request.getTimeZone() != null) {
            ghl.setStartTime(DateTimeUtil.toZonedDateTime(request.getStartDate(), request.getStartTime(), request.getTimeZone()));
        }
        if (request.getEndDate() != null && request.getEndTime() != null && request.getTimeZone() != null) {
            ghl.setEndTime(DateTimeUtil.toZonedDateTime(request.getEndDate(), request.getEndTime(), request.getTimeZone()));
        }

        return ghl;
    }
}