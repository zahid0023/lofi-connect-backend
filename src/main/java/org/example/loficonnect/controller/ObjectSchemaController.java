package org.example.loficonnect.controller;

import org.example.loficonnect.config.AppKey;
import org.example.loficonnect.dto.request.objectschema.CustomObjectCreateRequest;
import org.example.loficonnect.dto.request.objectschema.ObjectSchemaUpdateRequest;
import org.example.loficonnect.service.ObjectSchemaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class ObjectSchemaController {

    private final ObjectSchemaService objectSchemaService;


    public ObjectSchemaController(ObjectSchemaService objectSchemaService) {
        this.objectSchemaService = objectSchemaService;
    }

    @AppKey
    @GetMapping("/objects/{key}")
    public ResponseEntity<?> getObjectSchema(
        @PathVariable("key") String key,
        @RequestParam("fetchProperties") String fetchProperties,
        @RequestParam("locationId") String locationId
    ) {
        return ResponseEntity.ok(objectSchemaService.getObjectSchema(key, locationId, fetchProperties));
    }

    @AppKey
    @PutMapping("/objects/{key}")
    public ResponseEntity<?> updateObjectSchema(
            @PathVariable("key") String key,
            @RequestBody ObjectSchemaUpdateRequest request
    ) {
        return ResponseEntity.ok(objectSchemaService.updateObjectSchema(key, request));
    }

    @AppKey
    @GetMapping("/objects")
    public ResponseEntity<?> getAllObjects(@RequestParam("location-id") String locationId) {
        return ResponseEntity.ok(objectSchemaService.getAllObjects(locationId));
    }

    @AppKey
    @PostMapping("/objects")
    public ResponseEntity<?> createCustomObject(@RequestBody CustomObjectCreateRequest request) {
        return ResponseEntity.status(201).body(objectSchemaService.createCustomObject(request));
    }
}
