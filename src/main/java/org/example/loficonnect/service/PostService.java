package org.example.loficonnect.service;

import com.fasterxml.jackson.databind.JsonNode;
import org.example.loficonnect.dto.request.post.BulkPostDeleteRequest;
import org.example.loficonnect.dto.request.post.PostCreateRequest;
import org.example.loficonnect.dto.request.post.PostListRequest;
import org.example.loficonnect.dto.request.post.PostUpdateRequest;

public interface PostService {
    JsonNode getPosts(String locationId, PostListRequest request);
    JsonNode createPost(String locationId, PostCreateRequest request);
    JsonNode getPost(String locationId, String id);
    JsonNode updatePost(String locationId, String id, PostUpdateRequest request);
    JsonNode deletePost(String locationId, String id);
    JsonNode bulkDeletePosts(String locationId, BulkPostDeleteRequest request);
}
