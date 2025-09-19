package org.example.loficonnect.dto.request.calendargroups;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CalendarGroupValidateSlugRequest {
    private String locationId;
    private String slug;
}
