package org.example.loficonnect.dto.mapper.calendar;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.example.loficonnect.dto.request.calendar.CalendarCreateRequest;

@Data
@EqualsAndHashCode(callSuper = true)
public class GoHighLevelCalendarCreateRequest extends GoHighLevelCalendarRequest {
    private Boolean isActive;
    private String locationId;
    private String calendarType;

    public static GoHighLevelCalendarCreateRequest fromRequest(final CalendarCreateRequest request) {
        if (request == null) return null;

        GoHighLevelCalendarCreateRequest ghlRequest = new GoHighLevelCalendarCreateRequest();

        // Populate common fields from base class mapper
        GoHighLevelCalendarRequest.fromRequest(request).copyTo(ghlRequest);

        // Populate Create-specific fields
        ghlRequest.setIsActive(request.getIsActive());
        ghlRequest.setLocationId(request.getLocationId());
        ghlRequest.setCalendarType(request.getCalendarType());

        return ghlRequest;
    }

}
