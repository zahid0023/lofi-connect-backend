package org.example.loficonnect.service;

import com.fasterxml.jackson.databind.JsonNode;
import org.example.loficonnect.dto.request.calendar.CalendarCreateRequest;
import org.example.loficonnect.dto.request.calendar.CalendarUpdateRequest;

import java.util.Map;

public interface CalendarService {
    JsonNode createCalendar(CalendarCreateRequest request);

    JsonNode getCalendars(Map<String, Object> queryParams);

    JsonNode getCalendar(String calendarId);

    JsonNode updateCalendar(String calendarId, CalendarUpdateRequest request);

    JsonNode deleteCalendar(String calendarId);

    JsonNode getFreeSlots(String calendarId, Map<String, Object> queryParams);
}
