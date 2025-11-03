package org.example.loficonnect.service;

import com.fasterxml.jackson.databind.JsonNode;
import org.example.loficonnect.dto.request.calendar.CreateCalendarRequest;

import java.util.Map;

public interface CalendarService {
    JsonNode createCalendar(CreateCalendarRequest request);

    JsonNode getCalendars(Map<String, Object> queryParams);

    JsonNode getCalendar(String calendarId);

    JsonNode deleteCalendar(String calendarId);

    JsonNode getFreeSlots(String calendarId, Map<String, Object> queryParams);
}
