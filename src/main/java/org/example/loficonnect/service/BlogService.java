package org.example.loficonnect.service;

import com.fasterxml.jackson.databind.JsonNode;
import org.example.loficonnect.dto.request.blogs.BlogPostCreateRequest;
import org.example.loficonnect.dto.request.blogs.BlogPostUpdateRequest;

import java.util.Map;

public interface BlogService {
    JsonNode checkUrlSlug(Map<String, Object> queryParams);
    JsonNode updateBlogPost(String postId, BlogPostUpdateRequest request);
    JsonNode createBlogPost(BlogPostCreateRequest request);
    JsonNode getAuthors(Map<String, Object> queryParams);
    JsonNode getCategories(Map<String, Object> queryParams);
    JsonNode getBlogPostsByBlogId(Map<String, Object> queryParams);
    JsonNode getBlogsByLocation(Map<String, Object> queryParams);
}
