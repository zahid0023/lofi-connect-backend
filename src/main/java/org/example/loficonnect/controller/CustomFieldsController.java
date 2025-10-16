package org.example.loficonnect.controller;

import org.example.loficonnect.config.AppKey;
import org.example.loficonnect.dto.request.custom.fields.CustomFieldCreateRequest;
import org.example.loficonnect.dto.request.custom.fields.CustomFieldUpdateRequest;
import org.example.loficonnect.dto.request.custom.fields.UploadCustomFieldFileRequest;
import org.example.loficonnect.service.CustomFieldsService;
import org.example.loficonnect.util.MapUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/ghl")
public class CustomFieldsController {
    private final CustomFieldsService customFieldsService;

    public CustomFieldsController(CustomFieldsService customFieldsService) {
        this.customFieldsService = customFieldsService;
    }

    @AppKey
    @GetMapping("/custom-fields")
    public ResponseEntity<?> getCustomFields(
            @RequestParam("location-id") String locationId,
            @RequestParam(value = "model", required = false) String model
    ) {
        Map<String, Object> queryParams = new HashMap<>();
        MapUtil.putIfNotNull(queryParams, "model", model);

        return ResponseEntity.ok(customFieldsService.getCustomFields(locationId, queryParams));
    }

    @AppKey
    @PostMapping("/custom-fields")
    public ResponseEntity<?> createCustomField(
            @RequestParam("location-id") String locationId,
            @RequestBody CustomFieldCreateRequest request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(customFieldsService.createCustomField(locationId, request));
    }

    @AppKey
    @GetMapping("/custom-fields/{id}")
    public ResponseEntity<?> getCustomField(
            @RequestParam("location-id") String locationId,
            @PathVariable("id") String id
    ) {
        return ResponseEntity.ok(customFieldsService.getCustomField(locationId, id));
    }

    @AppKey
    @PutMapping("/custom-fields/{id}")
    public ResponseEntity<?> updateCustomField(
            @RequestParam("location-id") String locationId,
            @PathVariable("id") String id,
            @RequestBody CustomFieldUpdateRequest request
    ) {
        return ResponseEntity.ok(customFieldsService.updateCustomField(locationId, id, request));
    }

    @AppKey
    @DeleteMapping("/custom-fields/{id}")
    public ResponseEntity<?> deleteCustomField(
            @RequestParam("location-id") String locationId,
            @PathVariable("id") String id
    ) {
        return ResponseEntity.ok(customFieldsService.deleteCustomField(locationId, id));
    }

    @AppKey
    @PostMapping("/custom-fields/files")
    public ResponseEntity<?> uploadCustomFieldFile(
            @RequestParam("location-id") String locationId,
            @RequestParam String id,
            @RequestParam(required = false) Integer maxFiles,
            @RequestParam MultipartFile file
    ) {
        UploadCustomFieldFileRequest request = new UploadCustomFieldFileRequest();

        request.setId(id);
        request.setMaxFiles(maxFiles);
        request.setFile(file);

        return ResponseEntity.ok(customFieldsService.uploadCustomFieldFile(locationId, request));
    }


}
