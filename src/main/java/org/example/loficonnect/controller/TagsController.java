package org.example.loficonnect.controller;

import org.example.loficonnect.config.AppKey;
import org.example.loficonnect.dto.request.tags.ContactTagCreateRequest;
import org.example.loficonnect.dto.request.tags.ContactTagDeleteRequest;
import org.example.loficonnect.dto.request.tags.TagCreateRequest;
import org.example.loficonnect.dto.request.tags.TagUpdateRequest;
import org.example.loficonnect.service.TagsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/ghl")
public class TagsController {
    private final TagsService tagsService;

    public TagsController(TagsService tagsService) {
        this.tagsService = tagsService;
    }

    @AppKey
    @PostMapping("/contacts/{contactId}/tags")
    public ResponseEntity<?> createContactTags(
            @PathVariable("contactId") String contactId,
            @RequestBody ContactTagCreateRequest request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(tagsService.createContactTags(contactId, request));
    }

    @AppKey
    @DeleteMapping("/contacts/{contactId}/tags")
    public ResponseEntity<?> deleteContactTags(
            @PathVariable("contactId") String contactId,
            @RequestBody ContactTagDeleteRequest request
    ) {
        return ResponseEntity.ok(tagsService.deleteContactTags(contactId, request));
    }

    @AppKey
    @GetMapping("/locations/{location-id}/tags")
    public ResponseEntity<?> getLocationTags(@PathVariable("location-id") String locationId) {
        return ResponseEntity.ok(tagsService.getLocationTags(locationId));
    }

    @AppKey
    @PostMapping("/tags")
    public ResponseEntity<?> createTag(
            @RequestParam("location-id") String locationId,
            @RequestBody TagCreateRequest request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(tagsService.createLocationTag(locationId, request));
    }

    @AppKey
    @GetMapping("/tags/{tag-id}")
    public ResponseEntity<?> getTagById(
            @PathVariable("tag-id") String tagId,
            @RequestParam("location-id") String locationId
    ) {
        return ResponseEntity.ok(tagsService.getTagById(locationId, tagId));
    }

    @AppKey
    @PutMapping("/locations/{location-id}/tags/{tag-id}")
    public ResponseEntity<?> updateTag(
            @PathVariable("location-id") String locationId,
            @PathVariable("tag-id") String tagId,
            @RequestBody TagUpdateRequest request
    ) {
        return ResponseEntity.ok(tagsService.updateTag(locationId, tagId, request));
    }

    @AppKey
    @DeleteMapping("/locations/{location-id}/tags/{tag-id}")
    public ResponseEntity<?> deleteTag(
            @PathVariable("location-id") String locationId,
            @PathVariable("tag-id") String tagId
    ) {
        return ResponseEntity.ok(tagsService.deleteTag(locationId, tagId));
    }


}
