package org.example.loficonnect.serviceImpl;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import org.example.loficonnect.dto.mapper.GoHighLevelCalendarCreateRequest;
import org.example.loficonnect.dto.mapper.GoHighLevelCalendarUpdateRequest;
import org.example.loficonnect.dto.request.calendar.CalendarCreateRequest;
import org.example.loficonnect.dto.request.calendar.CalendarUpdateRequest;
import org.example.loficonnect.feignclients.CalendarClient;
import org.example.loficonnect.service.AuthorizationService;
import org.example.loficonnect.service.CalendarService;
import org.example.loficonnect.util.AppKeyContext;
import org.example.loficonnect.util.VersionContext;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@Slf4j
public class CalendarServiceImpl implements CalendarService {
    private final CalendarClient calendarClient;
    private final AuthorizationService authorizationService;

    public CalendarServiceImpl(CalendarClient calendarClient,
                               AuthorizationService authorizationService) {
        this.calendarClient = calendarClient;
        this.authorizationService = authorizationService;
    }

    @Override
    public JsonNode createCalendar(CalendarCreateRequest request) {
        String accessKey = authorizationService.getAccessToken(AppKeyContext.getAppKey());
        String version = VersionContext.getVersion();
        GoHighLevelCalendarCreateRequest goHighLevelCalendarCreateRequest = GoHighLevelCalendarCreateRequest.fromRequest(request);
        return calendarClient.createCalendar(accessKey, version, goHighLevelCalendarCreateRequest);
    }

    @Override
    public JsonNode getCalendars(Map<String, Object> queryParams) {
        String accessKey = authorizationService.getAccessToken(AppKeyContext.getAppKey());
        String version = VersionContext.getVersion();
        return calendarClient.getCalendars(accessKey, version, queryParams);
    }

    @Override
    public JsonNode getCalendar(String calendarId) {
        String accessKey = authorizationService.getAccessToken(AppKeyContext.getAppKey());
        String version = VersionContext.getVersion();
        log.info("Calling getCalendar with Authorization={}, Version={}, calendarId={}",
                accessKey, version, calendarId);
        return calendarClient.getCalendar(accessKey, version, calendarId);
    }


    @Override
    public JsonNode updateCalendar(String calendarId, CalendarUpdateRequest request) {
        String accessKey = authorizationService.getAccessToken(AppKeyContext.getAppKey());
        String version = VersionContext.getVersion();
        GoHighLevelCalendarUpdateRequest updateRequest = GoHighLevelCalendarUpdateRequest.fromRequest(request);
        return calendarClient.updateCalendar(accessKey, version, calendarId, updateRequest);
    }

    @Override
    public JsonNode deleteCalendar(String calendarId) {
        String accessKey = authorizationService.getAccessToken(AppKeyContext.getAppKey());
        String version = VersionContext.getVersion();
        return calendarClient.deleteCalendarById(accessKey, version, calendarId);
    }

    @Override
    public JsonNode getFreeSlots(String calendarId, Map<String, Object> queryParams) {
        String accessKey = authorizationService.getAccessToken(AppKeyContext.getAppKey());
        String version = VersionContext.getVersion();
        return calendarClient.getFreeSlots(accessKey, version, calendarId, queryParams);
    }
}
