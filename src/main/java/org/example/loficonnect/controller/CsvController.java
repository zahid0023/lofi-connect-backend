package org.example.loficonnect.controller;

import org.example.loficonnect.commons.annotation.AppKey;
import org.example.loficonnect.dto.request.csv.CsvFinalizeRequest;
import org.example.loficonnect.dto.request.csv.CsvSetAccountsRequest;
import org.example.loficonnect.dto.request.csv.CsvUploadRequest;
import org.example.loficonnect.service.CsvService;
import org.example.loficonnect.util.MapUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/ghl")
public class CsvController {

    private final CsvService csvService;

    public CsvController(CsvService csvService) {
        this.csvService = csvService;
    }

    @AppKey
    @PostMapping("/csv/{location-id}")
    public ResponseEntity<?> uploadCsv(
        @PathVariable("location-id") String locationId,
        @RequestPart("file") MultipartFile file
    ) {
        CsvUploadRequest request = new CsvUploadRequest();
        request.setFile(file);
        return ResponseEntity.status(HttpStatus.CREATED).body(csvService.uploadCsv(locationId, request));
    }

    @AppKey
    @GetMapping("/csv/{location-id}")
    public ResponseEntity<?> getCsvUploadStatus(
            @PathVariable("location-id") String locationId,
            @RequestParam(value = "include-users", required = false) String includeUsers,
            @RequestParam(value = "limit", required = false, defaultValue = "10") String limit,
            @RequestParam(value = "skip", required = false, defaultValue = "0") String skip,
            @RequestParam(value = "user-id", required = false) String userId
    ) {
        Map<String, Object> queryParams = new HashMap<>();
        MapUtil.putIfNotNull(queryParams, "includeUsers", includeUsers);
        MapUtil.putIfNotNull(queryParams, "limit", limit);
        MapUtil.putIfNotNull(queryParams, "skip", skip);
        MapUtil.putIfNotNull(queryParams, "userId", userId);

        return ResponseEntity.ok(csvService.getCsvUploadStatus(locationId, queryParams));
    }

    @AppKey
    @PostMapping("/csv/{location-id}/set-accounts")
    public ResponseEntity<?> setAccounts(
            @PathVariable("location-id") String locationId,
            @RequestBody CsvSetAccountsRequest request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(csvService.setAccounts(locationId, request));
    }

    @AppKey
    @GetMapping("/csv/{location-id}/{id}")
    public ResponseEntity<?> getCsvPost(
            @PathVariable("location-id") String locationId,
            @PathVariable("id") String id,
            @RequestParam(value = "limit", required = false) String limit,
            @RequestParam(value = "skip", required = false) String skip
    ) {
        Map<String, Object> queryParams = new HashMap<>();
        MapUtil.putIfNotNull(queryParams, "limit", limit);
        MapUtil.putIfNotNull(queryParams, "skip", skip);

        return ResponseEntity.ok(csvService.getCsvPost(locationId, id, queryParams));
    }

    @AppKey
    @PatchMapping("/csv/{location-id}/{id}")
    public ResponseEntity<?> finalizeCsv(
            @PathVariable("location-id") String locationId,
            @PathVariable("id") String id,
            @RequestBody CsvFinalizeRequest request
    ) {
        return ResponseEntity.ok(csvService.finalizeCsv(locationId, id, request));
    }

    @AppKey
    @DeleteMapping("/csv/{location-id}/{id}")
    public ResponseEntity<?> deleteCsv(
            @PathVariable("location-id") String locationId,
            @PathVariable("id") String id
    ) {
        return ResponseEntity.ok(csvService.deleteCsv(locationId, id));
    }

    @AppKey
    @DeleteMapping("/csv/{location-id}/{csv-id}/post/{post-id}")
    public ResponseEntity<?> deleteCsvPost(
            @PathVariable("location-id") String locationId,
            @PathVariable("csv-id") String csvId,
            @PathVariable("post-id") String postId
    ) {
        return ResponseEntity.ok(csvService.deleteCsvPost(locationId, csvId, postId));
    }

}
