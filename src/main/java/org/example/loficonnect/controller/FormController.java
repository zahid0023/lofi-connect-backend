package org.example.loficonnect.controller;

import com.fasterxml.jackson.databind.JsonNode;
import org.example.loficonnect.config.AppKey;
import org.example.loficonnect.dto.request.forms.FileUploadRequest;
import org.example.loficonnect.service.FormService;
import org.example.loficonnect.util.MapUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/ghl")
public class FormController {

    private final FormService formService;

    public FormController(FormService formService) {
        this.formService = formService;
    }

    @AppKey
    @GetMapping("/forms/submissions")
    public ResponseEntity<?> getFormSubmissions(
        @RequestParam(value = "end-at", required = false) String endAt,
        @RequestParam(value = "form-id", required = false) String formId,
        @RequestParam(value = "limit", required = false, defaultValue = "20") int limit,
        @RequestParam(value = "page", required = false, defaultValue = "1") int page,
        @RequestParam(value = "q", required = false) String q,
        @RequestParam(value = "start-at", required = false) String startAt,
        @RequestParam("location-id") String locationId
    ) {
        Map<String, Object> queryParams = new HashMap<>();
        MapUtil.putIfNotNull(queryParams, "endAt", endAt);
        MapUtil.putIfNotNull(queryParams, "formId", formId);
        MapUtil.putIfNotNull(queryParams, "limit", limit);
        MapUtil.putIfNotNull(queryParams, "page", page);
        MapUtil.putIfNotNull(queryParams, "q", q);
        MapUtil.putIfNotNull(queryParams, "startAt", startAt);
        MapUtil.putIfNotNull(queryParams, "locationId", locationId);

        JsonNode response = formService.getFormSubmissions(queryParams);
        return ResponseEntity.ok(response);
    }

    @AppKey
    @PostMapping("/forms/upload-custom-files")
    public ResponseEntity<?> uploadCustomFiles(
            @RequestBody FileUploadRequest request
    ) {
        JsonNode response = formService.uploadCustomFiles(request);
        return ResponseEntity.ok(response);
    }

    @AppKey
    @GetMapping("/forms")
    public ResponseEntity<?> getForms(
            @RequestParam(value = "limit", required = false, defaultValue = "10") int limit,
            @RequestParam(value = "skip", required = false, defaultValue = "0") int skip,
            @RequestParam(value = "type", required = false) String type,
            @RequestParam("location-id") String locationId
    ) {
        Map<String, Object> queryParams = new HashMap<>();
        MapUtil.putIfNotNull(queryParams, "limit", limit);
        MapUtil.putIfNotNull(queryParams, "skip", skip);
        MapUtil.putIfNotNull(queryParams, "type", type);
        MapUtil.putIfNotNull(queryParams, "locationId", locationId);

        JsonNode response = formService.getForms(queryParams);
        return ResponseEntity.ok(response);
    }
}
