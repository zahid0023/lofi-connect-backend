package org.example.loficonnect.controller;

import com.fasterxml.jackson.databind.JsonNode;
import org.example.loficonnect.config.AppKey;
import org.example.loficonnect.dto.request.blogs.BlogPostCreateRequest;
import org.example.loficonnect.dto.request.blogs.BlogPostUpdateRequest;
import org.example.loficonnect.service.BlogService;
import org.example.loficonnect.util.MapUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/ghl")
public class BlogController {

    private final BlogService blogService;

    public BlogController(BlogService blogService) {
        this.blogService = blogService;
    }

    @AppKey
    @GetMapping("/blogs/posts/url-slug-exists")
    public ResponseEntity<?> checkUrlSlug(
            @RequestParam(value = "post-id", required = false) String postId,
            @RequestParam(value = "location-id") String locationId,
            @RequestParam(value = "url-slug", required = false) String urlSlug
    ) {
        Map<String, Object> queryParams = new HashMap<>();
        MapUtil.putIfNotNull(queryParams, "postId", postId);
        MapUtil.putIfNotNull(queryParams, "locationId", locationId);
        MapUtil.putIfNotNull(queryParams, "urlSlug", urlSlug);

        JsonNode response = blogService.checkUrlSlug(queryParams);
        return ResponseEntity.ok(response);
    }

    @AppKey
    @PutMapping("/blogs/posts/{post-id}")
    public ResponseEntity<?> updateBlogPost(
            @PathVariable("post-id") String postId,
            @RequestBody BlogPostUpdateRequest request
    ) {
        JsonNode response = blogService.updateBlogPost(postId, request);
        return ResponseEntity.ok(response);
    }

    @AppKey
    @PostMapping("/blogs/posts")
    public ResponseEntity<?> createBlogPost(@RequestBody BlogPostCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(blogService.createBlogPost(request));
    }

    @AppKey
    @GetMapping("/blogs/authors")
    public ResponseEntity<?> getAuthors(
            @RequestParam("limit") Integer limit,
            @RequestParam("location-id") String locationId,
            @RequestParam("offset") Integer offset
    ) {
        Map<String, Object> queryParams = new HashMap<>();
        MapUtil.putIfNotNull(queryParams, "limit", limit);
        MapUtil.putIfNotNull(queryParams, "locationId", locationId);
        MapUtil.putIfNotNull(queryParams, "offset", offset);
        return ResponseEntity.ok(blogService.getAuthors(queryParams));
    }

    @AppKey
    @GetMapping("/blogs/categories")
    public ResponseEntity<?> getCategories(
            @RequestParam("limit") Integer limit,
            @RequestParam("location-id") String locationId,
            @RequestParam("offset") Integer offset
    ) {
        Map<String, Object> queryParams = new HashMap<>();
        MapUtil.putIfNotNull(queryParams, "limit", limit);
        MapUtil.putIfNotNull(queryParams, "locationId", locationId);
        MapUtil.putIfNotNull(queryParams, "offset", offset);
        return ResponseEntity.ok(blogService.getCategories(queryParams));
    }

    @AppKey
    @GetMapping("/blogs/posts/all")
    public ResponseEntity<?> getBlogPostsByBlogId(
            @RequestParam(value = "search-term", required = false) String searchTerm,
            @RequestParam(value = "status", required = false) String status, // PUBLISHED|SCHEDULED|ARCHIVED|DRAFT
            @RequestParam("blog-id") String blogId,
            @RequestParam("limit") Integer limit,
            @RequestParam("location-id") String locationId,
            @RequestParam("offset") Integer offset
    ) {
        Map<String, Object> queryParams = new HashMap<>();
        MapUtil.putIfNotNull(queryParams, "searchTerm", searchTerm);
        MapUtil.putIfNotNull(queryParams, "status", status);
        MapUtil.putIfNotNull(queryParams, "blogId", blogId);
        MapUtil.putIfNotNull(queryParams, "limit", limit);
        MapUtil.putIfNotNull(queryParams, "locationId", locationId);
        MapUtil.putIfNotNull(queryParams, "offset", offset);
        return ResponseEntity.ok(blogService.getBlogPostsByBlogId(queryParams));
    }

    @AppKey
    @GetMapping("/blogs/site/all")
    public ResponseEntity<?> getBlogsByLocation(
            @RequestParam(value = "search-term", required = false) String searchTerm,
            @RequestParam("limit") Integer limit,
            @RequestParam("location-id") String locationId,
            @RequestParam("skip") Integer skip
    ) {
        Map<String, Object> queryParams = new HashMap<>();
        MapUtil.putIfNotNull(queryParams, "searchTerm", searchTerm);
        MapUtil.putIfNotNull(queryParams, "limit", limit);
        MapUtil.putIfNotNull(queryParams, "locationId", locationId);
        MapUtil.putIfNotNull(queryParams, "skip", skip);
        return ResponseEntity.ok(blogService.getBlogsByLocation(queryParams));
    }
}
