package org.example.loficonnect.controller;

import org.example.loficonnect.commons.annotation.AppKey;
import org.example.loficonnect.dto.request.calendar.CreateCalendarRequest;
import org.example.loficonnect.service.CalendarService;
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
@RequestMapping("/api/v1/ghl/calendars")
public class CalendarController {
    private final CalendarService calendarService;

    public CalendarController(CalendarService calendarService) {
        this.calendarService = calendarService;
    }

    @AppKey
    @PostMapping
    public ResponseEntity<?> createCalendar(@RequestBody CreateCalendarRequest calendarCreateRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(calendarService.createCalendar(calendarCreateRequest));
    }

    @AppKey
    @GetMapping
    public ResponseEntity<?> getCalendars(
            @RequestParam(value = "group-id", required = false) String groupId
    ) {
        Map<String, Object> queryParams = new HashMap<>();
        MapUtil.putIfNotNull(queryParams, "groupId", groupId);
        return ResponseEntity.status(HttpStatus.OK).body(calendarService.getCalendars(queryParams));
    }

    @AppKey
    @GetMapping("/{calendar-id}")
    public ResponseEntity<?> getCalendar(@PathVariable("calendar-id") String calendarId) {
        return ResponseEntity.status(HttpStatus.OK).body(calendarService.getCalendar(calendarId));
    }

    @AppKey
    @DeleteMapping("/{calendar-id}")
    public ResponseEntity<?> deleteCalendar(@PathVariable("calendar-id") String calendarId) {
        return ResponseEntity.status(HttpStatus.OK).body(calendarService.deleteCalendar(calendarId));
    }

    @AppKey
    @GetMapping("/{calendar-id}/free-slots")
    public ResponseEntity<?> getFreeSlots(@PathVariable("calendar-id") String calendarId,
                                          @RequestParam(value = "time-zone", required = false, defaultValue = "America/New_York") String timeZone,
                                          @RequestParam(value = "user-id", required = false) String userId,
                                          @RequestParam("start-date") LocalDate startDate,
                                          @RequestParam("start-time") LocalTime startTime,
                                          @RequestParam("end-date") LocalDate endDate,
                                          @RequestParam("end-time") LocalTime endTime) {
        Map<String, Object> queryParams = new HashMap<>();
        MapUtil.putIfNotNull(queryParams, "timezone", timeZone);
        MapUtil.putIfNotNull(queryParams, "user-id", userId);
        MapUtil.putIfNotNull(queryParams, "startDate", DateTimeUtil.toEpochMillis(LocalDateTime.of(startDate, startTime), timeZone));
        MapUtil.putIfNotNull(queryParams, "endDate", DateTimeUtil.toEpochMillis(LocalDateTime.of(endDate, endTime), timeZone));
        return ResponseEntity.status(HttpStatus.OK).body(calendarService.getFreeSlots(calendarId, queryParams));
    }
}
