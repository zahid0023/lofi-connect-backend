package org.example.loficonnect.dto.request.calendar;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class CalendarCreateRequest extends CalendarRequest {
    private Boolean isActive;
    private String locationId;
    private String calendarType;
}
