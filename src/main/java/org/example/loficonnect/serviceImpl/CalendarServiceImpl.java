package org.example.loficonnect.serviceImpl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.example.loficonnect.dto.mapper.calendar.GoHighLevelCreateCalendarRequest;
import org.example.loficonnect.dto.request.calendar.CreateCalendarRequest;
import org.example.loficonnect.feignclients.CalendarClient;
import org.example.loficonnect.service.AuthorizationService;
import org.example.loficonnect.service.CalendarService;
import org.example.loficonnect.util.AppKeyContext;
import org.example.loficonnect.util.LocationContext;
import org.example.loficonnect.util.VersionContext;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@Slf4j
public class CalendarServiceImpl implements CalendarService {
    private final CalendarClient calendarClient;
    private final AuthorizationService authorizationService;
    private final ObjectMapper objectMapper;

    public CalendarServiceImpl(CalendarClient calendarClient,
                               AuthorizationService authorizationService,
                               ObjectMapper objectMapper) {
        this.calendarClient = calendarClient;
        this.authorizationService = authorizationService;
        this.objectMapper = objectMapper;
    }

    @Override
    public JsonNode createCalendar(CreateCalendarRequest request) {
        String accessKey = authorizationService.getAccessToken(AppKeyContext.getAppKey());
        String version = VersionContext.getVersion();
        GoHighLevelCreateCalendarRequest goHighLevelCreateCalendarRequest = GoHighLevelCreateCalendarRequest.fromRequest(request, objectMapper);
        return calendarClient.createCalendar(accessKey, version, goHighLevelCreateCalendarRequest);
    }

    @Override
    public JsonNode getCalendars(Map<String, Object> queryParams) {
        String accessKey = authorizationService.getAccessToken(AppKeyContext.getAppKey());
        String version = VersionContext.getVersion();
        queryParams.put("locationId", LocationContext.getLocationId());
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
