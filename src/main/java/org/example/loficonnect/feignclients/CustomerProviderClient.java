package org.example.loficonnect.feignclients;

import com.fasterxml.jackson.databind.JsonNode;
import org.example.loficonnect.commons.config.FeignLoggingConfig;
import org.example.loficonnect.dto.mapper.customerprovider.GoHighLevelCustomProviderCreateRequest;
import org.example.loficonnect.dto.mapper.customerprovider.GoHighLevelCustomProviderDisconnectRequest;
import org.example.loficonnect.dto.mapper.customerprovider.GoHighLevelCustomerProviderCreateRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "customerProviderClient", url = "https://services.leadconnectorhq.com", configuration = FeignLoggingConfig.class)
public interface CustomerProviderClient {

    @PostMapping(
            value = "/payments/custom-provider/provider",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    JsonNode createCustomProvider(
            @RequestHeader("Authorization") String authorization,
            @RequestHeader("Version") String version,
            @RequestParam("locationId") String locationId,
            @RequestBody GoHighLevelCustomerProviderCreateRequest request
    );

    @DeleteMapping("/payments/custom-provider/provider")
    JsonNode deleteCustomProvider(
            @RequestHeader("Authorization") String authorization,
            @RequestHeader("Version") String version,
            @RequestParam("locationId") String locationId
    );

    @GetMapping("/payments/custom-provider/connect")
    JsonNode getCustomProviderConfig(
            @RequestHeader("Authorization") String authorization,
            @RequestHeader("Version") String version,
            @RequestParam("locationId") String locationId
    );

    @PostMapping(
            value = "/payments/custom-provider/connect",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    JsonNode createCustomProviderConfig(
            @RequestHeader("Authorization") String authorization,
            @RequestHeader("Version") String version,
            @RequestParam("locationId") String locationId,
            @RequestBody GoHighLevelCustomProviderCreateRequest request
    );

    @PostMapping(
            value = "/payments/custom-provider/disconnect",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    JsonNode disconnectCustomProviderConfig(
            @RequestHeader("Authorization") String authorization,
            @RequestHeader("Version") String version,
            @RequestParam("locationId") String locationId,
            @RequestBody GoHighLevelCustomProviderDisconnectRequest request
    );

}
