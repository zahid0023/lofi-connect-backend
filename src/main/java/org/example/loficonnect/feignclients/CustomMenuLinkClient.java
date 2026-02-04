package org.example.loficonnect.feignclients;

import com.fasterxml.jackson.databind.JsonNode;
import org.example.loficonnect.commons.config.FeignLoggingConfig;
import org.example.loficonnect.dto.mapper.custommenulink.GoHighLevelCustomMenuLinkCreateRequest;
import org.example.loficonnect.dto.mapper.custommenulink.GoHighLevelCustomMenuLinkUpdateRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@FeignClient(name = "customMenuLinkClient", url = "https://services.leadconnectorhq.com", configuration = FeignLoggingConfig.class)
public interface CustomMenuLinkClient {

    @GetMapping(
        value = "/custom-menus/{customMenuId}",
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    JsonNode getCustomMenuLink(
        @RequestHeader("Authorization") String authorization,
        @RequestHeader("Version") String version,
        @PathVariable("customMenuId") String customMenuId
    );

    @DeleteMapping(
            value = "/custom-menus/{customMenuId}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    JsonNode deleteCustomMenuLink(
            @RequestHeader("Authorization") String authorization,
            @RequestHeader("Version") String version,
            @PathVariable("customMenuId") String customMenuId
    );

    @PutMapping(
            value = "/custom-menus/{customMenuId}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    JsonNode updateCustomMenuLink(
            @RequestHeader("Authorization") String authorization,
            @RequestHeader("Version") String version,
            @PathVariable("customMenuId") String customMenuId,
            @RequestBody GoHighLevelCustomMenuLinkUpdateRequest request
    );

    @GetMapping(
            value = "/custom-menus/",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    JsonNode getCustomMenuLinks(
            @RequestHeader("Authorization") String authorization,
            @RequestHeader("Version") String version,
            @RequestParam Map<String, Object> queryParams
    );

    @PostMapping(
            value = "/custom-menus/",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    JsonNode createCustomMenuLink(
            @RequestHeader("Authorization") String authorization,
            @RequestHeader("Version") String version,
            @RequestBody GoHighLevelCustomMenuLinkCreateRequest request
    );

}
