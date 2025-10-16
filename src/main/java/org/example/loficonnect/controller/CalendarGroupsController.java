package org.example.loficonnect.controller;

import org.example.loficonnect.config.AppKey;
import org.example.loficonnect.dto.request.calendargroups.CalendarGroupCreateRequest;
import org.example.loficonnect.dto.request.calendargroups.CalendarGroupStatusUpdateRequest;
import org.example.loficonnect.dto.request.calendargroups.CalendarGroupUpdateRequest;
import org.example.loficonnect.dto.request.calendargroups.CalendarGroupValidateSlugRequest;
import org.example.loficonnect.service.CalendarGroupsService;
import org.example.loficonnect.util.MapUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/ghl")
public class CalendarGroupsController {
    private final CalendarGroupsService calendarGroupsService;

    public CalendarGroupsController(CalendarGroupsService calendarGroupsService) {
        this.calendarGroupsService = calendarGroupsService;
    }

    @AppKey
    @GetMapping("/calendars/groups")
    public ResponseEntity<?> getCalendarGroups(
            @RequestParam("location-id") String locationId
    ) {
        Map<String, Object> queryParams = new HashMap<>();
        MapUtil.putIfNotNull(queryParams, "locationId", locationId);

        return ResponseEntity.ok(calendarGroupsService.getCalendarGroups(queryParams));
    }

    @AppKey
    @PostMapping("/calendars/groups")
    public ResponseEntity<?> createCalendarGroup(@RequestBody CalendarGroupCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(calendarGroupsService.createCalendarGroup(request));
    }

    @AppKey
    @PostMapping("/calendars/groups/validate-slug")
    public ResponseEntity<?> validateCalendarGroupSlug(@RequestBody CalendarGroupValidateSlugRequest request) {
        return ResponseEntity.ok(calendarGroupsService.validateCalendarGroupSlug(request));
    }

    @AppKey
    @DeleteMapping("/calendars/groups/{group-id}")
    public ResponseEntity<?> deleteCalendarGroup(@PathVariable("group-id") String groupId) {
        return ResponseEntity.ok(calendarGroupsService.deleteCalendarGroup(groupId));
    }

    @AppKey
    @PutMapping("/calendars/groups/{group-id}")
    public ResponseEntity<?> updateCalendarGroup(
            @PathVariable("group-id") String groupId,
            @RequestBody CalendarGroupUpdateRequest request
    ) {
        return ResponseEntity.ok(calendarGroupsService.updateCalendarGroup(groupId, request));
    }

    @AppKey
    @PutMapping("/calendars/groups/{group-id}/status")
    public ResponseEntity<?> updateGroupStatus(
            @PathVariable("group-id") String groupId,
            @RequestBody CalendarGroupStatusUpdateRequest request
    ) {
        return ResponseEntity.ok(calendarGroupsService.updateGroupStatus(groupId, request));
    }
}
