package org.example.loficonnect.serviceImpl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.example.loficonnect.dto.mapper.opportunity.GoHighLevelOpportunityCreateRequest;
import org.example.loficonnect.dto.mapper.opportunity.GoHighLevelOpportunityStatusUpdateRequest;
import org.example.loficonnect.dto.mapper.opportunity.GoHighLevelOpportunityUpdateRequest;
import org.example.loficonnect.dto.mapper.opportunity.GoHighLevelOpportunityUpsertRequest;
import org.example.loficonnect.dto.request.opportunity.OpportunityCreateRequest;
import org.example.loficonnect.dto.request.opportunity.OpportunityStatusUpdateRequest;
import org.example.loficonnect.dto.request.opportunity.OpportunityUpdateRequest;
import org.example.loficonnect.dto.request.opportunity.OpportunityUpsertRequest;
import org.example.loficonnect.feignclients.OpportunityClient;
import org.example.loficonnect.service.AuthorizationService;
import org.example.loficonnect.service.OpportunityService;
import org.example.loficonnect.util.AppKeyContext;
import org.example.loficonnect.util.VersionContext;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class OpportunityServiceImpl implements OpportunityService {

    private final OpportunityClient opportunityClient;
    private final AuthorizationService authorizationService;
    private final ObjectMapper objectMapper;

    public OpportunityServiceImpl(OpportunityClient opportunityClient,
                                  AuthorizationService authorizationService,
                                  ObjectMapper objectMapper) {
        this.opportunityClient = opportunityClient;
        this.authorizationService = authorizationService;
        this.objectMapper = objectMapper;
    }

    @Override
    public JsonNode getOpportunityById(String opportunityId) {
        String accessKey = authorizationService.getAccessToken(AppKeyContext.getAppKey());
        String version = VersionContext.getVersion();
        return opportunityClient.getOpportunityById(accessKey, version, opportunityId);
    }

    @Override
    public JsonNode deleteOpportunityById(String opportunityId) {
        String accessKey = authorizationService.getAccessToken(AppKeyContext.getAppKey());
        String version = VersionContext.getVersion();
        return opportunityClient.deleteOpportunityById(accessKey, version, opportunityId);
    }

    @Override
    public JsonNode updateOpportunityById(String id, OpportunityUpdateRequest request) {
        String accessKey = authorizationService.getAccessToken(AppKeyContext.getAppKey());
        String version = VersionContext.getVersion();
        GoHighLevelOpportunityUpdateRequest ghlRequest = GoHighLevelOpportunityUpdateRequest.fromRequest(request, objectMapper);
        return opportunityClient.updateOpportunityById(accessKey, version, id, ghlRequest);
    }

    @Override
    public JsonNode updateOpportunityStatus(String id, OpportunityStatusUpdateRequest request) {
        String accessKey = authorizationService.getAccessToken(AppKeyContext.getAppKey());
        String version = VersionContext.getVersion();
        GoHighLevelOpportunityStatusUpdateRequest ghlRequest =
                GoHighLevelOpportunityStatusUpdateRequest.fromRequest(request, objectMapper);
        return opportunityClient.updateOpportunityStatus(accessKey, version, id, ghlRequest);
    }

    @Override
    public JsonNode upsertOpportunity(OpportunityUpsertRequest request) {
        String accessKey = authorizationService.getAccessToken(AppKeyContext.getAppKey());
        String version = VersionContext.getVersion();
        GoHighLevelOpportunityUpsertRequest ghlRequest = GoHighLevelOpportunityUpsertRequest.fromRequest(request, objectMapper);
        return opportunityClient.upsertOpportunity(accessKey, version, ghlRequest);
    }

    @Override
    public JsonNode createOpportunity(OpportunityCreateRequest request) {
        String accessKey = authorizationService.getAccessToken(AppKeyContext.getAppKey());
        String version = VersionContext.getVersion();
        GoHighLevelOpportunityCreateRequest ghlRequest = GoHighLevelOpportunityCreateRequest.fromRequest(request, objectMapper);
        return opportunityClient.createOpportunity(accessKey, version, ghlRequest);
    }

}
