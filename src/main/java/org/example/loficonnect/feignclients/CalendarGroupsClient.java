package org.example.loficonnect.feignclients;

import com.fasterxml.jackson.databind.JsonNode;
import org.example.loficonnect.commons.config.FeignLoggingConfig;
import org.example.loficonnect.dto.mapper.calendargroups.GoHighLevelCalendarGroupCreateRequest;
import org.example.loficonnect.dto.mapper.calendargroups.GoHighLevelCalendarGroupStatusUpdateRequest;
import org.example.loficonnect.dto.mapper.calendargroups.GoHighLevelCalendarGroupUpdateRequest;
import org.example.loficonnect.dto.mapper.calendargroups.GoHighLevelCalendarGroupValidateSlugRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@FeignClient(name = "calendarGroupsClient", url = "https://services.leadconnectorhq.com", configuration = FeignLoggingConfig.class)
public interface CalendarGroupsClient {
    @GetMapping(
            value = "/calendars/groups",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    JsonNode getCalendarGroups(
            @RequestHeader("Authorization") String authorization,
            @RequestHeader("Version") String version,
            @RequestParam Map<String, Object> queryParams
    );

    @PostMapping(
            value = "/calendars/groups",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    JsonNode createCalendarGroup(
            @RequestHeader("Authorization") String authorization,
            @RequestHeader("Version") String version,
            @RequestBody GoHighLevelCalendarGroupCreateRequest request
    );

    @PostMapping(
            value = "/calendars/groups/validate-slug",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    JsonNode validateCalendarGroupSlug(
            @RequestHeader("Authorization") String authorization,
            @RequestHeader("Version") String version,
            @RequestBody GoHighLevelCalendarGroupValidateSlugRequest request
    );

    @DeleteMapping(
            value = "/calendars/groups/{groupId}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    JsonNode deleteCalendarGroup(
            @RequestHeader("Authorization") String authorization,
            @RequestHeader("Version") String version,
            @PathVariable("groupId") String groupId
    );

    @PutMapping(
            value = "/calendars/groups/{groupId}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    JsonNode updateCalendarGroup(
            @RequestHeader("Authorization") String authorization,
            @RequestHeader("Version") String version,
            @PathVariable("groupId") String groupId,
            @RequestBody GoHighLevelCalendarGroupUpdateRequest request
    );

    @PutMapping(
            value = "/calendars/groups/{groupId}/status",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    JsonNode updateGroupStatus(
            @RequestHeader("Authorization") String authorization,
            @RequestHeader("Version") String version,
            @PathVariable("groupId") String groupId,
            @RequestBody GoHighLevelCalendarGroupStatusUpdateRequest request
    );




}
