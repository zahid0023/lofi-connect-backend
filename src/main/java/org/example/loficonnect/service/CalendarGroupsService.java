package org.example.loficonnect.service;

import com.fasterxml.jackson.databind.JsonNode;
import org.example.loficonnect.dto.request.calendargroups.CalendarGroupCreateRequest;
import org.example.loficonnect.dto.request.calendargroups.CalendarGroupStatusUpdateRequest;
import org.example.loficonnect.dto.request.calendargroups.CalendarGroupUpdateRequest;
import org.example.loficonnect.dto.request.calendargroups.CalendarGroupValidateSlugRequest;

import java.util.Map;

public interface CalendarGroupsService {
    JsonNode getCalendarGroups(Map<String, Object> queryParams);

    JsonNode createCalendarGroup(CalendarGroupCreateRequest request);

    JsonNode validateCalendarGroupSlug(CalendarGroupValidateSlugRequest request);

    JsonNode deleteCalendarGroup(String groupId);

    JsonNode updateCalendarGroup(String groupId, CalendarGroupUpdateRequest request);

    JsonNode updateGroupStatus(String groupId, CalendarGroupStatusUpdateRequest request);

}
