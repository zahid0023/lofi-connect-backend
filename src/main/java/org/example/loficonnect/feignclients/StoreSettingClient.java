package org.example.loficonnect.feignclients;

import com.fasterxml.jackson.databind.JsonNode;
import org.example.loficonnect.config.FeignLoggingConfig;
import org.example.loficonnect.dto.mapper.storesetting.GoHighLevelStoreSettingCreateRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@FeignClient(name = "storeSettingClient", url = "https://services.leadconnectorhq.com", configuration = FeignLoggingConfig.class)
public interface StoreSettingClient {

    @PostMapping(
        value = "/store/store-setting",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    JsonNode createOrUpdateStoreSetting(
        @RequestHeader("Authorization") String authorization,
        @RequestHeader("Version") String version,
        @RequestBody GoHighLevelStoreSettingCreateRequest request
    );

    @GetMapping(
            value = "/store/store-setting",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    JsonNode getStoreSetting(
            @RequestHeader("Authorization") String authorization,
            @RequestHeader("Version") String version,
            @RequestParam Map<String, Object> queryParams
    );

}
