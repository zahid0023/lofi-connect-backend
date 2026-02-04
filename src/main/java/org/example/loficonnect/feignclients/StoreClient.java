package org.example.loficonnect.feignclients;

import com.fasterxml.jackson.databind.JsonNode;
import org.example.loficonnect.commons.config.FeignLoggingConfig;
import org.example.loficonnect.dto.mapper.store.GoHighLevelStoreUpdateRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@FeignClient(name = "storeClient", url = "https://services.leadconnectorhq.com", configuration = FeignLoggingConfig.class)
public interface StoreClient {

    @GetMapping(
        value = "/products/store/{storeId}/stats",
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    JsonNode getStoreStats(
        @RequestHeader("Authorization") String authorization,
        @RequestHeader("Version") String version,
        @PathVariable("storeId") String storeId,
        @RequestParam Map<String, Object> queryParams
    );

    @PostMapping(
            value = "/products/store/{storeId}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    JsonNode updateStoreProducts(
            @RequestHeader("Authorization") String authorization,
            @RequestHeader("Version") String version,
            @PathVariable("storeId") String storeId,
            @RequestBody GoHighLevelStoreUpdateRequest request
    );

}
