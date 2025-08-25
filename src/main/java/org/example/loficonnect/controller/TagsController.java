package org.example.loficonnect.controller;

import org.example.loficonnect.config.AppKey;
import org.example.loficonnect.dto.request.tags.ContactTagCreateRequest;
import org.example.loficonnect.dto.request.tags.ContactTagDeleteRequest;
import org.example.loficonnect.service.TagsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
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


}
