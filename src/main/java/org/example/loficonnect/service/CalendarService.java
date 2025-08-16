package org.example.loficonnect.service;

import com.fasterxml.jackson.databind.JsonNode;
import org.example.loficonnect.dto.request.calendar.CalendarRequest;

public interface CalendarService {
    JsonNode createCalendar(CalendarRequest request);
}
