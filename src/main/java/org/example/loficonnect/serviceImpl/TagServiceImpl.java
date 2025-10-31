package org.example.loficonnect.serviceImpl;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import org.example.loficonnect.dto.mapper.tag.GoHighLevelTagByIdsRequest;
import org.example.loficonnect.dto.request.tag.TagByIdsRequest;
import org.example.loficonnect.feignclients.TagClient;
import org.example.loficonnect.service.AuthorizationService;
import org.example.loficonnect.service.TagService;
import org.example.loficonnect.util.AppKeyContext;
import org.example.loficonnect.util.LocationContext;
import org.example.loficonnect.util.VersionContext;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@Slf4j
public class TagServiceImpl implements TagService {

    private final TagClient tagClient;
    private final AuthorizationService authorizationService;

    public TagServiceImpl(TagClient tagClient, AuthorizationService authorizationService) {
        this.tagClient = tagClient;
        this.authorizationService = authorizationService;
    }

    @Override
    public JsonNode getTags(Map<String, Object> queryParams) {
        String accessToken = authorizationService.getAccessToken(AppKeyContext.getAppKey());
        String version = VersionContext.getVersion();
        return tagClient.getTags(accessToken, version, LocationContext.getLocationId(), queryParams);
    }

    @Override
    public JsonNode getTagsByIds(TagByIdsRequest request) {
        String accessToken = authorizationService.getAccessToken(AppKeyContext.getAppKey());
        String version = VersionContext.getVersion();
        GoHighLevelTagByIdsRequest ghlRequest = GoHighLevelTagByIdsRequest.fromRequest(request);
        return tagClient.getTagsByIds(accessToken, version, LocationContext.getLocationId(), ghlRequest);
    }
}
