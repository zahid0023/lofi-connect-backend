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
@RequestMapping("/api/v1/ghl")
public class CampaignsController {
    private final CampaignsService campaignsService;

    public CampaignsController(CampaignsService campaignsService) {
        this.campaignsService = campaignsService;
    }

    @AppKey
    @PostMapping("/contacts/{contact-id}/campaigns/{campaign-id}")
    public ResponseEntity<?> addContactToCampaign(
            @PathVariable("contact-id") String contactId,
            @PathVariable("campaign-id") String campaignId,
            @RequestBody ContactCampaignAssignRequest request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(campaignsService.assignContactToCampaign(contactId, campaignId, request));
    }

    @AppKey
    @DeleteMapping("/contacts/{contact-id}/campaigns/{campaign-id}")
    public ResponseEntity<?> removeContactFromCampaign(
            @PathVariable("contact-id") String contactId,
            @PathVariable("campaign-id") String campaignId
    ) {
        Map<String, Object> queryParams = new HashMap<>();
        return ResponseEntity.ok(campaignsService.removeContactFromCampaign(contactId, campaignId, queryParams));
    }

    @AppKey
    @DeleteMapping("/contacts/{contact-id}/campaigns/removeAll")
    public ResponseEntity<?> removeAllCampaignsFromContact(
            @PathVariable("contact-id") String contactId
    ) {
        Map<String, Object> queryParams = new HashMap<>();
        return ResponseEntity.ok(campaignsService.removeAllCampaignsFromContact(contactId, queryParams));
    }
}
