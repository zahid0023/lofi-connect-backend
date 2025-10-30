package org.example.loficonnect.serviceImpl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.example.loficonnect.dto.mapper.notes.GoHighLevelContactNoteCreateRequest;
import org.example.loficonnect.dto.mapper.notes.GoHighLevelContactNoteUpdateRequest;
import org.example.loficonnect.dto.request.notes.ContactNoteCreateRequest;
import org.example.loficonnect.dto.request.notes.ContactNoteUpdateRequest;
import org.example.loficonnect.feignclients.NotesClient;
import org.example.loficonnect.service.AuthorizationService;
import org.example.loficonnect.service.NotesService;
import org.example.loficonnect.util.AppKeyContext;
import org.example.loficonnect.util.VersionContext;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@Slf4j
public class NotesServiceImpl implements NotesService {
    private final AuthorizationService authorizationService;
    private final NotesClient notesClient;
    private final ObjectMapper objectMapper;

    public NotesServiceImpl(AuthorizationService authorizationService,
                            NotesClient notesClient,
                            ObjectMapper objectMapper) {
        this.authorizationService = authorizationService;
        this.notesClient = notesClient;
        this.objectMapper = objectMapper;
    }

    @Override
    public JsonNode getContactNotes(String contactId, Map<String, Object> queryParams) {
        String accessKey = authorizationService.getAccessToken(AppKeyContext.getAppKey());
        String version = VersionContext.getVersion();
        return notesClient.getContactNotes(accessKey, version, contactId, queryParams);
    }

    @Override
    public JsonNode createContactNote(String contactId, ContactNoteCreateRequest request) {
        String accessKey = authorizationService.getAccessToken(AppKeyContext.getAppKey());
        String version = VersionContext.getVersion();
        GoHighLevelContactNoteCreateRequest ghlRequest = GoHighLevelContactNoteCreateRequest.fromRequest(request, objectMapper);
        return notesClient.createContactNote(accessKey, version, contactId, ghlRequest);
    }

    @Override
    public JsonNode getContactNoteById(String contactId, String noteId, Map<String, Object> queryParams) {
        String accessKey = authorizationService.getAccessToken(AppKeyContext.getAppKey());
        String version = VersionContext.getVersion();
        return notesClient.getContactNoteById(accessKey, version, contactId, noteId, queryParams);
    }

    @Override
    public JsonNode updateContactNote(String contactId, String noteId, ContactNoteUpdateRequest request) {
        String accessKey = authorizationService.getAccessToken(AppKeyContext.getAppKey());
        String version = VersionContext.getVersion();
        GoHighLevelContactNoteUpdateRequest ghlRequest = GoHighLevelContactNoteUpdateRequest.fromRequest(request, objectMapper);
        return notesClient.updateContactNote(accessKey, version, contactId, noteId, ghlRequest);
    }

    @Override
    public JsonNode deleteContactNote(String contactId, String noteId, Map<String, Object> queryParams) {
        String accessKey = authorizationService.getAccessToken(AppKeyContext.getAppKey());
        String version = VersionContext.getVersion();
        return notesClient.deleteContactNote(accessKey, version, contactId, noteId, queryParams);
    }
}
