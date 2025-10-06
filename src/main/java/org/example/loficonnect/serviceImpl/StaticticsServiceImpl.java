package org.example.loficonnect.serviceImpl;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import org.example.loficonnect.dto.mapper.statictics.GoHighLevelStaticticsRequest;
import org.example.loficonnect.dto.request.statictics.StaticticsRequest;
import org.example.loficonnect.feignclients.StaticticsClient;
import org.example.loficonnect.service.AuthorizationService;
import org.example.loficonnect.service.StaticticsService;
import org.example.loficonnect.util.AppKeyContext;
import org.example.loficonnect.util.VersionContext;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class StaticticsServiceImpl implements StaticticsService {

    private final StaticticsClient staticticsClient;
    private final AuthorizationService authorizationService;

    public StaticticsServiceImpl(StaticticsClient staticticsClient, AuthorizationService authorizationService) {
        this.staticticsClient = staticticsClient;
        this.authorizationService = authorizationService;
    }

    @Override
    public JsonNode getStatistics(String locationId, StaticticsRequest request) {
        String accessToken = authorizationService.getAccessToken(AppKeyContext.getAppKey());
        String version = VersionContext.getVersion();
        GoHighLevelStaticticsRequest ghlRequest = GoHighLevelStaticticsRequest.fromRequest(request);
        return staticticsClient.getStatistics(accessToken, version, locationId, ghlRequest);
    }
}
