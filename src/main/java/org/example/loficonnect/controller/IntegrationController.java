package org.example.loficonnect.controller;

import org.example.loficonnect.config.AppKey;
import org.example.loficonnect.dto.request.integrations.IntegrationCreateRequest;
import org.example.loficonnect.service.IntegrationService;
import org.example.loficonnect.util.MapUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/ghl")
public class IntegrationController {

    private final IntegrationService integrationService;

    public IntegrationController(IntegrationService integrationService) {
        this.integrationService = integrationService;
    }

    @AppKey
    @PostMapping("/integrations/provider/whitelabel")
    public ResponseEntity<?> createIntegration(@RequestBody IntegrationCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(integrationService.createIntegration(request));
    }


    @AppKey
    @GetMapping("/integrations/provider/whitelabel")
    public ResponseEntity<?> getIntegrationProviders(
            @RequestParam("alt-id") String altId,
            @RequestParam("alt-type") String altType,
            @RequestParam(value = "limit", required = false, defaultValue = "20") Integer limit,
            @RequestParam(value = "offset", required = false, defaultValue = "0") Integer offset
    ) {
        Map<String, Object> queryParams = new HashMap<>();
        MapUtil.putIfNotNull(queryParams, "altId", altId);
        MapUtil.putIfNotNull(queryParams, "altType", altType);
        MapUtil.putIfNotNull(queryParams, "limit", limit);
        MapUtil.putIfNotNull(queryParams, "offset", offset);

        return ResponseEntity.ok(integrationService.getIntegrationProviders(queryParams));
    }

}
