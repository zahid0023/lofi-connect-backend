package org.example.loficonnect.controller;

import org.example.loficonnect.config.AppKey;
import org.example.loficonnect.dto.request.subaccount.LocationCreateRequest;
import org.example.loficonnect.dto.request.subaccount.LocationUpdateRequest;
import org.example.loficonnect.service.SubAccountService;
import org.example.loficonnect.util.MapUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1")
public class SubAccountController {
    private final SubAccountService subAccountService;

    public SubAccountController(SubAccountService subAccountService) {
        this.subAccountService = subAccountService;
    }

    @AppKey
    @GetMapping("/locations/{location-id}")
    public ResponseEntity<?> getLocation(
            @PathVariable("location-id") String locationId
    ) {
        return ResponseEntity.ok(subAccountService.getLocation(locationId));
    }

    @AppKey
    @PutMapping("/locations/{location-id}")
    public ResponseEntity<?> updateLocation(
            @PathVariable("location-id") String locationId,
            @RequestBody LocationUpdateRequest request
    ) {
        return ResponseEntity.ok(subAccountService.updateLocation(locationId, request));
    }

    @AppKey
    @DeleteMapping("/locations/{location-id}")
    public ResponseEntity<?> deleteLocation(
            @PathVariable("location-id") String locationId,
            @RequestParam(value = "delete-twilio-account", required = false, defaultValue = "false") Boolean deleteTwilioAccount
    ) {
        Map<String, Object> queryParams = new HashMap<>();
        MapUtil.putIfNotNull(queryParams, "deleteTwilioAccount", deleteTwilioAccount);

        return ResponseEntity.ok(subAccountService.deleteLocation(locationId, queryParams));
    }

    @AppKey
    @PostMapping("/locations")
    public ResponseEntity<?> createLocation(@RequestBody LocationCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(subAccountService.createLocation(request));
    }


}
