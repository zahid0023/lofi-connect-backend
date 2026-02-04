package org.example.loficonnect.feignclients;

import com.fasterxml.jackson.databind.JsonNode;
import org.example.loficonnect.commons.config.FeignLoggingConfig;
import org.example.loficonnect.dto.mapper.relations.GoHighLevelCreateRelationRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "RelationsClient", url = "https://services.leadconnectorhq.com", configuration = FeignLoggingConfig.class)
public interface RelationsClient {

    @PostMapping(
        value = "/associations/relations",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    JsonNode createRelation(
        @RequestHeader("Authorization") String authorization,
        @RequestHeader("Version") String version,
        @RequestBody GoHighLevelCreateRelationRequest request
    );

    @GetMapping(
            value = "/associations/relations/{recordId}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    JsonNode getAllRelationsByRecordId(
            @RequestHeader("Authorization") String authorization,
            @RequestHeader("Version") String version,
            @PathVariable("recordId") String recordId,
            @RequestParam("associationIds") String[] associationIds,
            @RequestParam("limit") int limit,
            @RequestParam("locationId") String locationId,
            @RequestParam("skip") int skip
    );

    @DeleteMapping(
            value = "/associations/relations/{relationId}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    JsonNode deleteRelation(
            @RequestHeader("Authorization") String authorization,
            @RequestHeader("Version") String version,
            @PathVariable("relationId") String relationId,
            @RequestParam("locationId") String locationId
    );
}
