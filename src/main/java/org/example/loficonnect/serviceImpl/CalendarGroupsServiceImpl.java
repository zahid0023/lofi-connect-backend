package org.example.loficonnect.serviceImpl;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import org.example.loficonnect.dto.mapper.calendargroups.GoHighLevelCalendarGroupCreateRequest;
import org.example.loficonnect.dto.mapper.calendargroups.GoHighLevelCalendarGroupStatusUpdateRequest;
import org.example.loficonnect.dto.mapper.calendargroups.GoHighLevelCalendarGroupUpdateRequest;
import org.example.loficonnect.dto.mapper.calendargroups.GoHighLevelCalendarGroupValidateSlugRequest;
import org.example.loficonnect.dto.request.calendargroups.CalendarGroupCreateRequest;
import org.example.loficonnect.dto.request.calendargroups.CalendarGroupStatusUpdateRequest;
import org.example.loficonnect.dto.request.calendargroups.CalendarGroupUpdateRequest;
import org.example.loficonnect.dto.request.calendargroups.CalendarGroupValidateSlugRequest;
import org.example.loficonnect.feignclients.CalendarGroupsClient;
import org.example.loficonnect.service.AuthorizationService;
import org.example.loficonnect.service.CalendarGroupsService;
import org.example.loficonnect.util.AppKeyContext;
import org.example.loficonnect.util.VersionContext;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@Slf4j
public class CalendarGroupsServiceImpl implements CalendarGroupsService {
    private final AuthorizationService authorizationService;
    private final CalendarGroupsClient calendarGroupsClient;

    public CalendarGroupsServiceImpl(AuthorizationService authorizationService, CalendarGroupsClient calendarGroupsClient) {
        this.authorizationService = authorizationService;
        this.calendarGroupsClient = calendarGroupsClient;
    }

    @Override
    public JsonNode getCalendarGroups(Map<String, Object> queryParams) {
        String accessKey = authorizationService.getAccessToken(AppKeyContext.getAppKey());
        String version = VersionContext.getVersion();
        return calendarGroupsClient.getCalendarGroups(accessKey, version, queryParams);
    }

    @Override
    public JsonNode createCalendarGroup(CalendarGroupCreateRequest request) {
        String accessKey = authorizationService.getAccessToken(AppKeyContext.getAppKey());
        String version = VersionContext.getVersion();
        GoHighLevelCalendarGroupCreateRequest ghlRequest =
                GoHighLevelCalendarGroupCreateRequest.fromRequest(request);
        return calendarGroupsClient.createCalendarGroup(accessKey, version, ghlRequest);
    }

    @Override
    public JsonNode validateCalendarGroupSlug(CalendarGroupValidateSlugRequest request) {
        String accessKey = authorizationService.getAccessToken(AppKeyContext.getAppKey());
        String version = VersionContext.getVersion();
        GoHighLevelCalendarGroupValidateSlugRequest ghlRequest =
                GoHighLevelCalendarGroupValidateSlugRequest.fromRequest(request);
        return calendarGroupsClient.validateCalendarGroupSlug(accessKey, version, ghlRequest);
    }

    @Override
    public JsonNode deleteCalendarGroup(String groupId) {
        String accessKey = authorizationService.getAccessToken(AppKeyContext.getAppKey());
        String version = VersionContext.getVersion();
        return calendarGroupsClient.deleteCalendarGroup(accessKey, version, groupId);
    }

    @Override
    public JsonNode updateCalendarGroup(String groupId, CalendarGroupUpdateRequest request) {
        String accessKey = authorizationService.getAccessToken(AppKeyContext.getAppKey());
        String version = VersionContext.getVersion();
        GoHighLevelCalendarGroupUpdateRequest ghlRequest =
                GoHighLevelCalendarGroupUpdateRequest.fromRequest(request);
        return calendarGroupsClient.updateCalendarGroup(accessKey, version, groupId, ghlRequest);
    }

    @Override
    public JsonNode updateGroupStatus(String groupId, CalendarGroupStatusUpdateRequest request) {
        String accessKey = authorizationService.getAccessToken(AppKeyContext.getAppKey());
        String version = VersionContext.getVersion();
        GoHighLevelCalendarGroupStatusUpdateRequest ghlRequest =
                GoHighLevelCalendarGroupStatusUpdateRequest.fromRequest(request);

        return calendarGroupsClient.updateGroupStatus(accessKey, version, groupId, ghlRequest);
    }


}
