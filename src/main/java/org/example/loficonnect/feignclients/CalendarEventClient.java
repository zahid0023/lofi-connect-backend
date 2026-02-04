package org.example.loficonnect.feignclients;

import com.fasterxml.jackson.databind.JsonNode;
import org.example.loficonnect.commons.config.FeignLoggingConfig;
import org.example.loficonnect.dto.mapper.calendarevents.GoHighLevelBlockSlotCreateRequest;
import org.example.loficonnect.dto.mapper.calendarevents.GoHighLevelAppointmentCreateRequest;
import org.example.loficonnect.dto.mapper.calendarevents.GoHighLevelAppointmentUpdateRequest;
import org.example.loficonnect.dto.mapper.calendarevents.GoHighLevelBlockSlotUpdateRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@FeignClient(name = "calendarEventClient", url = "https://services.leadconnectorhq.com", configuration = FeignLoggingConfig.class)
public interface CalendarEventClient {
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

    @PutMapping(
            value = "/calendars/events/appointments/{appointmentId}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    JsonNode updateAppointment(
            @RequestHeader("Authorization") String authorization,
            @RequestHeader("Version") String version,
            @PathVariable("appointmentId") String appointmentId,
            @RequestBody GoHighLevelAppointmentUpdateRequest request
    );

    @GetMapping(
            value = "/calendars/events/appointments/{appointmentId}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    JsonNode getAppointment(
            @RequestHeader("Authorization") String authorization,
            @RequestHeader("Version") String version,
            @PathVariable("appointmentId") String appointmentId
    );

    @GetMapping(
            value = "/calendars/events",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    JsonNode getCalendarEvents(
            @RequestHeader("Authorization") String authorization,
            @RequestHeader("Version") String version,
            @RequestParam Map<String, Object> queryParams
    );

    @GetMapping(
            value = "/calendars/blocked-slots",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    JsonNode getBlockedSlots(
            @RequestHeader("Authorization") String authorization,
            @RequestHeader("Version") String version,
            @RequestParam Map<String, Object> queryParams
    );

    @PostMapping(
            value = "/calendars/events/block-slots",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    JsonNode createBlockSlot(
            @RequestHeader("Authorization") String authorization,
            @RequestHeader("Version") String version,
            @RequestBody GoHighLevelBlockSlotCreateRequest request
    );

    @PutMapping(
            value = "/calendars/events/block-slots/{eventId}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    JsonNode updateBlockSlot(
            @RequestHeader("Authorization") String authorization,
            @RequestHeader("Version") String version,
            @PathVariable("eventId") String eventId,
            @RequestBody GoHighLevelBlockSlotUpdateRequest request
    );

    @DeleteMapping(
            value = "/calendars/events/{eventId}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    JsonNode deleteEvent(
            @RequestHeader("Authorization") String authorization,
            @RequestHeader("Version") String version,
            @PathVariable("eventId") String eventId
    );



}
