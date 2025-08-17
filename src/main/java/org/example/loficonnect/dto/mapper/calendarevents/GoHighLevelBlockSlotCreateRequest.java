package org.example.loficonnect.dto.mapper.calendarevents;

import lombok.Data;
import org.example.loficonnect.dto.request.calendarevents.BlockSlotCreateRequest;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@Data
public class GoHighLevelBlockSlotCreateRequest {
    private String title;
    private String calendarId;
    private String assignedUserId;
    private String locationId;
    private ZonedDateTime startTime;
    private ZonedDateTime endTime;

    public static GoHighLevelBlockSlotCreateRequest fromRequest(BlockSlotCreateRequest request) {
        GoHighLevelBlockSlotCreateRequest ghlRequest = new GoHighLevelBlockSlotCreateRequest();
        ghlRequest.setTitle(request.getTitle());
        ghlRequest.setCalendarId(request.getCalendarId());
        ghlRequest.setAssignedUserId(request.getAssignedUserId());
        ghlRequest.setLocationId(request.getLocationId());
        if (request.getStartDate() != null && request.getStartTime() != null) {
            ghlRequest.setStartTime(ZonedDateTime.of(LocalDateTime.of(request.getStartDate(), request.getStartTime()), ZoneId.of(request.getTimeZone())));
        }
        if (request.getEndDate() != null && request.getEndTime() != null) {
            ghlRequest.setEndTime(ZonedDateTime.of(LocalDateTime.of(request.getEndDate(), request.getEndTime()), ZoneId.of(request.getTimeZone())));
        }
        return ghlRequest;
    }
}
