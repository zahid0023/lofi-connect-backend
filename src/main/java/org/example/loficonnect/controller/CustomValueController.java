package org.example.loficonnect.controller;

import org.example.loficonnect.config.AppKey;
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
    public ResponseEntity<?> getCustomValues(@RequestParam("location-id") String locationId) {
        return ResponseEntity.ok(customValueService.getCustomValues(locationId));
    }

    @AppKey
    @PostMapping("/custom-values")
    public ResponseEntity<?> createCustomValue(
            @RequestParam("location-id") String locationId,
            @RequestBody CustomValueCreateRequest request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(customValueService.createCustomValue(locationId, request));
    }

    @AppKey
    @GetMapping("/locations/{location-id}/custom-values/{id}")
    public ResponseEntity<?> getCustomValue(
            @PathVariable("location-id") String locationId,
            @PathVariable("id") String id
    ) {
        return ResponseEntity.ok(customValueService.getCustomValue(locationId, id));
    }

    @AppKey
    @PutMapping("/custom-values/{id}")
    public ResponseEntity<?> updateCustomValue(
            @RequestParam("location-id") String locationId,
            @PathVariable("id") String id,
            @RequestBody CustomValueUpdateRequest request
    ) {
        return ResponseEntity.ok(customValueService.updateCustomValue(locationId, id, request));
    }

    @AppKey
    @DeleteMapping("/custom-values/{id}")
    public ResponseEntity<?> deleteCustomValue(
            @RequestParam("location-id") String locationId,
            @PathVariable("id") String id
    ) {
        return ResponseEntity.ok(customValueService.deleteCustomValue(locationId, id));
    }


}
