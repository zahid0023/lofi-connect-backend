package org.example.loficonnect.feignclients;

import com.fasterxml.jackson.databind.JsonNode;
import org.example.loficonnect.config.FeignLoggingConfig;
import org.example.loficonnect.dto.mapper.tags.GoHighLevelContactTagCreateRequest;
import org.example.loficonnect.dto.mapper.tags.GoHighLevelContactTagDeleteRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "tagsClient", url = "https://services.leadconnectorhq.com", configuration = FeignLoggingConfig.class)
public interface TagsClient {
    @PostMapping(
            value = "/contacts/{contactId}/tags",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    JsonNode createContactTags(
            @RequestHeader("Authorization") String authorization,
            @RequestHeader("Version") String version,
            @PathVariable("contactId") String contactId,
            @RequestBody GoHighLevelContactTagCreateRequest request
    );

    @DeleteMapping(
            value = "/contacts/{contactId}/tags",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    JsonNode deleteContactTags(
            @RequestHeader("Authorization") String authorization,
            @RequestHeader("Version") String version,
            @PathVariable("contactId") String contactId,
            @RequestBody GoHighLevelContactTagDeleteRequest request
    );


}
