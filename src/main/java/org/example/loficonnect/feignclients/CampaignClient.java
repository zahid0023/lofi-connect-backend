package org.example.loficonnect.feignclients;

import com.fasterxml.jackson.databind.JsonNode;
import org.example.loficonnect.config.FeignLoggingConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@FeignClient(name = "campaignClient", url = "https://services.leadconnectorhq.com", configuration = FeignLoggingConfig.class)
public interface CampaignClient {
    @GetMapping(
            value = "/campaigns",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    JsonNode getCampaigns(
            @RequestHeader("Authorization") String authorization,
            @RequestHeader("Version") String version,
            @RequestParam Map<String, Object> queryParams
    );

}
