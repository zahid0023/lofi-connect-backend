package org.example.loficonnect.feignclients;

import com.fasterxml.jackson.databind.JsonNode;
import org.example.loficonnect.commons.config.FeignLoggingConfig;
import org.example.loficonnect.dto.request.campaigns.GoHighLevelContactCampaignAssignRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@FeignClient(name = "campaignsClient", url = "https://services.leadconnectorhq.com", configuration = FeignLoggingConfig.class)
public interface CampaignsClient {
    @PostMapping(
            value = "/contacts/{contactId}/campaigns/{campaignId}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    JsonNode assignContactToCampaign(
            @RequestHeader("Authorization") String authorization,
            @RequestHeader("Version") String version,
            @PathVariable("contactId") String contactId,
            @PathVariable("campaignId") String campaignId,
            @RequestBody GoHighLevelContactCampaignAssignRequest request
    );

    @DeleteMapping(
            value = "/contacts/{contactId}/campaigns/{campaignId}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    JsonNode removeContactFromCampaign(
            @RequestHeader("Authorization") String authorization,
            @RequestHeader("Version") String version,
            @PathVariable("contactId") String contactId,
            @PathVariable("campaignId") String campaignId,
            @RequestParam Map<String, Object> queryParams
    );
    @DeleteMapping(
            value = "/contacts/{contactId}/campaigns/removeAll",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    JsonNode removeAllCampaignsFromContact(
            @RequestHeader("Authorization") String authorization,
            @RequestHeader("Version") String version,
            @PathVariable("contactId") String contactId,
            @RequestParam Map<String, Object> queryParams
    );



}
