package org.example.loficonnect.serviceImpl;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import org.example.loficonnect.dto.mapper.triggerlinks.GoHighLevelLinkCreateRequest;
import org.example.loficonnect.dto.mapper.triggerlinks.GoHighLevelLinkUpdateRequest;
import org.example.loficonnect.dto.request.triggerlinks.LinkCreateRequest;
import org.example.loficonnect.dto.request.triggerlinks.LinkUpdateRequest;
import org.example.loficonnect.feignclients.TriggerLinksClient;
import org.example.loficonnect.service.AuthorizationService;
import org.example.loficonnect.service.TriggerLinkService;
import org.example.loficonnect.util.AppKeyContext;
import org.example.loficonnect.util.VersionContext;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@Slf4j
public class TriggerLinkServiceImpl implements TriggerLinkService {

    private final TriggerLinksClient triggerLinksClient;
    private final AuthorizationService authorizationService;

    public TriggerLinkServiceImpl(TriggerLinksClient triggerLinksClient, AuthorizationService authorizationService) {
        this.triggerLinksClient = triggerLinksClient;
        this.authorizationService = authorizationService;
    }

    @Override
    public JsonNode updateLink(String linkId, LinkUpdateRequest request) {
        String accessKey = authorizationService.getAccessToken(AppKeyContext.getAppKey());
        String version = VersionContext.getVersion();
        GoHighLevelLinkUpdateRequest ghlRequest = GoHighLevelLinkUpdateRequest.fromRequest(request);
        return triggerLinksClient.updateLink(accessKey, version, linkId, ghlRequest);
    }

    @Override
    public JsonNode deleteLink(String linkId) {
        String accessKey = authorizationService.getAccessToken(AppKeyContext.getAppKey());
        String version = VersionContext.getVersion();
        return triggerLinksClient.deleteLink(accessKey, version, linkId);
    }

    @Override
    public JsonNode getLinks(Map<String, Object> queryParams) {
        String accessKey = authorizationService.getAccessToken(AppKeyContext.getAppKey());
        String version = VersionContext.getVersion();
        return triggerLinksClient.getLinks(accessKey, version, queryParams);
    }

    @Override
    public JsonNode createLink(LinkCreateRequest request) {
        String accessKey = authorizationService.getAccessToken(AppKeyContext.getAppKey());
        String version = VersionContext.getVersion();
        GoHighLevelLinkCreateRequest ghlRequest = GoHighLevelLinkCreateRequest.fromRequest(request);
        return triggerLinksClient.createLink(accessKey, version, ghlRequest);
    }
}
