package org.example.loficonnect.feignclients;

import com.fasterxml.jackson.databind.JsonNode;
import org.example.loficonnect.config.FeignLoggingConfig;
import org.example.loficonnect.dto.mapper.integrations.GoHighLevelIntegrationCreateRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@FeignClient(name = "integrationClient", url = "https://services.leadconnectorhq.com", configuration = FeignLoggingConfig.class)
public interface IntegrationClient {

    @PostMapping(
            value = "/payments/integrations/provider/whitelabel",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    JsonNode createIntegration(
            @RequestHeader("Authorization") String authorization,
            @RequestHeader("Version") String version,
            @RequestBody GoHighLevelIntegrationCreateRequest request
    );

    @GetMapping(
            value = "/payments/integrations/provider/whitelabel",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    JsonNode getIntegrationProviders(
            @RequestHeader("Authorization") String authorization,
            @RequestHeader("Version") String version,
            @RequestParam Map<String, Object> queryParams
    );

}
