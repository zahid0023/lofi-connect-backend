package org.example.loficonnect.dto.request.calendargroups;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CalendarGroupCreateRequest {
    private String locationId;
    private String name;
    private String description;
    private String slug;
    private Boolean isActive;
}
