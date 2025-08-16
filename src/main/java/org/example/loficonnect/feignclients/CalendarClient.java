package org.example.loficonnect.feignclients;

import com.fasterxml.jackson.databind.JsonNode;
import org.example.loficonnect.config.FeignLoggingConfig;
import org.example.loficonnect.dto.mapper.GoHighLevelCalendarCreateRequest;
import org.example.loficonnect.dto.mapper.GoHighLevelCalendarUpdateRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@FeignClient(name = "calendarClient", url = "https://services.leadconnectorhq.com", configuration = FeignLoggingConfig.class)
public interface CalendarClient {

    @PostMapping("/calendars/")
    JsonNode createCalendar(
            @RequestHeader("Authorization") String bearerToken,
            @RequestHeader("Version") String version,
            @RequestBody GoHighLevelCalendarCreateRequest request
    );

    @GetMapping("/calendars/")
    JsonNode getCalendars(
            @RequestHeader("Authorization") String authorization,
            @RequestHeader("Version") String version,
            @RequestParam Map<String, Object> queryParams
    );

    @GetMapping("/calendars/{calendarId}")
    JsonNode getCalendar(
            @RequestHeader("Authorization") String authorization,
            @RequestHeader("Version") String version,
            @PathVariable String calendarId
    );

    @PutMapping("/calendars/{calendarId}")
    JsonNode updateCalendar(
            @RequestHeader(name = "Authorization") String authorization,
            @RequestHeader(name = "Version") String version,
            @PathVariable("calendarId") String calendarId,
            @RequestBody GoHighLevelCalendarUpdateRequest request
    );

    @DeleteMapping("/calendars/{calendarId}")
    JsonNode deleteCalendarById(
            @RequestHeader("Authorization") String authorization,
            @RequestHeader("Version") String version,
            @PathVariable("calendarId") String calendarId
    );

    @GetMapping("/calendars/{calendarId}/free-slots")
    JsonNode getFreeSlots(
            @RequestHeader("Authorization") String authorization,
            @RequestHeader("Version") String version,
            @PathVariable("calendarId") String calendarId,
            @RequestParam Map<String, Object> queryParams
    );

}
