package org.example.loficonnect.serviceImpl;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import org.example.loficonnect.dto.mapper.post.GoHighLevelBulkPostDeleteRequest;
import org.example.loficonnect.dto.mapper.post.GoHighLevelPostCreateRequest;
import org.example.loficonnect.dto.mapper.post.GoHighLevelPostListRequest;
import org.example.loficonnect.dto.mapper.post.GoHighLevelPostUpdateRequest;
import org.example.loficonnect.dto.request.post.BulkPostDeleteRequest;
import org.example.loficonnect.dto.request.post.PostCreateRequest;
import org.example.loficonnect.dto.request.post.PostListRequest;
import org.example.loficonnect.dto.request.post.PostUpdateRequest;
import org.example.loficonnect.feignclients.PostClient;
import org.example.loficonnect.service.AuthorizationService;
import org.example.loficonnect.service.PostService;
import org.example.loficonnect.util.AppKeyContext;
import org.example.loficonnect.util.VersionContext;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class PostServiceImpl implements PostService {

    private final PostClient postClient;
    private final AuthorizationService authorizationService;

    public PostServiceImpl(PostClient postClient, AuthorizationService authorizationService) {
        this.postClient = postClient;
        this.authorizationService = authorizationService;
    }

    @Override
    public JsonNode getPosts(String locationId, PostListRequest request) {
        String accessKey = authorizationService.getAccessToken(AppKeyContext.getAppKey());
        String version = VersionContext.getVersion();
        GoHighLevelPostListRequest ghlRequest = GoHighLevelPostListRequest.fromRequest(request);
        return postClient.getPosts(accessKey, version, locationId, ghlRequest);
    }

    @Override
    public JsonNode createPost(String locationId, PostCreateRequest request) {
        String accessKey = authorizationService.getAccessToken(AppKeyContext.getAppKey());
        String version = VersionContext.getVersion();
        GoHighLevelPostCreateRequest ghlRequest = GoHighLevelPostCreateRequest.fromRequest(request);
        return postClient.createPost(accessKey, version, locationId, ghlRequest);
    }

    @Override
    public JsonNode getPost(String locationId, String id) {
        String accessKey = authorizationService.getAccessToken(AppKeyContext.getAppKey());
        String version = VersionContext.getVersion();
        return postClient.getPost(accessKey, version, locationId, id);
    }

    @Override
    public JsonNode updatePost(String locationId, String id, PostUpdateRequest request) {
        String accessKey = authorizationService.getAccessToken(AppKeyContext.getAppKey());
        String version = VersionContext.getVersion();
        GoHighLevelPostUpdateRequest ghlRequest = GoHighLevelPostUpdateRequest.fromRequest(request);
        return postClient.updatePost(accessKey, version, locationId, id, ghlRequest);
    }

    @Override
    public JsonNode deletePost(String locationId, String id) {
        String accessKey = authorizationService.getAccessToken(AppKeyContext.getAppKey());
        String version = VersionContext.getVersion();
        return postClient.deletePost(accessKey, version, locationId, id);
    }

    @Override
    public JsonNode bulkDeletePosts(String locationId, BulkPostDeleteRequest request) {
        String accessKey = authorizationService.getAccessToken(AppKeyContext.getAppKey());
        String version = VersionContext.getVersion();
        GoHighLevelBulkPostDeleteRequest ghlRequest = GoHighLevelBulkPostDeleteRequest.fromRequest(request);
        return postClient.bulkDeletePosts(accessKey, version, locationId, ghlRequest);
    }

}
