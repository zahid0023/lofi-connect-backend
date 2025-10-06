package org.example.loficonnect.feignclients;

import com.fasterxml.jackson.databind.JsonNode;
import org.example.loficonnect.config.FeignLoggingConfig;
import org.example.loficonnect.dto.mapper.post.GoHighLevelBulkPostDeleteRequest;
import org.example.loficonnect.dto.mapper.post.GoHighLevelPostCreateRequest;
import org.example.loficonnect.dto.mapper.post.GoHighLevelPostListRequest;
import org.example.loficonnect.dto.mapper.post.GoHighLevelPostUpdateRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "postClient", url = "https://services.leadconnectorhq.com", configuration = FeignLoggingConfig.class)
public interface PostClient {

    @PostMapping(
        value = "/social-media-posting/{locationId}/posts/list",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    JsonNode getPosts(
        @RequestHeader("Authorization") String authorization,
        @RequestHeader("Version") String version,
        @PathVariable("locationId") String locationId,
        @RequestBody GoHighLevelPostListRequest request
    );

    @PostMapping(
            value = "/social-media-posting/{locationId}/posts",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    JsonNode createPost(
            @RequestHeader("Authorization") String authorization,
            @RequestHeader("Version") String version,
            @PathVariable("locationId") String locationId,
            @RequestBody GoHighLevelPostCreateRequest request
    );

    @GetMapping(
            value = "/social-media-posting/{locationId}/posts/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    JsonNode getPost(
            @RequestHeader("Authorization") String authorization,
            @RequestHeader("Version") String version,
            @PathVariable("locationId") String locationId,
            @PathVariable("id") String id
    );

    @PutMapping(
            value = "/social-media-posting/{locationId}/posts/{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    JsonNode updatePost(
            @RequestHeader("Authorization") String authorization,
            @RequestHeader("Version") String version,
            @PathVariable("locationId") String locationId,
            @PathVariable("id") String id,
            @RequestBody GoHighLevelPostUpdateRequest request
    );

    @DeleteMapping(
            value = "/social-media-posting/{locationId}/posts/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    JsonNode deletePost(
            @RequestHeader("Authorization") String authorization,
            @RequestHeader("Version") String version,
            @PathVariable("locationId") String locationId,
            @PathVariable("id") String id
    );

    @PostMapping(
            value = "/social-media-posting/{locationId}/posts/bulk-delete",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    JsonNode bulkDeletePosts(
            @RequestHeader("Authorization") String authorization,
            @RequestHeader("Version") String version,
            @PathVariable("locationId") String locationId,
            @RequestBody GoHighLevelBulkPostDeleteRequest request
    );

}
