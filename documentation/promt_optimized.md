I am trying to create an API Gateway for GoHighLevel APIs. They have some consistencies which i want to improve and make
easy to use for my users. I am using SpringBoot for this.

I will give a curl command as input and you will have to give me:

For POST/PUT

DTOs

- request dto class(which my users will see)
- ghl request dto class (which i will send to the ghl backend as payload content)
- request dto class will be
    - annotated with @Data
    - annotated with @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    - variable name as camelCase
- ghl request dto will be
    - annotated with @Data
    - variable name is exactly as in curl
- a mapper method in ghl request dto which will map the fields from the request dto

Feign Client Method

- it will be sent to the ghl backend
- it will take @RequestBody ghl dto

Service Method

- It will be called by my controller method
- It will take request dto as argument and convert it to ght dto using the mapper method in ghl dto

Service Implementation Method

- It will implement the Service Method

Controller Method

- It is the end point my user will hit to access the ghl backend apis and take @RequestBody request dto

Example:

Input:

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

Output:

- request dto:
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

- ghl request dto
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

- feign client method
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

- service method:
  JsonNode createCalendarEvent(AppointmentCreateRequest request);

- serviceImpl method
  @Override
  public JsonNode createCalendar(CalendarCreateRequest request) {
  String accessKey = authorizationService.getAccessToken(AppKeyContext.getAppKey());
  String version = VersionContext.getVersion();
  GoHighLevelCalendarCreateRequest goHighLevelCalendarCreateRequest = GoHighLevelCalendarCreateRequest.fromRequest(
  request);
  return calendarClient.createCalendar(accessKey, version, goHighLevelCalendarCreateRequest);
  }

- controller method
  @AppKey
  @PostMapping("/appointments")
  public ResponseEntity<?> createAppointment(@RequestBody AppointmentCreateRequest request) {
  return ResponseEntity.status(HttpStatus.CREATED).body(calendarEventService.createCalendarEvent(request));
  }

For GET/DELETE
there will be curl command and/or path variables and/or query parameters block

- path variables and/or
- query parameters

Feign Client Method

- Feign Client Method will get Map<String, Object> queryParameters as its argument for query parameters.
- Feign Client Method will get @PathVariable as its path variable if any exists

Service Method

- Service Method will get Map<String, Object queryParameters> as its arguments for query parameters
- Service Method will get path variables as a method argument

Service Implementation Method

- Implements the Service Method

Controller Method:

- Gets @RequestParam and/or @PathVariable which reflects the block given after curl
- checks if the params are not null and populates the map and sends as arguments to service method

Example:

- feign client method
  @GetMapping(
  value = "/calendars/events",
  produces = MediaType.APPLICATION_JSON_VALUE
  )
  JsonNode getCalendarEvents(
  @RequestHeader("Authorization") String authorization,
  @RequestHeader("Version") String version,
  @RequestParam Map<String, Object> queryParams
  );

- service method
  JsonNode getCalendarEvents(Map<String, Object> queryParams);

- serviceImpl method
  @Override
  public JsonNode getCalendarEvents(Map<String, Object> queryParams) {
  String accessKey = authorizationService.getAccessToken(AppKeyContext.getAppKey());
  String version = VersionContext.getVersion();
  return calendarEventClient.getCalendarEvents(accessKey, version, queryParams);
  }

- controller method
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
  MapUtil.putIfNotNull(queryParams, "startTime", DateTimeUtil.toEpochMillis(LocalDateTime.of(startDate, startTime),
  timeZone));
  MapUtil.putIfNotNull(queryParams, "endTime", DateTimeUtil.toEpochMillis(LocalDateTime.of(endDate, endTime),
  timeZone));
  MapUtil.putIfNotNull(queryParams, "locationId", locationId);

        return ResponseEntity.ok(calendarEventService.getCalendarEvents(queryParams));
  }

notes:

- path variable and query parameters format:
    - @PathVariable("appointment-id") String appointmentId there will be - in bracket
    - @RequestParam("appointment-id") String appointmentId there will be - in bracket

- go high level has inconsistent date-time format. sometimes its epoch milis. sometimes it string.we want to give our
  users a consistent date-time format.
    - in request dto/ query param/ path variable there will be timeZone which will be type String
    - in request dto/ query param/ path variable there will be date which will be type LocalDate
    - in request dto/ query param/ path variable there will be time which will be type LocalTime
    - in ghl request dto/ query param/ path variable there will be date-time as ZonedDateTime
    - ZonedDateTime will be converted from LocalDate and LocalTime of request dto using a util method

- response type from any method except for controller method will be type JsonNode

- response type from controller method will be ResponseEntity<?>
- name: *.client i will give the feign client name
- i want the methods in the classes for service, serviceImpl, feignClient, controller not the class definitions

wait for curl command.