package org.example.loficonnect.dto.mapper.calendarevents;

import lombok.Data;
import org.example.loficonnect.dto.request.calendarevents.AppointmentCreateRequest;

@Data
public class GoHighLevelAppointmentCreateRequest {
    private String title;
    private String meetingLocationType;
    private String meetingLocationId;
    private Boolean overrideLocationConfig;
    private String appointmentStatus;
    private String assignedUserId;
    private String address;
    private Boolean ignoreDateRange;
    private Boolean toNotify;
    private Boolean ignoreFreeSlotValidation;
    private String rrule;
    private String calendarId;
    private String locationId;
    private String contactId;
    private String startTime;
    private String endTime;

    public static GoHighLevelAppointmentCreateRequest fromRequest(AppointmentCreateRequest request) {
        GoHighLevelAppointmentCreateRequest ghlRequest = new GoHighLevelAppointmentCreateRequest();

        ghlRequest.setTitle(request.getTitle());
        ghlRequest.setMeetingLocationType(request.getMeeting_location_type());
        ghlRequest.setMeetingLocationId(request.getMeeting_location_id());
        ghlRequest.setOverrideLocationConfig(request.getOverride_location_config());
        ghlRequest.setAppointmentStatus(request.getAppointment_status());
        ghlRequest.setAssignedUserId(request.getAssigned_user_id());
        ghlRequest.setAddress(request.getAddress());
        ghlRequest.setIgnoreDateRange(request.getIgnore_date_range());
        ghlRequest.setToNotify(request.getTo_notify());
        ghlRequest.setIgnoreFreeSlotValidation(request.getIgnore_free_slot_validation());
        ghlRequest.setRrule(request.getRrule());
        ghlRequest.setCalendarId(request.getCalendar_id());
        ghlRequest.setLocationId(request.getLocation_id());
        ghlRequest.setContactId(request.getContact_id());
        ghlRequest.setStartTime(request.getStart_time());
        ghlRequest.setEndTime(request.getEnd_time());

        return ghlRequest;
    }

}
