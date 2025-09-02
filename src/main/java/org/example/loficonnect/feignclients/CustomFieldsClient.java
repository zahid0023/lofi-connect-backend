package org.example.loficonnect.feignclients;

import com.fasterxml.jackson.databind.JsonNode;
import org.example.loficonnect.config.FeignLoggingConfig;
import org.example.loficonnect.dto.mapper.custom.fields.GoHighLevelCustomFieldCreateRequest;
import org.example.loficonnect.dto.mapper.custom.fields.GoHighLevelCustomFieldUpdateRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@FeignClient(name = "customFieldsClient", url = "https://services.leadconnectorhq.com", configuration = FeignLoggingConfig.class)
public interface CustomFieldsClient {
    @GetMapping(
            value = "/locations/{location-id}/customFields"
    )
    JsonNode getCustomFields(
            @RequestHeader("Authorization") String authorization,
            @RequestHeader("Version") String version,
            @PathVariable("location-id") String locationId,
            @RequestParam Map<String, Object> queryParams
    );

    @PostMapping(
            value = "/locations/{location-id}/customFields",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    JsonNode createCustomField(
            @RequestHeader("Authorization") String authorization,
            @RequestHeader("Version") String version,
            @PathVariable("location-id") String locationId,
            @RequestBody GoHighLevelCustomFieldCreateRequest request
    );

    @GetMapping(
            value = "/locations/{location-id}/customFields/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    JsonNode getCustomField(
            @RequestHeader("Authorization") String authorization,
            @RequestHeader("Version") String version,
            @PathVariable("location-id") String locationId,
            @PathVariable("id") String id
    );

    @PutMapping(
            value = "/locations/{location-id}/customFields/{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    JsonNode updateCustomField(
            @RequestHeader("Authorization") String authorization,
            @RequestHeader("Version") String version,
            @PathVariable("location-id") String locationId,
            @PathVariable("id") String id,
            @RequestBody GoHighLevelCustomFieldUpdateRequest request
    );

    @DeleteMapping(
            value = "/locations/{location-id}/customFields/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    JsonNode deleteCustomField(
            @RequestHeader("Authorization") String authorization,
            @RequestHeader("Version") String version,
            @PathVariable("location-id") String locationId,
            @PathVariable("id") String id
    );

    @PostMapping(value = "/locations/{locationId}/customFields/upload", consumes = "multipart/form-data")
    JsonNode uploadCustomFieldFile(
            @RequestHeader("Authorization") String accessToken,
            @RequestHeader("Version") String version,
            @PathVariable("locationId") String locationId,
            @RequestPart("id") String id,
            @RequestPart("maxFiles") Integer maxFiles,
            @RequestPart("file") MultipartFile file
    );


}
