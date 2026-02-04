package org.example.loficonnect.controller;

import org.example.loficonnect.commons.annotation.AppKey;
import org.example.loficonnect.service.RelationsService;
import org.example.loficonnect.dto.request.relations.RelationCreateRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/ghl")
public class RelationsController {
    private final RelationsService relationsService;

    public RelationsController(RelationsService relationsService) {
        this.relationsService = relationsService;
    }

    @AppKey
    @PostMapping("/associations/relations")
    public ResponseEntity<?> createRelation(@RequestBody RelationCreateRequest request) {
        return ResponseEntity.status(201).body(relationsService.createRelation(request));
    }

    @GetMapping("/associations/relations/{record-id}")
    public ResponseEntity<?> getAllRelationsByRecordId(
            @PathVariable("record-id") String recordId,
            @RequestParam("association-ids") String[] associationIds,
            @RequestParam("limit") int limit,
            @RequestParam("location-id") String locationId,
            @RequestParam("skip") int skip
    ) {
        return ResponseEntity.ok(relationsService.getAllRelationsByRecordId(recordId, associationIds, limit, locationId, skip));
    }

    @DeleteMapping("/associations/relations/{relation-id}")
    public ResponseEntity<?> deleteRelation(
            @PathVariable("relation-id") String relationId,
            @RequestParam("location-id") String locationId
    ) {
        return ResponseEntity.ok(relationsService.deleteRelation(relationId, locationId));
    }
}
