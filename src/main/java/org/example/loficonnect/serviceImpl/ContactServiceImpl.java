package org.example.loficonnect.serviceImpl;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import org.example.loficonnect.dto.mapper.contact.GoHighLevelContactCreateRequest;
import org.example.loficonnect.dto.mapper.contact.GoHighLevelContactUpdateRequest;
import org.example.loficonnect.dto.request.contact.ContactCreateRequest;
import org.example.loficonnect.dto.request.contact.ContactUpdateRequest;
import org.example.loficonnect.feignclients.ContactClient;
import org.example.loficonnect.service.AuthorizationService;
import org.example.loficonnect.service.ContactService;
import org.example.loficonnect.util.AppKeyContext;
import org.example.loficonnect.util.VersionContext;
import org.springframework.stereotype.Service;


@Service
@Slf4j
public class ContactServiceImpl implements ContactService {
    private final AuthorizationService authorizationService;
    private final ContactClient contactClient;

    public ContactServiceImpl(AuthorizationService authorizationService, ContactClient contactClient) {
        this.authorizationService = authorizationService;
        this.contactClient = contactClient;
    }

    @Override
    public JsonNode getContact(String contactId) {
        String accessKey = authorizationService.getAccessToken(AppKeyContext.getAppKey());
        String version = VersionContext.getVersion();
        return contactClient.getContact(accessKey, version, contactId);
    }

    @Override
    public JsonNode updateContact(String contactId, ContactUpdateRequest request) {
        String accessKey = authorizationService.getAccessToken(AppKeyContext.getAppKey());
        String version = VersionContext.getVersion();
        GoHighLevelContactUpdateRequest ghlRequest = GoHighLevelContactUpdateRequest.fromRequest(request);
        return contactClient.updateContact(accessKey, version, contactId, ghlRequest);
    }

    @Override
    public JsonNode deleteContact(String contactId) {
        String accessKey = authorizationService.getAccessToken(AppKeyContext.getAppKey());
        String version = VersionContext.getVersion();
        return contactClient.deleteContact(accessKey, version, contactId);
    }

    @Override
    public JsonNode getBusinessContacts(String businessId) {
        String accessKey = authorizationService.getAccessToken(AppKeyContext.getAppKey());
        String version = VersionContext.getVersion();
        return contactClient.getBusinessContacts(accessKey, version, businessId);
    }

    @Override
    public JsonNode createContact(ContactCreateRequest request) {
        String accessKey = authorizationService.getAccessToken(AppKeyContext.getAppKey());
        String version = VersionContext.getVersion();
        GoHighLevelContactCreateRequest ghlRequest = GoHighLevelContactCreateRequest.fromRequest(request);
        return contactClient.createContact(accessKey, version, ghlRequest);
    }


}
