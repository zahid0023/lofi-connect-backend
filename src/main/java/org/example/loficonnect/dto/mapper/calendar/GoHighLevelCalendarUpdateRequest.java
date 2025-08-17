package org.example.loficonnect.dto.mapper.calendar;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.example.loficonnect.dto.request.calendar.CalendarUpdateRequest;

@Data
@EqualsAndHashCode(callSuper = true)
public class GoHighLevelCalendarUpdateRequest extends GoHighLevelCalendarRequest {
    public static GoHighLevelCalendarUpdateRequest fromRequest(final CalendarUpdateRequest request) {
        if (request == null) return null;

        GoHighLevelCalendarUpdateRequest ghlRequest = new GoHighLevelCalendarUpdateRequest();

        // Copy all common fields from base class
        GoHighLevelCalendarRequest.fromRequest(request).copyTo(ghlRequest);

        return ghlRequest;
    }

}
