package org.example.loficonnect.controller;

import org.example.loficonnect.commons.annotation.AppKey;
import org.example.loficonnect.dto.request.calendarnotifications.CalendarNotificationUpdateRequest;
import org.example.loficonnect.dto.request.calendarnotifications.NotificationCreateRequest;
import org.example.loficonnect.service.CalendarNotificationService;
import org.example.loficonnect.util.MapUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/ghl")
public class CalendarNotificationController {
    private final CalendarNotificationService calendarNotificationService;

    public CalendarNotificationController(CalendarNotificationService calendarNotificationService) {
        this.calendarNotificationService = calendarNotificationService;
    }

    @AppKey
    @PostMapping("/calendars/notifications/{calendarId}")
    public ResponseEntity<?> createNotification(
            @PathVariable("calendarId") String calendarId,
            @RequestBody List<NotificationCreateRequest> request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(calendarNotificationService.createCalendarNotification(calendarId, request));
    }

    @AppKey
    @PutMapping("/calendars/notifications/{calendar-id}/{notification-id}")
    public ResponseEntity<?> updateCalendarNotification(
            @PathVariable("calendar-id") String calendarId,
            @PathVariable("notification-id") String notificationId,
            @RequestBody CalendarNotificationUpdateRequest request
    ) {
        return ResponseEntity.ok(
                calendarNotificationService.updateCalendarNotification(calendarId, notificationId, request)
        );
    }

    @AppKey
    @GetMapping("/calendars/notifications/{calendar-id}/{notification-id}")
    public ResponseEntity<?> getCalendarNotification(
            @PathVariable("calendar-id") String calendarId,
            @PathVariable("notification-id") String notificationId
    ) {
        return ResponseEntity.ok(
                calendarNotificationService.getCalendarNotification(calendarId, notificationId)
        );
    }

    @AppKey
    @GetMapping("/calendars/notifications/{calendar-id}")
    public ResponseEntity<?> getCalendarNotifications(
            @PathVariable("calendar-id") String calendarId,
            @RequestParam(value = "deleted", required = false) Boolean deleted,
            @RequestParam(value = "isActive", required = false) Boolean isActive,
            @RequestParam(value = "limit", required = false, defaultValue = "100") Integer limit,
            @RequestParam(value = "skip", required = false, defaultValue = "0") Integer skip
    ) {
        Map<String, Object> queryParams = new HashMap<>();
        MapUtil.putIfNotNull(queryParams, "deleted", deleted);
        MapUtil.putIfNotNull(queryParams, "isActive", isActive);
        MapUtil.putIfNotNull(queryParams, "limit", limit);
        MapUtil.putIfNotNull(queryParams, "skip", skip);

        return ResponseEntity.ok(calendarNotificationService.getCalendarNotifications(calendarId, queryParams));
    }

    @AppKey
    @DeleteMapping("/calendars/notifications/{calendar-id}/{notification-id}")
    public ResponseEntity<?> deleteCalendarNotification(
            @PathVariable("calendar-id") String calendarId,
            @PathVariable("notification-id") String notificationId
    ) {
        return ResponseEntity.ok(calendarNotificationService.deleteCalendarNotification(calendarId, notificationId));
    }

}
