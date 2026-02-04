package org.example.loficonnect.feignclients;

import com.fasterxml.jackson.databind.JsonNode;
import org.example.loficonnect.commons.config.FeignLoggingConfig;
import org.example.loficonnect.dto.mapper.collections.GoHighLevelCollectionCreateRequest;
import org.example.loficonnect.dto.mapper.collections.GoHighLevelCollectionUpdateRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@FeignClient(name = "collectionClient", url = "https://services.leadconnectorhq.com", configuration = FeignLoggingConfig.class)
public interface CollectionClient {

    @GetMapping(
            value = "/products/collections",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    JsonNode getProductCollections(
            @RequestHeader("Authorization") String authorization,
            @RequestHeader("Version") String version,
            @RequestParam Map<String, Object> queryParams
    );

    @PostMapping(
            value = "/products/collections",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    JsonNode createCollection(
            @RequestHeader("Authorization") String authorization,
            @RequestHeader("Version") String version,
            @RequestBody GoHighLevelCollectionCreateRequest request
    );

    @GetMapping(
            value = "/products/collections/{collectionId}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    JsonNode getCollectionById(
            @RequestHeader("Authorization") String authorization,
            @RequestHeader("Version") String version,
            @PathVariable("collectionId") String collectionId
    );

    @PutMapping(
            value = "/products/collections/{collectionId}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    JsonNode updateCollection(
            @RequestHeader("Authorization") String authorization,
            @RequestHeader("Version") String version,
            @PathVariable("collectionId") String collectionId,
            @RequestBody GoHighLevelCollectionUpdateRequest request
    );

    @DeleteMapping(
            value = "/products/collections/{collectionId}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    JsonNode deleteCollection(
            @RequestHeader("Authorization") String authorization,
            @RequestHeader("Version") String version,
            @PathVariable("collectionId") String collectionId,
            @RequestParam Map<String, Object> queryParams
    );

}
