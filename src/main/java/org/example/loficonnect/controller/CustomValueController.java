package org.example.loficonnect.controller;

import org.example.loficonnect.commons.annotation.AppKey;
import org.example.loficonnect.dto.request.custom.value.CustomValueCreateRequest;
import org.example.loficonnect.dto.request.custom.value.CustomValueUpdateRequest;
import org.example.loficonnect.service.CustomValueService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/ghl")
public class CustomValueController {
    private final CustomValueService customValueService;

    public CustomValueController(CustomValueService customValueService) {
        this.customValueService = customValueService;
    }

    @AppKey
    @GetMapping("/custom-values")
    public ResponseEntity<?> getCustomValues() {
        return ResponseEntity.ok(customValueService.getCustomValues());
    }

    @AppKey
    @PostMapping("/custom-values")
    public ResponseEntity<?> createCustomValue(
            @RequestBody CustomValueCreateRequest request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(customValueService.createCustomValue(request));
    }

    @AppKey
    @GetMapping("/locations/custom-values/{custom-value-id}")
    public ResponseEntity<?> getCustomValue(
            @PathVariable("custom-value-id") String id
    ) {
        return ResponseEntity.ok(customValueService.getCustomValue(id));
    }

    @AppKey
    @PutMapping("/custom-values/{custom-value-id}")
    public ResponseEntity<?> updateCustomValue(
            @PathVariable("custom-value-id") String id,
            @RequestBody CustomValueUpdateRequest request
    ) {
        return ResponseEntity.ok(customValueService.updateCustomValue(id, request));
    }

    @AppKey
    @DeleteMapping("/custom-values/{custom-value-id}")
    public ResponseEntity<?> deleteCustomValue(
            @PathVariable("custom-value-id") String id
    ) {
        return ResponseEntity.ok(customValueService.deleteCustomValue(id));
    }
}
