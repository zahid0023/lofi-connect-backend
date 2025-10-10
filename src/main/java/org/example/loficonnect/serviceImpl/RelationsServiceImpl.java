package org.example.loficonnect.serviceImpl;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import org.example.loficonnect.feignclients.RelationsClient;
import org.example.loficonnect.service.AuthorizationService;
import org.example.loficonnect.service.RelationsService;
import org.example.loficonnect.dto.request.relations.RelationCreateRequest;
import org.example.loficonnect.dto.mapper.relations.GoHighLevelCreateRelationRequest;
import org.example.loficonnect.util.AppKeyContext;
import org.example.loficonnect.util.VersionContext;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class RelationsServiceImpl implements RelationsService {

    private final RelationsClient relationsClient;
    private final AuthorizationService authorizationService;

    public RelationsServiceImpl(RelationsClient relationsClient, AuthorizationService authorizationService) {
        this.relationsClient = relationsClient;
        this.authorizationService = authorizationService;
    }

    @Override
    public JsonNode createRelation(RelationCreateRequest request) {
        String accessKey = authorizationService.getAccessToken(AppKeyContext.getAppKey());
        String version = VersionContext.getVersion();
        GoHighLevelCreateRelationRequest ghlRequest = GoHighLevelCreateRelationRequest.fromRequest(request);
        return relationsClient.createRelation(accessKey, version, ghlRequest);
    }

    @Override
    public JsonNode getAllRelationsByRecordId(String recordId, String[] associationIds, int limit, String locationId, int skip) {
        String accessKey = authorizationService.getAccessToken(AppKeyContext.getAppKey());
        String version = VersionContext.getVersion();
        return relationsClient.getAllRelationsByRecordId(accessKey, version, recordId, associationIds, limit, locationId, skip);
    }

    @Override
    public JsonNode deleteRelation(String relationId, String locationId) {
        String accessKey = authorizationService.getAccessToken(AppKeyContext.getAppKey());
        String version = VersionContext.getVersion();
        return relationsClient.deleteRelation(accessKey, version, relationId, locationId);
    }
}
