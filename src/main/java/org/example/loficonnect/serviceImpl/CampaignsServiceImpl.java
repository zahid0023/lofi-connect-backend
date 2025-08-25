package org.example.loficonnect.serviceImpl;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import org.example.loficonnect.dto.mapper.campaigns.ContactCampaignAssignRequest;
import org.example.loficonnect.dto.request.campaigns.GoHighLevelContactCampaignAssignRequest;
import org.example.loficonnect.feignclients.CampaignsClient;
import org.example.loficonnect.service.AuthorizationService;
import org.example.loficonnect.service.CampaignsService;
import org.example.loficonnect.util.AppKeyContext;
import org.example.loficonnect.util.VersionContext;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@Slf4j
public class CampaignsServiceImpl implements CampaignsService {
    private final AuthorizationService authorizationService;
    private final CampaignsClient campaignsClient;

    public CampaignsServiceImpl(AuthorizationService authorizationService, CampaignsClient campaignsClient) {
        this.authorizationService = authorizationService;
        this.campaignsClient = campaignsClient;
    }

    @Override
    public JsonNode assignContactToCampaign(String contactId, String campaignId, ContactCampaignAssignRequest request) {
        String accessKey = authorizationService.getAccessToken(AppKeyContext.getAppKey());
        String version = VersionContext.getVersion();
        GoHighLevelContactCampaignAssignRequest ghlRequest = GoHighLevelContactCampaignAssignRequest.fromRequest(request);
        return campaignsClient.assignContactToCampaign(accessKey, version, contactId, campaignId, ghlRequest);
    }

    @Override
    public JsonNode removeContactFromCampaign(String contactId, String campaignId, Map<String, Object> queryParams) {
        String accessKey = authorizationService.getAccessToken(AppKeyContext.getAppKey());
        String version = VersionContext.getVersion();
        return campaignsClient.removeContactFromCampaign(accessKey, version, contactId, campaignId, queryParams);
    }
    @Override
    public JsonNode removeAllCampaignsFromContact(String contactId, Map<String, Object> queryParams) {
        String accessKey = authorizationService.getAccessToken(AppKeyContext.getAppKey());
        String version = VersionContext.getVersion();
        return campaignsClient.removeAllCampaignsFromContact(accessKey, version, contactId, queryParams);
    }



}
