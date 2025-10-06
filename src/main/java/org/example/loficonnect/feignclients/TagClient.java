package org.example.loficonnect.feignclients;

import com.fasterxml.jackson.databind.JsonNode;
import org.example.loficonnect.config.FeignLoggingConfig;
import org.example.loficonnect.dto.mapper.tag.GoHighLevelTagByIdsRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@FeignClient(name = "tagClient", url = "https://services.leadconnectorhq.com", configuration = FeignLoggingConfig.class)
public interface TagClient {

    @GetMapping(
        value = "/social-media-posting/{locationId}/tags",
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    JsonNode getTags(
        @RequestHeader("Authorization") String authorization,
        @RequestHeader("Version") String version,
        @PathVariable("locationId") String locationId,
        @RequestParam Map<String, Object> queryParams
    );

    @PostMapping(
            value = "/social-media-posting/{locationId}/tags/details",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    JsonNode getTagsByIds(
            @RequestHeader("Authorization") String authorization,
            @RequestHeader("Version") String version,
            @PathVariable("locationId") String locationId,
            @RequestBody GoHighLevelTagByIdsRequest request
    );

}
