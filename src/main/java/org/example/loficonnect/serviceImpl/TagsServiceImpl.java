package org.example.loficonnect.serviceImpl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.example.loficonnect.dto.mapper.tags.GoHighLevelContactTagCreateRequest;
import org.example.loficonnect.dto.mapper.tags.GoHighLevelContactTagDeleteRequest;
import org.example.loficonnect.dto.mapper.tags.GoHighLevelTagCreateRequest;
import org.example.loficonnect.dto.mapper.tags.GoHighLevelTagUpdateRequest;
import org.example.loficonnect.dto.request.tags.ContactTagsAddRequest;
import org.example.loficonnect.dto.request.tags.ContactTagsRemoveRequest;
import org.example.loficonnect.dto.request.tags.TagCreateRequest;
import org.example.loficonnect.dto.request.tags.TagUpdateRequest;
import org.example.loficonnect.feignclients.TagsClient;
import org.example.loficonnect.service.AuthorizationService;
import org.example.loficonnect.service.TagsService;
import org.example.loficonnect.util.AppKeyContext;
import org.example.loficonnect.util.LocationContext;
import org.example.loficonnect.util.VersionContext;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class TagsServiceImpl implements TagsService {
    private final AuthorizationService authorizationService;
    private final TagsClient tagsClient;
    private final ObjectMapper objectMapper;

    public TagsServiceImpl(AuthorizationService authorizationService,
                           TagsClient tagsClient,
                           ObjectMapper objectMapper) {
        this.authorizationService = authorizationService;
        this.tagsClient = tagsClient;
        this.objectMapper = objectMapper;
    }

    @Override
    public JsonNode createContactTags(String contactId, ContactTagsAddRequest request) {
        String accessKey = authorizationService.getAccessToken(AppKeyContext.getAppKey());
        String version = VersionContext.getVersion();
        GoHighLevelContactTagCreateRequest ghlRequest = GoHighLevelContactTagCreateRequest.fromRequest(request, objectMapper);
        return tagsClient.createContactTags(accessKey, version, contactId, ghlRequest);
    }

    @Override
    public JsonNode deleteContactTags(String contactId, ContactTagsRemoveRequest request) {
        String accessKey = authorizationService.getAccessToken(AppKeyContext.getAppKey());
        String version = VersionContext.getVersion();
        GoHighLevelContactTagDeleteRequest ghlRequest = GoHighLevelContactTagDeleteRequest.fromRequest(request, objectMapper);
        return tagsClient.deleteContactTags(accessKey, version, contactId, ghlRequest);
    }

    @Override
    public JsonNode getLocationTags() {
        String accessKey = authorizationService.getAccessToken(AppKeyContext.getAppKey());
        String version = VersionContext.getVersion();
        return tagsClient.getLocationTags(accessKey, version, LocationContext.getLocationId());
    }

    @Override
    public JsonNode createLocationTag(TagCreateRequest request) {
        String accessKey = authorizationService.getAccessToken(AppKeyContext.getAppKey());
        String version = VersionContext.getVersion();
        GoHighLevelTagCreateRequest ghlRequest = GoHighLevelTagCreateRequest.fromRequest(request, objectMapper);
        return tagsClient.createLocationTag(accessKey, version, LocationContext.getLocationId(), ghlRequest);
    }

    @Override
    public JsonNode getTagById(String tagId) {
        String accessKey = authorizationService.getAccessToken(AppKeyContext.getAppKey());
        String version = VersionContext.getVersion();
        return tagsClient.getTagById(accessKey, version, LocationContext.getLocationId(), tagId);
    }

    @Override
    public JsonNode updateTag(String tagId, TagUpdateRequest request) {
        String accessKey = authorizationService.getAccessToken(AppKeyContext.getAppKey());
        String version = VersionContext.getVersion();

        GoHighLevelTagUpdateRequest ghlRequest = GoHighLevelTagUpdateRequest.fromRequest(request, objectMapper);

        return tagsClient.updateTag(accessKey, version, LocationContext.getLocationId(), tagId, ghlRequest);
    }

    @Override
    public JsonNode deleteTag(String tagId) {
        String accessKey = authorizationService.getAccessToken(AppKeyContext.getAppKey());
        String version = VersionContext.getVersion();
        return tagsClient.deleteTag(accessKey, version, LocationContext.getLocationId(), tagId);
    }
}
