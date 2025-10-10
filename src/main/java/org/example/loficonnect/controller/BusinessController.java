package org.example.loficonnect.controller;

import com.fasterxml.jackson.databind.JsonNode;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import org.example.loficonnect.config.AppKey;
import org.example.loficonnect.dto.request.businesses.BusinessCreateRequest;
import org.example.loficonnect.dto.request.businesses.BusinessUpdateRequest;
import org.example.loficonnect.service.BusinessService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1")
public class BusinessController {

    private final BusinessService businessService;
    public BusinessController(BusinessService businessService) {
        this.businessService = businessService;
    }

    @AppKey
    @GetMapping("/business/{business-id}")
    public ResponseEntity<?> getBusiness(@PathVariable("business-id") String businessId) {
        JsonNode response = businessService.getBusiness(businessId);
        return ResponseEntity.ok(response);
    }

    @AppKey
    @GetMapping("/businesses")
    public ResponseEntity<JsonNode> getBusinessesByLocation(
            @RequestParam(value = "location-id") String locationId,
            @RequestHeader("Authorization") String authorization,
            @RequestHeader("Version") String version
    ) {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("locationId", locationId);
        return ResponseEntity.ok(businessService.getBusinessesByLocation(queryParams));
    }

    @AppKey
    @PostMapping("/businesses")
    public ResponseEntity<JsonNode> createBusiness(@RequestBody BusinessCreateRequest request) {
        JsonNode response = businessService.createBusiness(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @AppKey
    @PutMapping("/businesses/{business-id}")
    public ResponseEntity<JsonNode> updateBusiness(
            @PathVariable("business-id") String businessId,
            @RequestBody BusinessUpdateRequest request
    ) {
        JsonNode response = businessService.updateBusiness(businessId, request);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @AppKey
    @DeleteMapping("/businesses/{business-id}")
    public ResponseEntity<JsonNode> deleteBusiness(@PathVariable("business-id") String businessId) {
        JsonNode response = businessService.deleteBusiness(businessId);
        return ResponseEntity.ok(response);
    }
}
