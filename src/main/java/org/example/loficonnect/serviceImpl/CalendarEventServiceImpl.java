package org.example.loficonnect.serviceImpl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.example.loficonnect.dto.mapper.calendarevents.GoHighLevelBlockSlotCreateRequest;
import org.example.loficonnect.dto.mapper.calendarevents.GoHighLevelBlockSlotUpdateRequest;
import org.example.loficonnect.dto.request.calendarevents.BlockSlotCreateRequest;
import org.example.loficonnect.dto.mapper.calendarevents.GoHighLevelAppointmentCreateRequest;
import org.example.loficonnect.dto.mapper.calendarevents.GoHighLevelAppointmentUpdateRequest;
import org.example.loficonnect.dto.request.calendarevents.AppointmentCreateRequest;
import org.example.loficonnect.dto.request.calendarevents.AppointmentUpdateRequest;
import org.example.loficonnect.dto.request.calendarevents.BlockSlotUpdateRequest;
import org.example.loficonnect.feignclients.CalendarEventClient;
import org.example.loficonnect.service.AuthorizationService;
import org.example.loficonnect.service.CalendarEventService;
import org.example.loficonnect.util.AppKeyContext;
import org.example.loficonnect.util.VersionContext;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@Slf4j
public class CalendarEventServiceImpl implements CalendarEventService {
    private final CalendarEventClient calendarEventClient;
    private final AuthorizationService authorizationService;
    private final ObjectMapper objectMapper;

    public CalendarEventServiceImpl(CalendarEventClient calendarEventClient,
                                    AuthorizationService authorizationService,
                                    ObjectMapper objectMapper) {
        this.calendarEventClient = calendarEventClient;
        this.authorizationService = authorizationService;
        this.objectMapper = objectMapper;
    }

    @Override
    public JsonNode createCalendarEvent(AppointmentCreateRequest request) {
        String accessKey = authorizationService.getAccessToken(AppKeyContext.getAppKey());
        String version = VersionContext.getVersion();
        GoHighLevelAppointmentCreateRequest goHighLevelAppointmentCreateRequest = GoHighLevelAppointmentCreateRequest.fromRequest(request, objectMapper);
        return calendarEventClient.createAppointment(accessKey, version, goHighLevelAppointmentCreateRequest);
    }

    @Override
    public JsonNode updateCalendarEvent(String appointmentId, AppointmentUpdateRequest request) {
        String accessKey = authorizationService.getAccessToken(AppKeyContext.getAppKey());
        String version = VersionContext.getVersion();
        GoHighLevelAppointmentUpdateRequest ghlRequest = GoHighLevelAppointmentUpdateRequest.fromRequest(request, objectMapper);
        return calendarEventClient.updateAppointment(accessKey, version, appointmentId, ghlRequest);
    }

    @Override
    public JsonNode getCalendarEvent(String appointmentId) {
        String accessKey = authorizationService.getAccessToken(AppKeyContext.getAppKey());
        String version = VersionContext.getVersion();
        return calendarEventClient.getAppointment(accessKey, version, appointmentId);
    }

    @Override
    public JsonNode getCalendarEvents(Map<String, Object> queryParams) {
        String accessKey = authorizationService.getAccessToken(AppKeyContext.getAppKey());
        String version = VersionContext.getVersion();
        return calendarEventClient.getCalendarEvents(accessKey, version, queryParams);
    }

    @Override
    public JsonNode getBlockedSlots(Map<String, Object> queryParams) {
        String accessKey = authorizationService.getAccessToken(AppKeyContext.getAppKey());
        String version = VersionContext.getVersion();
        return calendarEventClient.getBlockedSlots(accessKey, version, queryParams);
    }

    @Override
    public JsonNode createBlockSlot(BlockSlotCreateRequest request) {
        String accessKey = authorizationService.getAccessToken(AppKeyContext.getAppKey());
        String version = VersionContext.getVersion();
        GoHighLevelBlockSlotCreateRequest ghlRequest = GoHighLevelBlockSlotCreateRequest.fromRequest(request, objectMapper);
        log.info("Create block slot request: {}", ghlRequest);
        return calendarEventClient.createBlockSlot(accessKey, version, ghlRequest);
    }

    @Override
    public JsonNode updateBlockSlot(String eventId, BlockSlotUpdateRequest request) {
        String accessKey = authorizationService.getAccessToken(AppKeyContext.getAppKey());
        String version = VersionContext.getVersion();
        GoHighLevelBlockSlotUpdateRequest ghlRequest = GoHighLevelBlockSlotUpdateRequest.fromRequest(request, objectMapper);
        return calendarEventClient.updateBlockSlot(accessKey, version, eventId, ghlRequest);
    }

    @Override
    public JsonNode deleteCalendarEvent(String eventId) {
        String accessKey = authorizationService.getAccessToken(AppKeyContext.getAppKey());
        String version = VersionContext.getVersion();
        return calendarEventClient.deleteEvent(accessKey, version, eventId);
    }


}
