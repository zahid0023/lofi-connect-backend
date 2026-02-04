package org.example.loficonnect.controller;

import org.example.loficonnect.commons.annotation.AppKey;
import org.example.loficonnect.dto.request.triggerlinks.LinkCreateRequest;
import org.example.loficonnect.dto.request.triggerlinks.LinkUpdateRequest;
import org.example.loficonnect.service.TriggerLinkService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/ghl")
public class TriggerLinkController {

    private final TriggerLinkService triggerLinkService;

    public TriggerLinkController(TriggerLinkService triggerLinkService) {
        this.triggerLinkService = triggerLinkService;
    }

    @AppKey
    @PutMapping("/trigger/{link-id}")
    public ResponseEntity<?> updateLink(
        @PathVariable("link-id") String linkId,
        @RequestBody LinkUpdateRequest request
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(triggerLinkService.updateLink(linkId, request));
    }

    @AppKey
    @DeleteMapping("/trigger/{link-id}")
    public ResponseEntity<?> deleteLink(
            @PathVariable("link-id") String linkId
    ) {
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(triggerLinkService.deleteLink(linkId));
    }

    @AppKey
    @GetMapping("/trigger")
    public ResponseEntity<?> getLinks(
            @RequestParam(value = "location-id") String locationId
    ) {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("locationId", locationId);

        return ResponseEntity.ok(triggerLinkService.getLinks(queryParams));
    }

    @AppKey
    @PostMapping("/trigger")
    public ResponseEntity<?> createLink(
            @RequestBody LinkCreateRequest request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(triggerLinkService.createLink(request));
    }
}
