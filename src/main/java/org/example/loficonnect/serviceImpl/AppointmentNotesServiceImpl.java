package org.example.loficonnect.serviceImpl;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import org.example.loficonnect.dto.mapper.appointmentnotes.GoHighLevelAppointmentNoteCreateRequest;
import org.example.loficonnect.dto.mapper.appointmentnotes.GoHighLevelAppointmentNoteUpdateRequest;
import org.example.loficonnect.dto.request.appointmentnotes.AppointmentNoteCreateRequest;
import org.example.loficonnect.dto.request.appointmentnotes.AppointmentNoteUpdateRequest;
import org.example.loficonnect.feignclients.AppointmentNotesClient;
import org.example.loficonnect.service.AppointmentNotesService;
import org.example.loficonnect.service.AuthorizationService;
import org.example.loficonnect.util.AppKeyContext;
import org.example.loficonnect.util.VersionContext;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@Slf4j
public class AppointmentNotesServiceImpl implements AppointmentNotesService {
    private final AppointmentNotesClient appointmentNotesClient;
    private final AuthorizationService authorizationService;

    public AppointmentNotesServiceImpl(AppointmentNotesClient appointmentNotesClient, AuthorizationService authorizationService) {
        this.appointmentNotesClient = appointmentNotesClient;
        this.authorizationService = authorizationService;
    }

    @Override
    public JsonNode getAppointmentNotes(String appointmentId, Map<String, Object> queryParams) {
        String accessKey = authorizationService.getAccessToken(AppKeyContext.getAppKey());
        String version = VersionContext.getVersion();
        return appointmentNotesClient.getAppointmentNotes(accessKey, version, appointmentId, queryParams);
    }

    @Override
    public JsonNode createAppointmentNote(String appointmentId, AppointmentNoteCreateRequest request) {
        String accessKey = authorizationService.getAccessToken(AppKeyContext.getAppKey());
        String version = VersionContext.getVersion();
        GoHighLevelAppointmentNoteCreateRequest ghlRequest = GoHighLevelAppointmentNoteCreateRequest.fromRequest(request);
        return appointmentNotesClient.createAppointmentNote(accessKey, version, appointmentId, ghlRequest);
    }

    @Override
    public JsonNode updateAppointmentNote(String appointmentId, String noteId, AppointmentNoteUpdateRequest request) {
        String accessKey = authorizationService.getAccessToken(AppKeyContext.getAppKey());
        String version = VersionContext.getVersion();
        GoHighLevelAppointmentNoteUpdateRequest ghlRequest = GoHighLevelAppointmentNoteUpdateRequest.fromRequest(request);
        return appointmentNotesClient.updateAppointmentNote(accessKey, version, appointmentId, noteId, ghlRequest);
    }

    @Override
    public JsonNode deleteAppointmentNote(String appointmentId, String noteId) {
        String accessKey = authorizationService.getAccessToken(AppKeyContext.getAppKey());
        String version = VersionContext.getVersion();
        return appointmentNotesClient.deleteAppointmentNote(accessKey, version, appointmentId, noteId);
    }


}
