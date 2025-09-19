package org.example.loficonnect.dto.mapper.calendargroups;

import lombok.Data;
import org.example.loficonnect.dto.request.calendargroups.CalendarGroupValidateSlugRequest;

@Data
public class GoHighLevelCalendarGroupValidateSlugRequest {
    private String locationId;
    private String slug;

    public static GoHighLevelCalendarGroupValidateSlugRequest fromRequest(CalendarGroupValidateSlugRequest request) {
        GoHighLevelCalendarGroupValidateSlugRequest ghlRequest = new GoHighLevelCalendarGroupValidateSlugRequest();
        ghlRequest.setLocationId(request.getLocationId());
        ghlRequest.setSlug(request.getSlug());
        return ghlRequest;
    }
}
