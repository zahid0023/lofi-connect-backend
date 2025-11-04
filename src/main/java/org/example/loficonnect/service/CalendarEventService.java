package org.example.loficonnect.service;

import com.fasterxml.jackson.databind.JsonNode;
import org.example.loficonnect.dto.request.calendarevents.BlockSlotCreateRequest;
import org.example.loficonnect.dto.request.calendarevents.AppointmentCreateRequest;
import org.example.loficonnect.dto.request.calendarevents.AppointmentUpdateRequest;
import org.example.loficonnect.dto.request.calendarevents.BlockSlotUpdateRequest;

import java.util.Map;

public interface CalendarEventService {
    JsonNode createCalendarEvent(AppointmentCreateRequest request);

    JsonNode updateCalendarEvent(String appointmentId, AppointmentUpdateRequest request);

    JsonNode getCalendarEvent(String appointmentId);

    JsonNode getCalendarEvents(Map<String, Object> queryParams);

    JsonNode getBlockedSlots(Map<String, Object> queryParams);

    JsonNode createBlockSlot(BlockSlotCreateRequest request);

    JsonNode updateBlockSlot(String eventId, BlockSlotUpdateRequest request);

    JsonNode deleteCalendarEvent(String eventId);
}
