package org.example.loficonnect.controller;

import org.example.loficonnect.config.AppKey;
import org.example.loficonnect.dto.request.searchobjectrecords.SearchObjectRecordsRequest;
import org.example.loficonnect.service.SearchObjectRecordsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/ghl")
public class SearchObjectRecordsController {

    private final SearchObjectRecordsService searchObjectRecordsService;

    public SearchObjectRecordsController(SearchObjectRecordsService searchObjectRecordsService) {
        this.searchObjectRecordsService = searchObjectRecordsService;
    }

    @AppKey
    @PostMapping("/objects/{schema-key}/records/search")
    public ResponseEntity<?> searchObjectRecords(
        @PathVariable("schema-key") String schemaKey,
        @RequestBody SearchObjectRecordsRequest request
    ) {
        return ResponseEntity.ok(searchObjectRecordsService.searchObjectRecords(schemaKey, request));
    }
}
