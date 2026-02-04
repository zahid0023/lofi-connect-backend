package org.example.loficonnect.feignclients;

import com.fasterxml.jackson.databind.JsonNode;
import org.example.loficonnect.commons.config.FeignLoggingConfig;
import org.example.loficonnect.dto.mapper.custom.value.GoHighLevelCustomValueCreateRequest;
import org.example.loficonnect.dto.mapper.custom.value.GoHighLevelCustomValueUpdateRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "customValueClient", url = "https://services.leadconnectorhq.com", configuration = FeignLoggingConfig.class)
public interface CustomValueClient {
    @GetMapping(
            value = "/locations/{location-id}/customValues",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    JsonNode getCustomValues(
            @RequestHeader("Authorization") String authorization,
            @RequestHeader("Version") String version,
            @PathVariable("location-id") String locationId
    );

    @PostMapping(
            value = "/locations/{location-id}/customValues",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    JsonNode createCustomValue(
            @RequestHeader("Authorization") String authorization,
            @RequestHeader("Version") String version,
            @PathVariable("location-id") String locationId,
            @RequestBody GoHighLevelCustomValueCreateRequest request
    );

    @GetMapping(
            value = "/locations/{location-id}/customValues/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    JsonNode getCustomValue(
            @RequestHeader("Authorization") String authorization,
            @RequestHeader("Version") String version,
            @PathVariable("location-id") String locationId,
            @PathVariable("id") String id
    );

    @PutMapping(
            value = "/locations/{location-id}/customValues/{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    JsonNode updateCustomValue(
            @RequestHeader("Authorization") String authorization,
            @RequestHeader("Version") String version,
            @PathVariable("location-id") String locationId,
            @PathVariable("id") String id,
            @RequestBody GoHighLevelCustomValueUpdateRequest request
    );

    @DeleteMapping(
            value = "/locations/{location-id}/customValues/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    JsonNode deleteCustomValue(
            @RequestHeader("Authorization") String authorization,
            @RequestHeader("Version") String version,
            @PathVariable("location-id") String locationId,
            @PathVariable("id") String id
    );

}
