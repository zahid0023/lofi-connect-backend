package org.example.loficonnect.serviceImpl;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import org.example.loficonnect.feignclients.AppointmentsClient;
import org.example.loficonnect.service.AppointmentsService;
import org.example.loficonnect.service.AuthorizationService;
import org.example.loficonnect.util.AppKeyContext;
import org.example.loficonnect.util.VersionContext;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@Slf4j
public class AppointmentsServiceImpl implements AppointmentsService {
    private final AuthorizationService authorizationService;
    private final AppointmentsClient appointmentsClient;

    public AppointmentsServiceImpl(AuthorizationService authorizationService, AppointmentsClient appointmentsClient) {
        this.authorizationService = authorizationService;
        this.appointmentsClient = appointmentsClient;
    }

    @Override
    public JsonNode getContactAppointments(String contactId, Map<String, Object> queryParams) {
        String accessKey = authorizationService.getAccessToken(AppKeyContext.getAppKey());
        String version = VersionContext.getVersion();
        return appointmentsClient.getContactAppointments(accessKey, version, contactId, queryParams);
    }

}
