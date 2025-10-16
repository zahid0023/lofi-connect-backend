package org.example.loficonnect.controller;

import org.example.loficonnect.config.AppKey;
import org.example.loficonnect.dto.request.records.RecordCreateRequest;
import org.example.loficonnect.dto.request.records.RecordUpdateRequest;
import org.example.loficonnect.service.RecordsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/ghl")
public class RecordsController {

    private final RecordsService recordsService;

    public RecordsController(RecordsService recordsService) {
        this.recordsService = recordsService;
    }

    @AppKey
    @GetMapping("/objects/{schema-key}/records/{id}")
    public ResponseEntity<?> getRecordById(
        @PathVariable("schema-key") String schemaKey,
        @PathVariable("id") String id
    ) {
        return ResponseEntity.ok(recordsService.getRecordById(schemaKey, id));
    }

    @AppKey
    @PutMapping("/objects/{schema-key}/records/{id}")
    public ResponseEntity<?> updateRecord(
            @PathVariable("schema-key") String schemaKey,
            @PathVariable("id") String id,
            @RequestBody RecordUpdateRequest request
    ) {
        return ResponseEntity.ok(recordsService.updateRecord(schemaKey, id, request));
    }

    @AppKey
    @DeleteMapping("/objects/{schema-key}/records/{id}")
    public ResponseEntity<?> deleteRecord(
            @PathVariable("schema-key") String schemaKey,
            @PathVariable("id") String id
    ) {
        return ResponseEntity.ok(recordsService.deleteRecord(schemaKey, id));
    }

    @AppKey
    @PostMapping("/objects/{schema-key}/records")
    public ResponseEntity<?> createRecord(
            @PathVariable("schema-key") String schemaKey,
            @RequestBody RecordCreateRequest request
    ) {
        return ResponseEntity.status(201).body(recordsService.createRecord(schemaKey, request));
    }
}
