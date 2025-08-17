package org.example.loficonnect.dto.request.calendarevents;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class AppointmentCreateRequest {
    private String title;
    private String meeting_location_type;
    private String meeting_location_id;
    private Boolean override_location_config;
    private String appointment_status;
    private String assigned_user_id;
    private String address;
    private Boolean ignore_date_range;
    private Boolean to_notify;
    private Boolean ignore_free_slot_validation;
    private String rrule;
    private String calendar_id;
    private String location_id;
    private String contact_id;
    private String start_time;
    private String end_time;
}
