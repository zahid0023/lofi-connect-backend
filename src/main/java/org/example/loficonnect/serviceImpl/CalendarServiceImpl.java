package org.example.loficonnect.serviceImpl;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import org.example.loficonnect.dto.mapper.GoHighLevelCalendarRequest;
import org.example.loficonnect.dto.request.calendar.CalendarRequest;
import org.example.loficonnect.feignclients.CalendarClient;
import org.example.loficonnect.service.AuthorizationService;
import org.example.loficonnect.service.CalendarService;
import org.example.loficonnect.util.AppKeyContext;
import org.example.loficonnect.util.VersionContext;
import org.springframework.stereotype.Service;

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
    public JsonNode createCalendar(CalendarRequest request) {
        log.info(authorizationService.getAccessToken(AppKeyContext.getAppKey()));
        log.info(VersionContext.getVersion());
        return calendarClient.createCalendar(authorizationService.getAccessToken(AppKeyContext.getAppKey()), VersionContext.getVersion(), GoHighLevelCalendarRequest.fromCalendarRequest(request));
    }
}
