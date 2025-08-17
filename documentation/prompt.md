I am trying to create API Gateway for GoHighLevel. The client would not directly hit the backend of the GoHighLevel.
They will hit our API Gateway with an APP Key generated in our system. Then our system validates the APP Key, get the
stored access key from the DB and hits the GoHighLevel backend.

Now I will give you a curl command, and you will give me DTOs(for POST, PUT).

There will be two DTOs called "something-Create/Update-Request" and annotated snakeCaseNaming strategy but declared as
camelcase and another will be
"GoHighLevel-something-Create/Update-Request" and in same naming strategy as in the curl.

something-Create/Update-Request is the one which will be sent payload
when the client hits our backend. Then this request will be mapped to GoHighLevel-something-Create/Update-Request to hit
the GoHighLevel backend from our backend.

Then there will be "feign method" which will replicate the curl and hit the GoHighLevel backend.

Then there will be a service method and a serviceImpl method.

Then there will be a controller method.

Example:

curl --request POST \
--url https://services.leadconnectorhq.com/calendars/events/appointments \
--header 'Accept: application/json' \
--header 'Authorization: Bearer 123' \
--header 'Content-Type: application/json' \
--header 'Version: 2021-04-15' \
--data '{
"title": "Test Event",
"meetingLocationType": "custom",
"meetingLocationId": "default",
"overrideLocationConfig": true,
"appointmentStatus": "new",
"assignedUserId": "0007BWpSzSwfiuSl0tR2",
"address": "Zoom",
"ignoreDateRange": false,
"toNotify": false,
"ignoreFreeSlotValidation": true,
"rrule": "RRULE:FREQ=DAILY;INTERVAL=1;COUNT=5",
"calendarId": "CVokAlI8fgw4WYWoCtQz",
"locationId": "C2QujeCh8ZnC7al2InWR",
"contactId": "0007BWpSzSwfiuSl0tR2",
"startTime": "2021-06-23T03:30:00+05:30",
"endTime": "2021-06-23T04:30:00+05:30"
}'

DTOs look like this:

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

Feign Client Method looks:

@PostMapping(
value = "/calendars/events/appointments",
consumes = MediaType.APPLICATION_JSON_VALUE,
produces = MediaType.APPLICATION_JSON_VALUE
)
JsonNode createAppointment(
@RequestHeader("Authorization") String authorization,
@RequestHeader("Version") String version,
@RequestBody GoHighLevelAppointmentCreateRequest request
);

Service Method looks:
JsonNode createCalendarEvent(AppointmentCreateRequest request);

ServiceImpl Method looks:
@Override
public JsonNode createCalendarEvent(AppointmentCreateRequest request) {
String accessKey = authorizationService.getAccessToken(AppKeyContext.getAppKey());
String version = VersionContext.getVersion();
GoHighLevelAppointmentCreateRequest goHighLevelAppointmentCreateRequest =
GoHighLevelAppointmentCreateRequest.fromRequest(request);
return calendarEventClient.createAppointment(accessKey, version, goHighLevelAppointmentCreateRequest);
}

Controller Method looks:
@AppKey
@PostMapping("/appointments")
public ResponseEntity<?> createAppointment(@RequestBody AppointmentCreateRequest request) {
return ResponseEntity.status(HttpStatus.CREATED).body(calendarEventService.createCalendarEvent(request));
}

for get and DELETE i will give you request params in another block and you will prepare the queryParams like this in the
controller method:

@AppKey
@GetMapping("/events")
public ResponseEntity<?> getCalendarEvents(
@RequestParam(value = "calendar-id", required = false) String calendarId,
@RequestParam(value = "group-id", required = false) String groupId,
@RequestParam(value = "user-id", required = false) String userId,
@RequestParam("start-time") Long startTime,
@RequestParam("end-time") Long endTime,
@RequestParam(value = "location-id") String locationId,
@RequestParam(value = "time-zone", required = false, defaultValue = "America/New_York") String timeZone
) {
Map<String, Object> queryParams = new HashMap<>();
MapUtil.putIfNotNull(queryParams, "calendarId", calendarId);
MapUtil.putIfNotNull(queryParams, "groupId", groupId);
MapUtil.putIfNotNull(queryParams, "userId", userId);
MapUtil.putIfNotNull(queryParams, "startTime", startTime);
MapUtil.putIfNotNull(queryParams, "endTime", endTime);
MapUtil.putIfNotNull(queryParams, "locationId", locationId);
MapUtil.putIfNotNull(queryParams, "timezone", timeZone);

        return ResponseEntity.ok(calendarEventService.getCalendarEvents(queryParams));
    }

there will be no request body only path variables and Map<String, Object> queryParams in feign client method,
service method and serviceImpl method.

note:

if you see time in milis/long in the request params please format it like this:
first add timeZone parameter:
@RequestParam(value = "time-zone", required = false, defaultValue = "America/New_York") String timeZone

then format date with date and time
if startDate:
@RequestParam("start-date") LocalDate startDate
@RequestParam("start-time") LocalTime startTime

then convert to milis:
MapUtil.putIfNotNull(queryParams, "startTime", DateTimeUtil.toEpochMillis(LocalDateTime.of(startDate, startTime),
timeZone));

if endDate:
@RequestParam("end-date") LocalDate endDate
@RequestParam("end-time") LocalTime endTime

MapUtil.putIfNotNull(queryParams, "endTime", DateTimeUtil.toEpochMillis(LocalDateTime.of(endDate, endTime), timeZone));
Example:
@AppKey
@GetMapping("/events")
public ResponseEntity<?> getCalendarEvents(
@RequestParam(value = "calendar-id", required = false) String calendarId,
@RequestParam(value = "group-id", required = false) String groupId,
@RequestParam(value = "user-id", required = false) String userId,
@RequestParam(value = "time-zone", required = false, defaultValue = "America/New_York") String timeZone,
@RequestParam("start-date") LocalDate startDate,
@RequestParam("start-time") LocalTime startTime,
@RequestParam("end-date") LocalDate endDate,
@RequestParam("end-time") LocalTime endTime,
@RequestParam(value = "location-id") String locationId

    ) {
        Map<String, Object> queryParams = new HashMap<>();
        MapUtil.putIfNotNull(queryParams, "calendarId", calendarId);
        MapUtil.putIfNotNull(queryParams, "groupId", groupId);
        MapUtil.putIfNotNull(queryParams, "userId", userId);
        MapUtil.putIfNotNull(queryParams, "startTime", DateTimeUtil.toEpochMillis(LocalDateTime.of(startDate, startTime), timeZone));
        MapUtil.putIfNotNull(queryParams, "endTime", DateTimeUtil.toEpochMillis(LocalDateTime.of(endDate, endTime), timeZone));
        MapUtil.putIfNotNull(queryParams, "locationId", locationId);

        return ResponseEntity.ok(calendarEventService.getCalendarEvents(queryParams));
    }

note:
the ghl date-time is inconsistent. sometimes it epoch milis. sometimes it is string. i want my users to be consistent.
the instructions are:
- in request dto/query parameter there should be timeZone which must be given
- in request dto/query parameter the date should be of LocalDate and time should be of LocalTime
- in ghl request dto the date-time should of ZonedDateTime
- in the mapper method inside ghl request the date/time in the request dto should be converted to the ZonedDateTime using
  ZonedDateTime.of(request.getStartDate(), request.getStartTime(),
  java.time.ZoneId.of(request.getTimeZone()))
- in the mapper method while converting the date time must be validate for not null in the request dto

never use snake_case a variable name it is always write them camel case.
Now wait for the curl command please!