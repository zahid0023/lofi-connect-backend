package org.example.loficonnect.dto.request.calendargroups;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CalendarGroupUpdateRequest {
    private String name;
    private String description;
    private String slug;
}
