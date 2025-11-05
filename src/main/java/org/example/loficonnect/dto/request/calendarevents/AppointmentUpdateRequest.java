package org.example.loficonnect.dto.request.calendarevents;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class) // converts camelCase to snake_case in JSON
public class AppointmentUpdateRequest {
    private String title;
    private String meetingLocationType;
    private String meetingLocationId;
    private Boolean overrideLocationConfig;
    private String appointmentStatus;
    private String assignedUserId;
    private String description;
    private String address;
    private Boolean ignoreDateRange;
    private Boolean toNotify;
    private Boolean ignoreFreeSlotValidation;
    private String rrule;
    private String calendarId;
    private String timeZone;
    private LocalDate startDate;
    private LocalTime startTime;
    private LocalDate endDate;
    private LocalTime endTime;
}