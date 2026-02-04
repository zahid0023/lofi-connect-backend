package org.example.loficonnect.feignclients;

import com.fasterxml.jackson.databind.JsonNode;
import org.example.loficonnect.commons.config.FeignLoggingConfig;
import org.example.loficonnect.dto.mapper.tags.GoHighLevelContactTagCreateRequest;
import org.example.loficonnect.dto.mapper.tags.GoHighLevelContactTagDeleteRequest;
import org.example.loficonnect.dto.mapper.tags.GoHighLevelTagCreateRequest;
import org.example.loficonnect.dto.mapper.tags.GoHighLevelTagUpdateRequest;
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

    @GetMapping(
            value = "/locations/{locationId}/tags",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    JsonNode getLocationTags(
            @RequestHeader("Authorization") String authorization,
            @RequestHeader("Version") String version,
            @PathVariable("locationId") String locationId
    );

    @PostMapping(
            value = "/locations/{locationId}/tags",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    JsonNode createLocationTag(
            @RequestHeader("Authorization") String authorization,
            @RequestHeader("Version") String version,
            @PathVariable("locationId") String locationId,
            @RequestBody GoHighLevelTagCreateRequest request
    );

    @GetMapping(
            value = "/locations/{locationId}/tags/{tagId}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    JsonNode getTagById(
            @RequestHeader("Authorization") String authorization,
            @RequestHeader("Version") String version,
            @PathVariable("locationId") String locationId,
            @PathVariable("tagId") String tagId
    );

    @PutMapping(
            value = "/locations/{locationId}/tags/{tagId}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    JsonNode updateTag(
            @RequestHeader("Authorization") String authorization,
            @RequestHeader("Version") String version,
            @PathVariable String locationId,
            @PathVariable String tagId,
            @RequestBody GoHighLevelTagUpdateRequest request
    );

    @DeleteMapping(
            value = "/locations/{locationId}/tags/{tagId}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    JsonNode deleteTag(
            @RequestHeader("Authorization") String authorization,
            @RequestHeader("Version") String version,
            @PathVariable String locationId,
            @PathVariable String tagId
    );



}
