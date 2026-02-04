package org.example.loficonnect.controller;

import org.example.loficonnect.commons.annotation.AppKey;
import org.example.loficonnect.dto.request.bulk.BulkBusinessUpdateRequest;
import org.example.loficonnect.dto.request.bulk.BulkTagUpdateRequest;
import org.example.loficonnect.service.BulkService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/ghl")
public class BulkController {
    private final BulkService bulkService;

    public BulkController(BulkService bulkService) {
        this.bulkService = bulkService;
    }

    @AppKey
    @PostMapping("/contacts/bulk/tags/update/{type}")
    public ResponseEntity<?> updateBulkTags(
            @PathVariable("type") String type,
            @RequestBody BulkTagUpdateRequest request) {
        return ResponseEntity.status(HttpStatus.OK).body(bulkService.updateBulkTags(type, request));
    }

    @AppKey
    @PostMapping("/contacts/bulk/business")
    public ResponseEntity<?> updateBulkBusiness(@RequestBody BulkBusinessUpdateRequest request) {
        return ResponseEntity.ok(bulkService.updateBulkBusiness(request));
    }
}
