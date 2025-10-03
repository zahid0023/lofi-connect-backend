package org.example.loficonnect.controller;

import lombok.RequiredArgsConstructor;
import org.example.loficonnect.config.AppKey;
import org.example.loficonnect.dto.request.opportunity.OpportunityCreateRequest;
import org.example.loficonnect.dto.request.opportunity.OpportunityStatusUpdateRequest;
import org.example.loficonnect.dto.request.opportunity.OpportunityUpdateRequest;
import org.example.loficonnect.dto.request.opportunity.OpportunityUpsertRequest;
import org.example.loficonnect.service.OpportunityService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/opportunities")
public class OpportunityController {

    private final OpportunityService opportunityService;

    public OpportunityController(OpportunityService opportunityService) {
        this.opportunityService = opportunityService;
    }

    @AppKey
    @GetMapping("/{id}")
    public ResponseEntity<?> getOpportunityById(@PathVariable("id") String opportunityId) {
        return ResponseEntity.ok(opportunityService.getOpportunityById(opportunityId));
    }

    @AppKey
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteOpportunityById(@PathVariable("id") String opportunityId) {
        return ResponseEntity.ok(opportunityService.deleteOpportunityById(opportunityId));
    }

    @AppKey
    @PutMapping("/{id}")
    public ResponseEntity<?> updateOpportunityById(
            @PathVariable("id") String id,
            @RequestBody OpportunityUpdateRequest request
    ) {
        return ResponseEntity.ok(opportunityService.updateOpportunityById(id, request));
    }

    @AppKey
    @PutMapping("/{id}/status")
    public ResponseEntity<?> updateOpportunityStatus(
            @PathVariable("id") String id,
            @RequestBody OpportunityStatusUpdateRequest request
    ) {
        return ResponseEntity.ok(opportunityService.updateOpportunityStatus(id, request));
    }

    @AppKey
    @PostMapping("/upsert")
    public ResponseEntity<?> upsertOpportunity(@RequestBody OpportunityUpsertRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(opportunityService.upsertOpportunity(request));
    }

    @AppKey
    @PostMapping
    public ResponseEntity<?> createOpportunity(@RequestBody OpportunityCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(opportunityService.createOpportunity(request));
    }

}
