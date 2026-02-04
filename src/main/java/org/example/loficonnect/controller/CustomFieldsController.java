package org.example.loficonnect.controller;

import org.example.loficonnect.commons.annotation.AppKey;
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
            @RequestParam(value = "model", required = false) String model
    ) {
        Map<String, Object> queryParams = new HashMap<>();
        MapUtil.putIfNotNull(queryParams, "model", model);

        return ResponseEntity.ok(customFieldsService.getCustomFields(queryParams));
    }

        @AppKey
        @GetMapping("/custom-fields/types")
        public ResponseEntity<?> getCustomFieldsTypes(
                @RequestParam(value = "model", required = false) String model
        ) {
            Map<String, Object> queryParams = new HashMap<>();
            MapUtil.putIfNotNull(queryParams, "model", model);
            return ResponseEntity.status(HttpStatus.OK).body(customFieldsService.getCustomFieldsTypes(queryParams));
        }

    @AppKey
    @PostMapping("/custom-fields")
    public ResponseEntity<?> createCustomField(     
            @RequestBody CustomFieldCreateRequest request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(customFieldsService.createCustomField(request));
    }

    @AppKey
    @GetMapping("/custom-fields/{custom-field-id}")
    public ResponseEntity<?> getCustomField(
            @PathVariable("custom-field-id") String id
    ) {
        return ResponseEntity.ok(customFieldsService.getCustomField(id));
    }

    @AppKey
    @PutMapping("/custom-fields/{custom-field-id}")
    public ResponseEntity<?> updateCustomField(
            @PathVariable("custom-field-id") String id,
            @RequestBody CustomFieldUpdateRequest request
    ) {
        return ResponseEntity.ok(customFieldsService.updateCustomField(id, request));
    }

    @AppKey
    @DeleteMapping("/custom-fields/{custom-field-id}")
    public ResponseEntity<?> deleteCustomField(
            @PathVariable("custom-field-id") String id
    ) {
        return ResponseEntity.ok(customFieldsService.deleteCustomField(id));
    }

    @AppKey
    @PostMapping("/custom-fields/files")
    public ResponseEntity<?> uploadCustomFieldFile(
            @RequestParam("custom-field-id") String id,
            @RequestParam(required = false) Integer maxFiles,
            @RequestParam MultipartFile file
    ) {
        UploadCustomFieldFileRequest request = new UploadCustomFieldFileRequest();

        request.setId(id);
        request.setMaxFiles(maxFiles);
        request.setFile(file);

        return ResponseEntity.ok(customFieldsService.uploadCustomFieldFile(request));
    }
}
