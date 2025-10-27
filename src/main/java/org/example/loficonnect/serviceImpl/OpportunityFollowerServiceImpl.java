package org.example.loficonnect.serviceImpl;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import org.example.loficonnect.dto.mapper.opportunityfollower.GoHighLevelOpportunityFollowerRequest;
import org.example.loficonnect.dto.mapper.opportunityfollower.GoHighLevelOpportunityRemoveFollowersRequest;
import org.example.loficonnect.dto.request.opportunityfollower.OpportunityFollowerRequest;
import org.example.loficonnect.dto.request.opportunityfollower.OpportunityRemoveFollowersRequest;
import org.example.loficonnect.feignclients.OpportunityFollowerClient;
import org.example.loficonnect.service.AuthorizationService;
import org.example.loficonnect.service.OpportunityFollowerService;
import org.example.loficonnect.util.AppKeyContext;
import org.example.loficonnect.util.VersionContext;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@Service
public class OpportunityFollowerServiceImpl implements OpportunityFollowerService {

    private final OpportunityFollowerClient opportunityFollowerClient;
    private final AuthorizationService authorizationService;

    public OpportunityFollowerServiceImpl(OpportunityFollowerClient opportunityFollowerClient, AuthorizationService authorizationService) {
        this.opportunityFollowerClient = opportunityFollowerClient;
        this.authorizationService = authorizationService;
    }

    @Override
    public JsonNode addFollowers(String opportunityId, OpportunityFollowerRequest request) {
        String accessKey = authorizationService.getAccessToken(AppKeyContext.getAppKey());
        String version = VersionContext.getVersion();
        GoHighLevelOpportunityFollowerRequest ghlRequest =
            GoHighLevelOpportunityFollowerRequest.fromRequest(request);

        return opportunityFollowerClient.addFollowers(accessKey, version, opportunityId, ghlRequest);
    }

    @Override
    public JsonNode removeFollowers(String opportunityId, OpportunityRemoveFollowersRequest request) {
        String accessKey = authorizationService.getAccessToken(AppKeyContext.getAppKey());
        String version = VersionContext.getVersion();
        GoHighLevelOpportunityRemoveFollowersRequest ghlRequest = GoHighLevelOpportunityRemoveFollowersRequest.fromRequest(request);
        return opportunityFollowerClient.removeFollowers(accessKey, version, opportunityId, ghlRequest);
    }

}
