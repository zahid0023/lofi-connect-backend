package org.example.loficonnect.feignclients;

import com.fasterxml.jackson.databind.JsonNode;
import org.example.loficonnect.config.AppKey;
import org.example.loficonnect.config.FeignLoggingConfig;
import org.example.loficonnect.dto.mapper.objectschema.GoHighLevelCreateCustomObjectRequest;
import org.example.loficonnect.dto.mapper.objectschema.GoHighLevelUpdateObjectSchemaRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "objectsClient", url = "https://services.leadconnectorhq.com", configuration = FeignLoggingConfig.class)
public interface ObjectSchemaClient {
    
    @GetMapping(
        value = "/objects/{key}",
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    JsonNode getObjectSchema(
        @RequestHeader("Authorization") String authorization,
        @RequestHeader("Version") String version,
        @PathVariable("key") String key,
        @RequestParam("fetchProperties") String fetchProperties,
        @RequestParam("locationId") String locationId
    );

    @PutMapping(
            value = "/objects/{key}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    JsonNode updateObjectSchema(
            @RequestHeader("Authorization") String authorization,
            @RequestHeader("Version") String version,
            @PathVariable("key") String key,
            @RequestBody GoHighLevelUpdateObjectSchemaRequest request
    );

    @GetMapping(
            value = "/objects",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    JsonNode getAllObjects(
            @RequestHeader("Authorization") String authorization,
            @RequestHeader("Version") String version,
            @RequestParam("locationId") String locationId
    );

    @AppKey
    @PostMapping(
            value = "/objects",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    JsonNode createCustomObject(
            @RequestHeader("Authorization") String authorization,
            @RequestHeader("Version") String version,
            @RequestBody GoHighLevelCreateCustomObjectRequest request
    );
}
