package org.example.loficonnect.dto.mapper.calendarevents;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import org.example.loficonnect.dto.request.calendarevents.AppointmentCreateRequest;
import org.example.loficonnect.util.DateTimeUtil;

import java.time.ZonedDateTime;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class GoHighLevelAppointmentCreateRequest {

    private String title;

    @JsonAlias("meeting_location_type")
    private String meetingLocationType;

    @JsonAlias("meeting_location_id")
    private String meetingLocationId;

    @JsonAlias("override_location_config")
    private Boolean overrideLocationConfig;

    @JsonAlias("appointment_status")
    private String appointmentStatus;

    @JsonAlias("assigned_user_id")
    private String assignedUserId;

    private String address;

    @JsonAlias("ignore_date_range")
    private Boolean ignoreDateRange;

    @JsonAlias("to_notify")
    private Boolean toNotify;

    @JsonAlias("ignore_free_slot_validation")
    private Boolean ignoreFreeSlotValidation;

    private String rrule;

    @JsonAlias("calendar_id")
    private String calendarId;

    @JsonAlias("location_id")
    private String locationId;

    @JsonAlias("contact_id")
    private String contactId;

    @JsonAlias("start_time")
    private ZonedDateTime startTime;

    @JsonAlias("end_time")
    private ZonedDateTime endTime;

    private GoHighLevelAppointmentCreateRequest() {
    }

    /**
     * Converts AppointmentCreateRequest -> GoHighLevelAppointmentCreateRequest using ObjectMapper
     */
    public static GoHighLevelAppointmentCreateRequest fromRequest(AppointmentCreateRequest request, ObjectMapper objectMapper) {
        GoHighLevelAppointmentCreateRequest ghl = objectMapper.convertValue(request, GoHighLevelAppointmentCreateRequest.class);
        ghl.setStartTime(DateTimeUtil.toZonedDateTime(request.getStartDate(), request.getStartTime(), request.getTimeZone()));
        ghl.setEndTime(DateTimeUtil.toZonedDateTime(request.getEndDate(), request.getEndTime(), request.getTimeZone()));
        return ghl;
    }
}