package org.example.loficonnect.serviceImpl;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import org.example.loficonnect.dto.mapper.tags.GoHighLevelContactTagCreateRequest;
import org.example.loficonnect.dto.mapper.tags.GoHighLevelContactTagDeleteRequest;
import org.example.loficonnect.dto.mapper.tags.GoHighLevelTagCreateRequest;
import org.example.loficonnect.dto.mapper.tags.GoHighLevelTagUpdateRequest;
import org.example.loficonnect.dto.request.tags.ContactTagCreateRequest;
import org.example.loficonnect.dto.request.tags.ContactTagDeleteRequest;
import org.example.loficonnect.dto.request.tags.TagCreateRequest;
import org.example.loficonnect.dto.request.tags.TagUpdateRequest;
import org.example.loficonnect.feignclients.TagsClient;
import org.example.loficonnect.service.AuthorizationService;
import org.example.loficonnect.service.TagsService;
import org.example.loficonnect.util.AppKeyContext;
import org.example.loficonnect.util.VersionContext;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class TagsServiceImpl implements TagsService {
    private final AuthorizationService authorizationService;
    private final TagsClient tagsClient;

    public TagsServiceImpl(AuthorizationService authorizationService, TagsClient tagsClient) {
        this.authorizationService = authorizationService;
        this.tagsClient = tagsClient;
    }

    @Override
    public JsonNode createContactTags(String contactId, ContactTagCreateRequest request) {
        String accessKey = authorizationService.getAccessToken(AppKeyContext.getAppKey());
        String version = VersionContext.getVersion();
        GoHighLevelContactTagCreateRequest ghlRequest = GoHighLevelContactTagCreateRequest.fromRequest(request);
        return tagsClient.createContactTags(accessKey, version, contactId, ghlRequest);
    }

    @Override
    public JsonNode deleteContactTags(String contactId, ContactTagDeleteRequest request) {
        String accessKey = authorizationService.getAccessToken(AppKeyContext.getAppKey());
        String version = VersionContext.getVersion();
        GoHighLevelContactTagDeleteRequest ghlRequest = GoHighLevelContactTagDeleteRequest.fromRequest(request);
        return tagsClient.deleteContactTags(accessKey, version, contactId, ghlRequest);
    }

    @Override
    public JsonNode getLocationTags(String locationId) {
        String accessKey = authorizationService.getAccessToken(AppKeyContext.getAppKey());
        String version = VersionContext.getVersion();
        return tagsClient.getLocationTags(accessKey, version, locationId);
    }

    @Override
    public JsonNode createLocationTag(String locationId, TagCreateRequest request) {
        String accessKey = authorizationService.getAccessToken(AppKeyContext.getAppKey());
        String version = VersionContext.getVersion();
        GoHighLevelTagCreateRequest ghlRequest = GoHighLevelTagCreateRequest.fromRequest(request);
        return tagsClient.createLocationTag(accessKey, version, locationId, ghlRequest);
    }

    @Override
    public JsonNode getTagById(String locationId, String tagId) {
        String accessKey = authorizationService.getAccessToken(AppKeyContext.getAppKey());
        String version = VersionContext.getVersion();
        return tagsClient.getTagById(accessKey, version, locationId, tagId);
    }

    @Override
    public JsonNode updateTag(String locationId, String tagId, TagUpdateRequest request) {
        String accessKey = authorizationService.getAccessToken(AppKeyContext.getAppKey());
        String version = VersionContext.getVersion();

        GoHighLevelTagUpdateRequest ghlRequest = GoHighLevelTagUpdateRequest.fromRequest(request);

        return tagsClient.updateTag(accessKey, version, locationId, tagId, ghlRequest);
    }

    @Override
    public JsonNode deleteTag(String locationId, String tagId) {
        String accessKey = authorizationService.getAccessToken(AppKeyContext.getAppKey());
        String version = VersionContext.getVersion();
        return tagsClient.deleteTag(accessKey, version, locationId, tagId);
    }


}
