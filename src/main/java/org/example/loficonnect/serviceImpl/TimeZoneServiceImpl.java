package org.example.loficonnect.serviceImpl;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import org.example.loficonnect.feignclients.TimeZoneClient;
import org.example.loficonnect.service.AuthorizationService;
import org.example.loficonnect.service.TimeZoneService;
import org.example.loficonnect.util.AppKeyContext;
import org.example.loficonnect.util.VersionContext;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class TimeZoneServiceImpl implements TimeZoneService {
    private final AuthorizationService authorizationService;
    private final TimeZoneClient timeZoneClient;

    public TimeZoneServiceImpl(AuthorizationService authorizationService, TimeZoneClient timeZoneClient) {
        this.authorizationService = authorizationService;
        this.timeZoneClient = timeZoneClient;
    }

    @Override
    public JsonNode getTimezones(String locationId) {
        String accessKey = authorizationService.getAccessToken(AppKeyContext.getAppKey());
        String version = VersionContext.getVersion();
        return timeZoneClient.getTimezones(accessKey, version, locationId);
    }

}
