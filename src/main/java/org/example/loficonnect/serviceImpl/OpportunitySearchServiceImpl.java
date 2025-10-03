package org.example.loficonnect.serviceImpl;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import org.example.loficonnect.feignclients.OpportunitySearchClient;
import org.example.loficonnect.service.AuthorizationService;
import org.example.loficonnect.service.OpportunitySearchService;
import org.example.loficonnect.util.AppKeyContext;
import org.example.loficonnect.util.VersionContext;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@Service
public class OpportunitySearchServiceImpl implements OpportunitySearchService {

    private final OpportunitySearchClient opportunitySearchClient;
    private final AuthorizationService authorizationService;

    public OpportunitySearchServiceImpl(OpportunitySearchClient opportunitySearchClient, AuthorizationService authorizationService) {
        this.opportunitySearchClient = opportunitySearchClient;
        this.authorizationService = authorizationService;
    }

    @Override
    public JsonNode searchOpportunities(Map<String, Object> queryParams) {
        String accessKey = authorizationService.getAccessToken(AppKeyContext.getAppKey());
        String version = VersionContext.getVersion();
        return opportunitySearchClient.searchOpportunities(accessKey, version, queryParams);
    }
}
