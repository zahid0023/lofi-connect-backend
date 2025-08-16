package org.example.loficonnect.feignclients;

import com.fasterxml.jackson.databind.JsonNode;
import org.example.loficonnect.config.FeignLoggingConfig;
import org.example.loficonnect.dto.mapper.GoHighLevelCalendarRequest;
import org.example.loficonnect.dto.request.calendar.CalendarRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "calendarClient", url = "https://services.leadconnectorhq.com", configuration = FeignLoggingConfig.class)
public interface CalendarClient {

    @PostMapping("/calendars/")
    JsonNode createCalendar(
            @RequestHeader("Authorization") String bearerToken,
            @RequestHeader("Version") String version,
            @RequestBody GoHighLevelCalendarRequest request
    );
}
