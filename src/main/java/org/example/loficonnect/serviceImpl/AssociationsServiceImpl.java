package org.example.loficonnect.serviceImpl;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import org.example.loficonnect.dto.mapper.associations.GoHighLevelCreateAssociationRequest;
import org.example.loficonnect.dto.mapper.associations.GoHighLevelUpdateAssociationRequest;
import org.example.loficonnect.dto.request.associations.AssociationUpdateRequest;
import org.example.loficonnect.dto.request.associations.AssociationCreateRequest;
import org.example.loficonnect.feignclients.AssociationsClient;
import org.example.loficonnect.service.AssociationsService;
import org.example.loficonnect.service.AuthorizationService;
import org.example.loficonnect.util.AppKeyContext;
import org.example.loficonnect.util.VersionContext;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AssociationsServiceImpl implements AssociationsService {
    private final AssociationsClient associationsClient;
    private final AuthorizationService authorizationService;

    public AssociationsServiceImpl(AssociationsClient associationsClient, AuthorizationService authorizationService) {
        this.associationsClient = associationsClient;
        this.authorizationService = authorizationService;
    }

    @Override
    public JsonNode getAssociationKeyByName(String keyName, String locationId) {
        String accessKey = authorizationService.getAccessToken(AppKeyContext.getAppKey());
        String version = VersionContext.getVersion();
        return associationsClient.getAssociationKeyByName(accessKey, version, keyName, locationId);
    }

    @Override
    public JsonNode getAssociationByObjectKey(String objectKey, String locationId) {
        String accessKey = authorizationService.getAccessToken(AppKeyContext.getAppKey());
        String version = VersionContext.getVersion();
        return associationsClient.getAssociationByObjectKey(accessKey, version, objectKey, locationId);
    }

    @Override
    public JsonNode updateAssociation(String associationId, AssociationUpdateRequest request) {
        String accessKey = authorizationService.getAccessToken(AppKeyContext.getAppKey());
        String version = VersionContext.getVersion();
        GoHighLevelUpdateAssociationRequest ghlRequest = GoHighLevelUpdateAssociationRequest.fromRequest(request);
        return associationsClient.updateAssociation(accessKey, version, associationId, ghlRequest);
    }

    @Override
    public JsonNode deleteAssociation(String associationId) {
        String accessKey = authorizationService.getAccessToken(AppKeyContext.getAppKey());
        String version = VersionContext.getVersion();
        return associationsClient.deleteAssociation(accessKey, version, associationId);
    }

    @Override
    public JsonNode getAssociationById(String associationId) {
        String accessKey = authorizationService.getAccessToken(AppKeyContext.getAppKey());
        String version = VersionContext.getVersion();
        return associationsClient.getAssociationById(accessKey, version, associationId);
    }

    @Override
    public JsonNode createAssociation(AssociationCreateRequest request) {
        String accessKey = authorizationService.getAccessToken(AppKeyContext.getAppKey());
        String version = VersionContext.getVersion();
        GoHighLevelCreateAssociationRequest ghlRequest = GoHighLevelCreateAssociationRequest.fromRequest(request);
        return associationsClient.createAssociation(accessKey, version, ghlRequest);
    }

    @Override
    public JsonNode getAllAssociations(int limit, int skip, String locationId) {
        String accessKey = authorizationService.getAccessToken(AppKeyContext.getAppKey());
        String version = VersionContext.getVersion();
        return associationsClient.getAllAssociations(accessKey, version, limit, skip, locationId);
    }
}
