package org.example.loficonnect.feignclients;

import com.fasterxml.jackson.databind.JsonNode;
import org.example.loficonnect.config.FeignLoggingConfig;
import org.example.loficonnect.dto.mapper.subaccount.GoHighLevelLocationCreateRequest;
import org.example.loficonnect.dto.mapper.subaccount.GoHighLevelLocationUpdateRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@FeignClient(name = "subAccountClient", url = "https://services.leadconnectorhq.com", configuration = FeignLoggingConfig.class)
public interface SubAccountClient {
    @GetMapping(
            value = "/locations/{locationId}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    JsonNode getLocation(
            @RequestHeader("Authorization") String authorization,
            @RequestHeader("Version") String version,
            @PathVariable("locationId") String locationId
    );

    @PutMapping(
            value = "/locations/{locationId}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    JsonNode updateLocation(
            @RequestHeader("Authorization") String authorization,
            @RequestHeader("Version") String version,
            @PathVariable("locationId") String locationId,
            @RequestBody GoHighLevelLocationUpdateRequest request
    );

    @DeleteMapping(
            value = "/locations/{locationId}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    JsonNode deleteLocation(
            @RequestHeader("Authorization") String authorization,
            @RequestHeader("Version") String version,
            @PathVariable("locationId") String locationId,
            @RequestParam Map<String, Object> queryParams
    );

    @PostMapping(
            value = "/locations/",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    JsonNode createLocation(
            @RequestHeader("Authorization") String authorization,
            @RequestHeader("Version") String version,
            @RequestBody GoHighLevelLocationCreateRequest request
    );



}
