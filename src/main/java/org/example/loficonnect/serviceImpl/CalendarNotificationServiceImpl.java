package org.example.loficonnect.serviceImpl;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import org.example.loficonnect.dto.mapper.calendarnotifications.GoHighLevelCalendarNotificationUpdateRequest;
import org.example.loficonnect.dto.mapper.calendarnotifications.GoHighLevelNotificationCreateRequest;
import org.example.loficonnect.dto.request.calendarnotifications.CalendarNotificationUpdateRequest;
import org.example.loficonnect.dto.request.calendarnotifications.NotificationCreateRequest;
import org.example.loficonnect.feignclients.CalendarNotificationClient;
import org.example.loficonnect.service.AuthorizationService;
import org.example.loficonnect.service.CalendarNotificationService;
import org.example.loficonnect.util.AppKeyContext;
import org.example.loficonnect.util.VersionContext;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class CalendarNotificationServiceImpl implements CalendarNotificationService {
    private final CalendarNotificationClient calendarNotificationClient;
    private final AuthorizationService authorizationService;

    public CalendarNotificationServiceImpl(CalendarNotificationClient calendarNotificationClient, AuthorizationService authorizationService) {
        this.calendarNotificationClient = calendarNotificationClient;
        this.authorizationService = authorizationService;
    }

    @Override
    public JsonNode createCalendarNotification(String calendarId, List<NotificationCreateRequest> request) {
        String accessKey = authorizationService.getAccessToken(AppKeyContext.getAppKey());
        String version = VersionContext.getVersion();

        List<GoHighLevelNotificationCreateRequest> ghlRequests = request.stream()
                .map(GoHighLevelNotificationCreateRequest::fromRequest)
                .toList();

        return calendarNotificationClient.createNotification(accessKey, version, calendarId, ghlRequests);
    }

    @Override
    public JsonNode updateCalendarNotification(String calendarId, String notificationId, CalendarNotificationUpdateRequest request) {
        String accessKey = authorizationService.getAccessToken(AppKeyContext.getAppKey());
        String version = VersionContext.getVersion();

        GoHighLevelCalendarNotificationUpdateRequest ghlRequest =
                GoHighLevelCalendarNotificationUpdateRequest.fromRequest(request);

        return calendarNotificationClient.updateCalendarNotification(accessKey, version, calendarId, notificationId, ghlRequest);
    }

    @Override
    public JsonNode getCalendarNotification(String calendarId, String notificationId) {
        String accessKey = authorizationService.getAccessToken(AppKeyContext.getAppKey());
        String version = VersionContext.getVersion();

        return calendarNotificationClient.getCalendarNotification(accessKey, version, calendarId, notificationId);
    }

    @Override
    public JsonNode getCalendarNotifications(String calendarId, Map<String, Object> queryParams) {
        String accessKey = authorizationService.getAccessToken(AppKeyContext.getAppKey());
        String version = VersionContext.getVersion();

        return calendarNotificationClient.getCalendarNotifications(accessKey, version, calendarId, queryParams);
    }

    @Override
    public JsonNode deleteCalendarNotification(String calendarId, String notificationId) {
        String accessKey = authorizationService.getAccessToken(AppKeyContext.getAppKey());
        String version = VersionContext.getVersion();

        return calendarNotificationClient.deleteCalendarNotification(accessKey, version, calendarId, notificationId);
    }


}
