package org.example.loficonnect.controller;

import org.example.loficonnect.config.AppKey;
import org.example.loficonnect.dto.request.customerprovider.CustomProviderCreateRequest;
import org.example.loficonnect.dto.request.customerprovider.CustomProviderDisconnectRequest;
import org.example.loficonnect.dto.request.customerprovider.CustomerProviderCreateRequest;
import org.example.loficonnect.service.CustomerProviderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/ghl")
public class CustomerProviderController {

    private final CustomerProviderService customerProviderService;

    public CustomerProviderController(CustomerProviderService customerProviderService) {
        this.customerProviderService = customerProviderService;
    }

    @AppKey
    @PostMapping("/payments/custom-providers")
    public ResponseEntity<?> createCustomProvider(
            @RequestParam("location-id") String locationId,
            @RequestBody CustomerProviderCreateRequest request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(customerProviderService.createCustomProvider(locationId, request));
    }

    @AppKey
    @DeleteMapping("/payments/custom-providers")
    public ResponseEntity<?> deleteCustomProvider(@RequestParam("location-id") String locationId) {
        return ResponseEntity.ok(customerProviderService.deleteCustomProvider(locationId));
    }

    @AppKey
    @GetMapping("/payments/custom-providers")
    public ResponseEntity<?> getCustomProviderConfig(@RequestParam("location-id") String locationId) {
        return ResponseEntity.ok(customerProviderService.getCustomProviderConfig(locationId));
    }

    @AppKey
    @PostMapping("/payments/custom-providers/payments/custom-provider/connect")
    public ResponseEntity<?> createCustomProviderConfig(
            @RequestParam("location-id") String locationId,
            @RequestBody CustomProviderCreateRequest request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(customerProviderService.createCustomProviderConfig(locationId, request));
    }

    @AppKey
    @PostMapping("/payments/custom-providers/payments/custom-provider/disconnect")
    public ResponseEntity<?> disconnectCustomProviderConfig(
            @RequestParam("location-id") String locationId,
            @RequestBody CustomProviderDisconnectRequest request
    ) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(customerProviderService.disconnectCustomProviderConfig(locationId, request));
    }

}
