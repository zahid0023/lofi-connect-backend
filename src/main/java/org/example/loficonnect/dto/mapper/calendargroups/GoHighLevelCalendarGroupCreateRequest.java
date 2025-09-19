package org.example.loficonnect.dto.mapper.calendargroups;

import lombok.Data;
import org.example.loficonnect.dto.request.calendargroups.CalendarGroupCreateRequest;

@Data
public class GoHighLevelCalendarGroupCreateRequest {
    private String locationId;
    private String name;
    private String description;
    private String slug;
    private Boolean isActive;

    public static GoHighLevelCalendarGroupCreateRequest fromRequest(CalendarGroupCreateRequest request) {
        GoHighLevelCalendarGroupCreateRequest ghlRequest = new GoHighLevelCalendarGroupCreateRequest();
        ghlRequest.setLocationId(request.getLocationId());
        ghlRequest.setName(request.getName());
        ghlRequest.setDescription(request.getDescription());
        ghlRequest.setSlug(request.getSlug());
        ghlRequest.setIsActive(request.getIsActive());
        return ghlRequest;
    }
}
