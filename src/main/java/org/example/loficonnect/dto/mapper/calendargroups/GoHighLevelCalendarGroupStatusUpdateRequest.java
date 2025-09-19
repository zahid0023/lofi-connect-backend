package org.example.loficonnect.dto.mapper.calendargroups;

import lombok.Data;
import org.example.loficonnect.dto.request.calendargroups.CalendarGroupStatusUpdateRequest;

@Data
public class GoHighLevelCalendarGroupStatusUpdateRequest {
    private Boolean isActive;

    public static GoHighLevelCalendarGroupStatusUpdateRequest fromRequest(CalendarGroupStatusUpdateRequest request) {
        GoHighLevelCalendarGroupStatusUpdateRequest ghlRequest = new GoHighLevelCalendarGroupStatusUpdateRequest();
        ghlRequest.setIsActive(request.getIsActive());
        return ghlRequest;
    }
}
