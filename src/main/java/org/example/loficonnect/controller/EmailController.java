package org.example.loficonnect.controller;

import org.example.loficonnect.config.AppKey;
import org.example.loficonnect.dto.request.emails.EmailTemplateCreateRequest;
import org.example.loficonnect.service.EmailService;
import org.example.loficonnect.util.MapUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1")
public class EmailController {
    private final EmailService emailService;

    public EmailController(EmailService emailService) {
        this.emailService = emailService;
    }

    @AppKey
    @GetMapping("/conversations/messages/email/{id}")
    public ResponseEntity<?> getEmailById(@PathVariable("id") String id) {
        return ResponseEntity.ok(emailService.getEmailById(id));
    }

    @AppKey
    @DeleteMapping("/conversations/messages/email/{email-message-id}/schedule")
    public ResponseEntity<?> cancelScheduledEmail(@PathVariable("email-message-id") String emailMessageId) {
        return ResponseEntity.ok(emailService.cancelScheduledEmail(emailMessageId));
    }

    @AppKey
    @GetMapping("/emails/schedule")
    public ResponseEntity<?> getCampaigns(
            @RequestParam(value = "archived", required = false) Boolean archived,
            @RequestParam(value = "campaignsOnly", required = false) Boolean campaignsOnly,
            @RequestParam(value = "emailStatus", required = false, defaultValue = "complete") String emailStatus,
            @RequestParam(value = "limit", required = false) Integer limit,
            @RequestParam(value = "limitedFields", required = false) Boolean limitedFields,
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "offset", required = false) Integer offset,
            @RequestParam(value = "parentId", required = false) String parentId,
            @RequestParam(value = "showStats", required = false) Boolean showStats,
            @RequestParam(value = "status", required = false, defaultValue = "active") String status,
            @RequestParam("locationId") String locationId
    ) {
        Map<String, Object> queryParams = new HashMap<>();
        MapUtil.putIfNotNull(queryParams, "archived", archived);
        MapUtil.putIfNotNull(queryParams, "campaignsOnly", campaignsOnly);
        MapUtil.putIfNotNull(queryParams, "emailStatus", emailStatus);
        MapUtil.putIfNotNull(queryParams, "limit", limit);
        MapUtil.putIfNotNull(queryParams, "limitedFields", limitedFields);
        MapUtil.putIfNotNull(queryParams, "name", name);
        MapUtil.putIfNotNull(queryParams, "offset", offset);
        MapUtil.putIfNotNull(queryParams, "parentId", parentId);
        MapUtil.putIfNotNull(queryParams, "showStats", showStats);
        MapUtil.putIfNotNull(queryParams, "status", status);
        MapUtil.putIfNotNull(queryParams, "locationId", locationId);

        return ResponseEntity.ok(emailService.getCampaigns(queryParams));
    }

    @AppKey
    @PostMapping("/builder")
    public ResponseEntity<?> createEmailTemplate(@RequestBody EmailTemplateCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(emailService.createEmailTemplate(request));
    }

    @GetMapping("/email/builder")
    public ResponseEntity<?> fetchEmailTemplates(
            @RequestParam Boolean archived,
            @RequestParam String builderVersion,
            @RequestParam Integer limit,
            @RequestParam(required = false) String name,
            @RequestParam Integer offset,
            @RequestParam(required = false) String originId,
            @RequestParam(required = false) String parentId,
            @RequestParam(required = false) String search,
            @RequestParam String sortByDate,
            @RequestParam Boolean templatesOnly,
            @RequestParam String locationId
    ) {
        return ResponseEntity.ok(emailService.fetchEmailTemplates(
                archived, builderVersion, limit, name, offset,
                originId, parentId, search, sortByDate, templatesOnly, locationId
        ));
    }

    @AppKey
    @DeleteMapping("/{locationId}/{templateId}")
    public ResponseEntity<?> deleteTemplate(
            @PathVariable("locationId") String locationId,
            @PathVariable("templateId") String templateId
    ) {
        return ResponseEntity.ok(emailService.deleteTemplate(locationId, templateId));
    }


}
