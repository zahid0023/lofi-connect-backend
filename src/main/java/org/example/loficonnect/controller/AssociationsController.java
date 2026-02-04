package org.example.loficonnect.controller;

import org.example.loficonnect.commons.annotation.AppKey;
import org.example.loficonnect.dto.request.associations.AssociationUpdateRequest;
import org.example.loficonnect.dto.request.associations.AssociationCreateRequest;
import org.example.loficonnect.service.AssociationsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/ghl")
public class AssociationsController {
    private final AssociationsService associationsService;

    public AssociationsController(AssociationsService associationsService) {
        this.associationsService = associationsService;
    }

    @AppKey
    @GetMapping("/associations/key/{key_name}")
    public ResponseEntity<?> getAssociationKeyByName(
        @PathVariable("key_name") String keyName,
        @RequestParam("location-id") String locationId
    ) {
        return ResponseEntity.ok(associationsService.getAssociationKeyByName(keyName, locationId));
    }

    @AppKey
    @GetMapping("/associations/object-key/{object-key}")
    public ResponseEntity<?> getAssociationByObjectKey(
            @PathVariable("object-key") String objectKey,
            @RequestParam("location-id") String locationId
    ) {
        return ResponseEntity.ok(associationsService.getAssociationByObjectKey(objectKey, locationId));
    }

    @AppKey
    @PutMapping("/associations/{association-id}")
    public ResponseEntity<?> updateAssociation(
            @PathVariable("association-id") String associationId,
            @RequestBody AssociationUpdateRequest request
    ) {
        return ResponseEntity.ok(associationsService.updateAssociation(associationId, request));
    }

    @AppKey
    @DeleteMapping("/associations/{association-id}")
    public ResponseEntity<?> deleteAssociation(@PathVariable("association-id") String associationId) {
        return ResponseEntity.ok(associationsService.deleteAssociation(associationId));
    }

    @AppKey
    @GetMapping("/associations/{association-id}")
    public ResponseEntity<?> getAssociationById(@PathVariable("association-id") String associationId) {
        return ResponseEntity.ok(associationsService.getAssociationById(associationId));
    }

    @AppKey
    @PostMapping("/associations")
    public ResponseEntity<?> createAssociation(@RequestBody AssociationCreateRequest request) {
        return ResponseEntity.status(201).body(associationsService.createAssociation(request));
    }

    @AppKey
    @GetMapping("/associations")
    public ResponseEntity<?> getAllAssociations(
            @RequestParam("limit") int limit,
            @RequestParam("skip") int skip,
            @RequestParam("locationId") String locationId
    ) {
        return ResponseEntity.ok(associationsService.getAllAssociations(limit, skip, locationId));
    }
}
