package org.example.loficonnect.serviceImpl;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import org.example.loficonnect.dto.mapper.blogs.GoHighLevelBlogPostCreateRequest;
import org.example.loficonnect.dto.mapper.blogs.GoHighLevelBlogPostUpdateRequest;
import org.example.loficonnect.dto.request.blogs.BlogPostCreateRequest;
import org.example.loficonnect.dto.request.blogs.BlogPostUpdateRequest;
import org.example.loficonnect.feignclients.BlogClient;
import org.example.loficonnect.service.AuthorizationService;
import org.example.loficonnect.service.BlogService;
import org.example.loficonnect.util.AppKeyContext;
import org.example.loficonnect.util.VersionContext;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@Slf4j
public class BlogServiceImpl implements BlogService {

    private final BlogClient blogClient;
    private final AuthorizationService authorizationService;

    public BlogServiceImpl(BlogClient blogClient, AuthorizationService authorizationService) {
        this.blogClient = blogClient;
        this.authorizationService = authorizationService;
    }

    @Override
    public JsonNode checkUrlSlug(Map<String, Object> queryParams) {
        String accessKey = authorizationService.getAccessToken(AppKeyContext.getAppKey());
        String version = VersionContext.getVersion();
        return blogClient.checkUrlSlug(accessKey, version, queryParams);
    }

    @Override
    public JsonNode updateBlogPost(String postId, BlogPostUpdateRequest request) {
        String accessKey = authorizationService.getAccessToken(AppKeyContext.getAppKey());
        String version = VersionContext.getVersion();
        GoHighLevelBlogPostUpdateRequest ghl = GoHighLevelBlogPostUpdateRequest.fromRequest(request);
        return blogClient.updateBlogPost(accessKey, version, postId, ghl);
    }

    @Override
    public JsonNode createBlogPost(BlogPostCreateRequest request) {
        String accessKey = authorizationService.getAccessToken(AppKeyContext.getAppKey());
        String version = VersionContext.getVersion();
        GoHighLevelBlogPostCreateRequest ghl = GoHighLevelBlogPostCreateRequest.fromRequest(request);
        return blogClient.createBlogPost(accessKey, version, ghl);
    }

    @Override
    public JsonNode getAuthors(Map<String, Object> queryParams) {
        String accessKey = authorizationService.getAccessToken(AppKeyContext.getAppKey());
        String version = VersionContext.getVersion();
        return blogClient.getAuthors(accessKey, version, queryParams);
    }

    @Override
    public JsonNode getCategories(Map<String, Object> queryParams) {
        String accessKey = authorizationService.getAccessToken(AppKeyContext.getAppKey());
        String version = VersionContext.getVersion();
        return blogClient.getCategories(accessKey, version, queryParams);
    }

    @Override
    public JsonNode getBlogPostsByBlogId(Map<String, Object> queryParams) {
        String accessKey = authorizationService.getAccessToken(AppKeyContext.getAppKey());
        String version = VersionContext.getVersion();
        return blogClient.getBlogPostsByBlogId(accessKey, version, queryParams);
    }

    @Override
    public JsonNode getBlogsByLocation(Map<String, Object> queryParams) {
        String accessKey = authorizationService.getAccessToken(AppKeyContext.getAppKey());
        String version = VersionContext.getVersion();
        return blogClient.getBlogsByLocation(accessKey, version, queryParams);
    }

}
