package org.example.loficonnect.controller;

import org.example.loficonnect.config.AppKey;
import org.example.loficonnect.dto.request.opportunityfollower.OpportunityFollowerRequest;
import org.example.loficonnect.service.OpportunityFollowerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/opportunities")
public class OpportunityFollowerController {

    private final OpportunityFollowerService opportunityFollowerService;

    public OpportunityFollowerController(OpportunityFollowerService opportunityFollowerService) {
        this.opportunityFollowerService = opportunityFollowerService;
    }

    @AppKey
    @PostMapping("/{id}/followers")
    public ResponseEntity<?> addFollowers(
        @PathVariable("id") String opportunityId,
        @RequestBody OpportunityFollowerRequest request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                             .body(opportunityFollowerService.addFollowers(opportunityId, request));
    }

    @AppKey
    @DeleteMapping("/{id}/followers")
    public ResponseEntity<?> removeFollowers(
            @PathVariable("id") String opportunityId,
            @RequestBody Map<String, Object> requestBody
    ) {
        return ResponseEntity.ok(opportunityFollowerService.removeFollowers(opportunityId, requestBody));
    }


}
