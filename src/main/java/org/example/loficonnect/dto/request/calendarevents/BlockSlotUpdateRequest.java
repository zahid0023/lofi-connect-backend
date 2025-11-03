package org.example.loficonnect.dto.request.calendarevents;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class    BlockSlotUpdateRequest {
    private String title;
    private String calendarId;
    private String assignedUserId;
    private String locationId;
    @JsonProperty(required = true)
    private String timeZone;
    private LocalDate startDate;
    private LocalTime startTime;
    private LocalDate endDate;
    private LocalTime endTime;
}
