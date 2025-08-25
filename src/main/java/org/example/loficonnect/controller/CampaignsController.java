package org.example.loficonnect.controller;

import org.example.loficonnect.config.AppKey;
import org.example.loficonnect.dto.mapper.campaigns.ContactCampaignAssignRequest;
import org.example.loficonnect.service.CampaignsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1")
public class CampaignsController {
    private final CampaignsService campaignsService;

    public CampaignsController(CampaignsService campaignsService) {
        this.campaignsService = campaignsService;
    }

    @AppKey
    @PostMapping("/contacts/{contactId}/campaigns/{campaignId}")
    public ResponseEntity<?> assignContactToCampaign(
            @PathVariable("contactId") String contactId,
            @PathVariable("campaignId") String campaignId,
            @RequestBody ContactCampaignAssignRequest request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(campaignsService.assignContactToCampaign(contactId, campaignId, request));
    }

    @AppKey
    @DeleteMapping("/contacts/{contactId}/campaigns/{campaignId}")
    public ResponseEntity<?> removeContactFromCampaign(
            @PathVariable("contactId") String contactId,
            @PathVariable("campaignId") String campaignId
    ) {
        Map<String, Object> queryParams = new HashMap<>();
        return ResponseEntity.ok(campaignsService.removeContactFromCampaign(contactId, campaignId, queryParams));
    }

    @AppKey
    @DeleteMapping("/contacts/{contactId}/campaigns/removeAll")
    public ResponseEntity<?> removeAllCampaignsFromContact(
            @PathVariable("contactId") String contactId
    ) {
        Map<String, Object> queryParams = new HashMap<>();
        return ResponseEntity.ok(campaignsService.removeAllCampaignsFromContact(contactId, queryParams));
    }


}
