package org.example.loficonnect.controller;

import org.example.loficonnect.commons.annotation.AppKey;
import org.example.loficonnect.dto.request.post.BulkPostDeleteRequest;
import org.example.loficonnect.dto.request.post.PostCreateRequest;
import org.example.loficonnect.dto.request.post.PostListRequest;
import org.example.loficonnect.dto.request.post.PostUpdateRequest;
import org.example.loficonnect.service.PostService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/ghl")
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @AppKey
    @PostMapping("/locations/{location-id}/posts/list")
    public ResponseEntity<?> getPosts(
        @PathVariable("location-id") String locationId,
        @RequestBody PostListRequest request
    ) {
        return ResponseEntity.ok(postService.getPosts(locationId, request));
    }

    @AppKey
    @PostMapping("/locations/{location-id}/posts")
    public ResponseEntity<?> createPost(
            @PathVariable("location-id") String locationId,
            @RequestBody PostCreateRequest request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(postService.createPost(locationId, request));
    }

    @AppKey
    @GetMapping("/locations/{location-id}/posts/{id}")
    public ResponseEntity<?> getPost(
            @PathVariable("location-id") String locationId,
            @PathVariable("id") String id
    ) {
        return ResponseEntity.ok(postService.getPost(locationId, id));
    }

    @AppKey
    @PutMapping("/locations/{location-id}/posts/{id}")
    public ResponseEntity<?> updatePost(
            @PathVariable("location-id") String locationId,
            @PathVariable("id") String id,
            @RequestBody PostUpdateRequest request
    ) {
        return ResponseEntity.ok(postService.updatePost(locationId, id, request));
    }

    @AppKey
    @DeleteMapping("/locations/{location-id}/posts/{id}")
    public ResponseEntity<?> deletePost(
            @PathVariable("location-id") String locationId,
            @PathVariable("id") String id
    ) {
        return ResponseEntity.ok(postService.deletePost(locationId, id));
    }

    @AppKey
    @PostMapping("/locations/{location-id}/posts/bulk-delete")
    public ResponseEntity<?> bulkDeletePosts(
            @PathVariable("location-id") String locationId,
            @RequestBody BulkPostDeleteRequest request
    ) {
        return ResponseEntity.ok(postService.bulkDeletePosts(locationId, request));
    }

}
