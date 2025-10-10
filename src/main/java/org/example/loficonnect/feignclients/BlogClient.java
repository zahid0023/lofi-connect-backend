package org.example.loficonnect.feignclients;

import com.fasterxml.jackson.databind.JsonNode;
import org.example.loficonnect.config.FeignLoggingConfig;
import org.example.loficonnect.dto.mapper.blogs.GoHighLevelBlogPostCreateRequest;
import org.example.loficonnect.dto.mapper.blogs.GoHighLevelBlogPostUpdateRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@FeignClient(name = "blogClient", url = "https://services.leadconnectorhq.com", configuration = FeignLoggingConfig.class)
public interface BlogClient {

    @GetMapping(
            value = "/blogs/posts/url-slug-exists",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    JsonNode checkUrlSlug(
            @RequestHeader("Authorization") String authorization,
            @RequestHeader("Version") String version,
            @RequestParam Map<String, Object> queryParams
    );

    @PutMapping(
            value = "/blogs/posts/{postId}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    JsonNode updateBlogPost(
            @RequestHeader("Authorization") String authorization,
            @RequestHeader("Version") String version,
            @PathVariable("postId") String postId,
            @RequestBody GoHighLevelBlogPostUpdateRequest request
    );

    @PostMapping(
            value = "/blogs/posts",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    JsonNode createBlogPost(
            @RequestHeader("Authorization") String authorization,
            @RequestHeader("Version") String version,
            @RequestBody GoHighLevelBlogPostCreateRequest request
    );

    @GetMapping(
            value = "/blogs/authors",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    JsonNode getAuthors(
            @RequestHeader("Authorization") String authorization,
            @RequestHeader("Version") String version,
            @RequestParam Map<String, Object> queryParams
    );

    @GetMapping(
            value = "/blogs/categories",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    JsonNode getCategories(
            @RequestHeader("Authorization") String authorization,
            @RequestHeader("Version") String version,
            @RequestParam Map<String, Object> queryParams
    );

    @GetMapping(
            value = "/blogs/posts/all",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    JsonNode getBlogPostsByBlogId(
            @RequestHeader("Authorization") String authorization,
            @RequestHeader("Version") String version,
            @RequestParam Map<String, Object> queryParams
    );

    @GetMapping(
            value = "/blogs/site/all",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    JsonNode getBlogsByLocation(
            @RequestHeader("Authorization") String authorization,
            @RequestHeader("Version") String version,
            @RequestParam Map<String, Object> queryParams
    );

}
