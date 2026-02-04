package org.example.loficonnect.feignclients;

import com.fasterxml.jackson.databind.JsonNode;
import org.example.loficonnect.commons.config.FeignLoggingConfig;
import org.example.loficonnect.dto.mapper.triggerlinks.GoHighLevelLinkCreateRequest;
import org.example.loficonnect.dto.mapper.triggerlinks.GoHighLevelLinkUpdateRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@FeignClient(name = "linksClient", url = "https://services.leadconnectorhq.com", configuration = FeignLoggingConfig.class)
public interface TriggerLinksClient {

    @PutMapping(
        value = "/links/{linkId}",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    JsonNode updateLink(
        @RequestHeader("Authorization") String authorization,
        @RequestHeader("Version") String version,
        @PathVariable("linkId") String linkId,
        @RequestBody GoHighLevelLinkUpdateRequest request
    );

    @DeleteMapping(
            value = "/links/{linkId}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    JsonNode deleteLink(
            @RequestHeader("Authorization") String authorization,
            @RequestHeader("Version") String version,
            @PathVariable("linkId") String linkId
    );

    @GetMapping(
            value = "/links/",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    JsonNode getLinks(
            @RequestHeader("Authorization") String authorization,
            @RequestHeader("Version") String version,
            @RequestParam Map<String, Object> queryParams
    );

    @PostMapping(
            value = "/links/",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    JsonNode createLink(
            @RequestHeader("Authorization") String authorization,
            @RequestHeader("Version") String version,
            @RequestBody GoHighLevelLinkCreateRequest request
    );
}
