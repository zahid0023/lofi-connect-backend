package org.example.loficonnect.dto.mapper.calendargroups;

import lombok.Data;
import org.example.loficonnect.dto.request.calendargroups.CalendarGroupUpdateRequest;

@Data
public class GoHighLevelCalendarGroupUpdateRequest {
    private String name;
    private String description;
    private String slug;

    public static GoHighLevelCalendarGroupUpdateRequest fromRequest(CalendarGroupUpdateRequest request) {
        GoHighLevelCalendarGroupUpdateRequest ghlRequest = new GoHighLevelCalendarGroupUpdateRequest();
        ghlRequest.setName(request.getName());
        ghlRequest.setDescription(request.getDescription());
        ghlRequest.setSlug(request.getSlug());
        return ghlRequest;
    }
}
