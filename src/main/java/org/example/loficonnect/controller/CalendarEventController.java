package org.example.loficonnect.controller;

import org.example.loficonnect.config.AppKey;
import org.example.loficonnect.dto.request.calendarevents.AppointmentCreateRequest;
import org.example.loficonnect.dto.request.calendarevents.AppointmentUpdateRequest;
import org.example.loficonnect.dto.request.calendarevents.BlockSlotCreateRequest;
import org.example.loficonnect.dto.request.calendarevents.BlockSlotUpdateRequest;
import org.example.loficonnect.service.CalendarEventService;
import org.example.loficonnect.util.DateTimeUtil;
import org.example.loficonnect.util.MapUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/ghl")
public class CalendarEventController {
    private final CalendarEventService calendarEventService;

    public CalendarEventController(CalendarEventService calendarEventService) {
        this.calendarEventService = calendarEventService;
    }

    @AppKey
    @PostMapping("/calendars/events/appointments")
    public ResponseEntity<?> createAppointment(@RequestBody AppointmentCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(calendarEventService.createCalendarEvent(request));
    }

    @AppKey
    @PutMapping("/calendars/events/appointments/{appointment-id}")
    public ResponseEntity<?> updateAppointment(
            @PathVariable("appointment-id") String appointmentId,
            @RequestBody AppointmentUpdateRequest request
    ) {
        return ResponseEntity.ok(calendarEventService.updateCalendarEvent(appointmentId, request));
    }

    @AppKey
    @GetMapping("/calendars/events/appointments/{appointmentId}")
    public ResponseEntity<?> getAppointment(
            @PathVariable String appointmentId
    ) {
        return ResponseEntity.ok(calendarEventService.getCalendarEvent(appointmentId));
    }

    @AppKey
    @GetMapping("/calendars/events/events")
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

    @AppKey
    @GetMapping("/calendars/events/blocked-slots")
    public ResponseEntity<?> getBlockedSlots(
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
        MapUtil.putIfNotNull(queryParams, "timezone", timeZone);

        return ResponseEntity.ok(calendarEventService.getBlockedSlots(queryParams));
    }

    @AppKey
    @PostMapping("/calendars/events/block-slots")
    public ResponseEntity<?> createBlockSlot(@RequestBody BlockSlotCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(calendarEventService.createBlockSlot(request));
    }

    @AppKey
    @PutMapping("/calendars/events/block-slots/{eventId}")
    public ResponseEntity<?> updateBlockSlot(
            @PathVariable("eventId") String eventId,
            @RequestBody BlockSlotUpdateRequest request
    ) {
        return ResponseEntity.ok(calendarEventService.updateBlockSlot(eventId, request));
    }

    @AppKey
    @DeleteMapping("/calendars/events/events/{eventId}")
    public ResponseEntity<?> deleteCalendarEvent(@PathVariable("eventId") String eventId) {
        return ResponseEntity.ok(calendarEventService.deleteCalendarEvent(eventId));
    }

}