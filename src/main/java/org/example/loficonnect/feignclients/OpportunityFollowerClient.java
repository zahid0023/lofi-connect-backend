package org.example.loficonnect.feignclients;

import com.fasterxml.jackson.databind.JsonNode;
import org.example.loficonnect.commons.config.FeignLoggingConfig;
import org.example.loficonnect.dto.mapper.opportunityfollower.GoHighLevelOpportunityFollowerRequest;
import org.example.loficonnect.dto.mapper.opportunityfollower.GoHighLevelOpportunityRemoveFollowersRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "opportunityFollowerClient", url = "https://services.leadconnectorhq.com", configuration = FeignLoggingConfig.class)
public interface OpportunityFollowerClient {

    @PostMapping(
        value = "/opportunities/{id}/followers",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    JsonNode addFollowers(
        @RequestHeader("Authorization") String authorization,
        @RequestHeader("Version") String version,
        @PathVariable("id") String opportunityId,
        @RequestBody GoHighLevelOpportunityFollowerRequest request
    );

    @DeleteMapping(
            value = "/opportunities/{id}/followers",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    JsonNode removeFollowers(
            @RequestHeader("Authorization") String authorization,
            @RequestHeader("Version") String version,
            @PathVariable("id") String opportunityId,
            @RequestBody GoHighLevelOpportunityRemoveFollowersRequest request
    );

}
