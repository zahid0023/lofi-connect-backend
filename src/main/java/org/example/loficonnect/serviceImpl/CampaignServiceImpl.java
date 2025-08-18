package org.example.loficonnect.serviceImpl;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import org.example.loficonnect.feignclients.CampaignClient;
import org.example.loficonnect.service.AuthorizationService;
import org.example.loficonnect.service.CampaignService;
import org.example.loficonnect.util.AppKeyContext;
import org.example.loficonnect.util.VersionContext;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@Slf4j
public class CampaignServiceImpl implements CampaignService {
    private final AuthorizationService authorizationService;
    private final CampaignClient campaignClient;

    public CampaignServiceImpl(AuthorizationService authorizationService, CampaignClient campaignClient) {
        this.authorizationService = authorizationService;
        this.campaignClient = campaignClient;
    }

    @Override
    public JsonNode getCampaigns(Map<String, Object> queryParams) {
        String accessKey = authorizationService.getAccessToken(AppKeyContext.getAppKey());
        String version = VersionContext.getVersion();
        return campaignClient.getCampaigns(accessKey, version, queryParams);
    }

}
