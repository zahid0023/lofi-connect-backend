package org.example.loficonnect.feignclients;

import com.fasterxml.jackson.databind.JsonNode;
import org.example.loficonnect.config.FeignLoggingConfig;
import org.example.loficonnect.dto.mapper.redirect.GoHighLevelRedirectCreateRequest;
import org.example.loficonnect.dto.mapper.redirect.GoHighLevelRedirectUpdateRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@FeignClient(name = "redirectClient", url = "https://services.leadconnectorhq.com", configuration = FeignLoggingConfig.class)
public interface RedirectClient {

    @PostMapping(
        value = "/funnels/lookup/redirect",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    JsonNode createRedirect(
        @RequestHeader("Authorization") String authorization,
        @RequestHeader("Version") String version,
        @RequestBody GoHighLevelRedirectCreateRequest request
    );

    @PatchMapping(
            value = "/funnels/lookup/redirect/{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    JsonNode updateRedirect(
            @RequestHeader("Authorization") String authorization,
            @RequestHeader("Version") String version,
            @PathVariable("id") String id,
            @RequestBody GoHighLevelRedirectUpdateRequest request
    );

    @DeleteMapping(
            value = "/funnels/lookup/redirect/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    JsonNode deleteRedirectById(
            @RequestHeader("Authorization") String authorization,
            @RequestHeader("Version") String version,
            @PathVariable("id") String id,
            @RequestParam Map<String, Object> queryParams
    );

    @GetMapping(
            value = "/funnels/lookup/redirect/list",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    JsonNode getRedirectList(
            @RequestHeader("Authorization") String authorization,
            @RequestHeader("Version") String version,
            @RequestParam Map<String, Object> queryParams
    );

}
