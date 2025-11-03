package org.example.loficonnect.controller;

import org.example.loficonnect.config.AppKey;
import org.example.loficonnect.dto.request.tags.ContactTagsAddRequest;
import org.example.loficonnect.dto.request.tags.ContactTagsRemoveRequest;
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
    @PostMapping("/contacts/{contact-id}/tags")
    public ResponseEntity<?> addTagsToContact(
            @PathVariable("contact-id") String contactId,
            @RequestBody ContactTagsAddRequest request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(tagsService.createContactTags(contactId, request));
    }

    @AppKey
    @DeleteMapping("/contacts/{contact-id}/tags")
    public ResponseEntity<?> removeTagsFromContact(
            @PathVariable("contact-id") String contactId,
            @RequestBody ContactTagsRemoveRequest request
    ) {
        return ResponseEntity.ok(tagsService.deleteContactTags(contactId, request));
    }

    @AppKey
    @GetMapping("/locations/tags")
    public ResponseEntity<?> getLocationTags() {
        return ResponseEntity.ok(tagsService.getLocationTags());
    }

    @AppKey
    @PostMapping("/tags")
    public ResponseEntity<?> createTag(
            @RequestBody TagCreateRequest request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(tagsService.createLocationTag(request));
    }

    @AppKey
    @GetMapping("/tags/{tag-id}")
    public ResponseEntity<?> getTagById(
            @PathVariable("tag-id") String tagId
    ) {
        return ResponseEntity.ok(tagsService.getTagById(tagId));
    }

    @AppKey
    @PutMapping("/locations/tags/{tag-id}")
    public ResponseEntity<?> updateTag(
            @PathVariable("tag-id") String tagId,
            @RequestBody TagUpdateRequest request
    ) {
        return ResponseEntity.ok(tagsService.updateTag(tagId, request));
    }

    @AppKey
    @DeleteMapping("/locations/tags/{tag-id}")
    public ResponseEntity<?> deleteTag(
            @PathVariable("tag-id") String tagId
    ) {
        return ResponseEntity.ok(tagsService.deleteTag(tagId));
    }
}
