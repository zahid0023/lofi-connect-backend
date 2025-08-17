package org.example.loficonnect.dto.mapper.calendarevents;

import lombok.Data;
import org.example.loficonnect.dto.request.calendarevents.BlockSlotUpdateRequest;
import java.time.ZonedDateTime;
import java.time.ZoneId;

@Data
public class GoHighLevelBlockSlotUpdateRequest {
    private String title;
    private String calendarId;
    private String assignedUserId;
    private String locationId;
    private ZonedDateTime startTime;
    private ZonedDateTime endTime;

    public static GoHighLevelBlockSlotUpdateRequest fromRequest(BlockSlotUpdateRequest request) {
        GoHighLevelBlockSlotUpdateRequest ghlRequest = new GoHighLevelBlockSlotUpdateRequest();

        ghlRequest.setTitle(request.getTitle());
        ghlRequest.setCalendarId(request.getCalendarId());
        ghlRequest.setAssignedUserId(request.getAssignedUserId());
        ghlRequest.setLocationId(request.getLocationId());

        if (request.getStartDate() != null && request.getStartTime() != null && request.getTimeZone() != null) {
            ghlRequest.setStartTime(ZonedDateTime.of(request.getStartDate(), request.getStartTime(), ZoneId.of(request.getTimeZone())));
        }

        if (request.getEndDate() != null && request.getEndTime() != null && request.getTimeZone() != null) {
            ghlRequest.setEndTime(ZonedDateTime.of(request.getEndDate(), request.getEndTime(), ZoneId.of(request.getTimeZone())));
        }

        return ghlRequest;
    }
}
