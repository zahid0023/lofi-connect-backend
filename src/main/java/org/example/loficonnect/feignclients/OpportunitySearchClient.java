package org.example.loficonnect.feignclients;

import com.fasterxml.jackson.databind.JsonNode;
import org.example.loficonnect.config.FeignLoggingConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@FeignClient(name = "opportunitySearchClient", url = "https://services.leadconnectorhq.com", configuration = FeignLoggingConfig.class)
public interface OpportunitySearchClient {

    @GetMapping(
        value = "/opportunities/search",
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    JsonNode searchOpportunities(
        @RequestHeader("Authorization") String authorization,
        @RequestHeader("Version") String version,
        @RequestParam Map<String, Object> queryParams
    );
}
