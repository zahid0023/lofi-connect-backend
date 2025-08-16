package org.example.loficonnect.controller;

import org.example.loficonnect.config.AppKey;
import org.example.loficonnect.dto.request.calendar.CalendarRequest;
import org.example.loficonnect.service.CalendarService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class CalendarController {
    private final CalendarService calendarService;

    public CalendarController(CalendarService calendarService) {
        this.calendarService = calendarService;
    }

    @AppKey
    @PostMapping("/calendars")
    public ResponseEntity<?> createCalendar(@RequestBody CalendarRequest calendarRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(calendarService.createCalendar(calendarRequest));
    }
}
