package org.example.loficonnect.service;

import com.fasterxml.jackson.databind.JsonNode;
import org.example.loficonnect.dto.request.calendarnotifications.CalendarNotificationUpdateRequest;
import org.example.loficonnect.dto.request.calendarnotifications.NotificationCreateRequest;

import java.util.List;
import java.util.Map;

public interface CalendarNotificationService {
    JsonNode createCalendarNotification(String calendarId, List<NotificationCreateRequest> request);

    JsonNode updateCalendarNotification(String calendarId, String notificationId, CalendarNotificationUpdateRequest request);

    JsonNode getCalendarNotification(String calendarId, String notificationId);

    JsonNode getCalendarNotifications(String calendarId, Map<String, Object> queryParams);

    JsonNode deleteCalendarNotification(String calendarId, String notificationId);



}
