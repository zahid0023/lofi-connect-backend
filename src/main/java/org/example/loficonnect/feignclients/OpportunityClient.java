package org.example.loficonnect.feignclients;

import com.fasterxml.jackson.databind.JsonNode;
import org.example.loficonnect.config.FeignLoggingConfig;
import org.example.loficonnect.dto.mapper.opportunity.GoHighLevelOpportunityCreateRequest;
import org.example.loficonnect.dto.mapper.opportunity.GoHighLevelOpportunityStatusUpdateRequest;
import org.example.loficonnect.dto.mapper.opportunity.GoHighLevelOpportunityUpdateRequest;
import org.example.loficonnect.dto.mapper.opportunity.GoHighLevelOpportunityUpsertRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "opportunityClient", url = "https://services.leadconnectorhq.com", configuration = FeignLoggingConfig.class)
public interface OpportunityClient {

    @GetMapping(
        value = "/opportunities/{id}",
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    JsonNode getOpportunityById(
        @RequestHeader("Authorization") String authorization,
        @RequestHeader("Version") String version,
        @PathVariable("id") String opportunityId
    );

    @DeleteMapping(
            value = "/opportunities/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    JsonNode deleteOpportunityById(
            @RequestHeader("Authorization") String authorization,
            @RequestHeader("Version") String version,
            @PathVariable("id") String opportunityId
    );

    @PutMapping(
            value = "/opportunities/{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    JsonNode updateOpportunityById(
            @RequestHeader("Authorization") String authorization,
            @RequestHeader("Version") String version,
            @PathVariable("id") String opportunityId,
            @RequestBody GoHighLevelOpportunityUpdateRequest request
    );

    @PutMapping(
            value = "/opportunities/{id}/status",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    JsonNode updateOpportunityStatus(
            @RequestHeader("Authorization") String authorization,
            @RequestHeader("Version") String version,
            @PathVariable("id") String opportunityId,
            @RequestBody GoHighLevelOpportunityStatusUpdateRequest request
    );

    @PostMapping(
            value = "/opportunities/upsert",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    JsonNode upsertOpportunity(
            @RequestHeader("Authorization") String authorization,
            @RequestHeader("Version") String version,
            @RequestBody GoHighLevelOpportunityUpsertRequest request
    );

    @PostMapping(
            value = "/opportunities",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    JsonNode createOpportunity(
            @RequestHeader("Authorization") String authorization,
            @RequestHeader("Version") String version,
            @RequestBody GoHighLevelOpportunityCreateRequest request
    );

}
