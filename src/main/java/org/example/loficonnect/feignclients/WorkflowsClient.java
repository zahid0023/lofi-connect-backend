package org.example.loficonnect.feignclients;

import com.fasterxml.jackson.databind.JsonNode;
import org.example.loficonnect.config.FeignLoggingConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "workflowsClient", url = "https://services.leadconnectorhq.com", configuration = FeignLoggingConfig.class)
public interface WorkflowsClient {

    @GetMapping(
            value = "/workflows/",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    JsonNode getWorkflows(
            @RequestHeader("Authorization") String authorization,
            @RequestHeader("Version") String version,
            @RequestParam("locationId") String locationId
    );
}
