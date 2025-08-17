package org.example.loficonnect.feignclients;

import com.fasterxml.jackson.databind.JsonNode;
import org.example.loficonnect.config.FeignLoggingConfig;
import org.example.loficonnect.dto.mapper.calendarnotifications.GoHighLevelCalendarNotificationUpdateRequest;
import org.example.loficonnect.dto.mapper.calendarnotifications.GoHighLevelNotificationCreateRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@FeignClient(name = "calendarNotificationClient", url = "https://services.leadconnectorhq.com", configuration = FeignLoggingConfig.class)
public interface CalendarNotificationClient {
    @PostMapping(
            value = "/calendars/{calendarId}/notifications",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    JsonNode createNotification(
            @RequestHeader("Authorization") String authorization,
            @RequestHeader("Version") String version,
            @PathVariable("calendarId") String calendarId,
            @RequestBody List<GoHighLevelNotificationCreateRequest> request
    );

    @PutMapping(
            value = "/calendars/{calendarId}/notifications/{notificationId}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    JsonNode updateCalendarNotification(
            @RequestHeader("Authorization") String authorization,
            @RequestHeader("Version") String version,
            @PathVariable("calendarId") String calendarId,
            @PathVariable("notificationId") String notificationId,
            @RequestBody GoHighLevelCalendarNotificationUpdateRequest request
    );

    @GetMapping(
            value = "/calendars/{calendarId}/notifications/{notificationId}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    JsonNode getCalendarNotification(
            @RequestHeader("Authorization") String authorization,
            @RequestHeader("Version") String version,
            @PathVariable("calendarId") String calendarId,
            @PathVariable("notificationId") String notificationId
    );

    @GetMapping(
            value = "/calendars/{calendarId}/notifications",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    JsonNode getCalendarNotifications(
            @RequestHeader("Authorization") String authorization,
            @RequestHeader("Version") String version,
            @PathVariable("calendarId") String calendarId,
            @RequestParam Map<String, Object> queryParams
    );

    @DeleteMapping(
            value = "/calendars/{calendarId}/notifications/{notificationId}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    JsonNode deleteCalendarNotification(
            @RequestHeader("Authorization") String authorization,
            @RequestHeader("Version") String version,
            @PathVariable("calendarId") String calendarId,
            @PathVariable("notificationId") String notificationId
    );


}
