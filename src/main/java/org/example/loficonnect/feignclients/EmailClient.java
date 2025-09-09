package org.example.loficonnect.feignclients;

import com.fasterxml.jackson.databind.JsonNode;
import org.example.loficonnect.config.FeignLoggingConfig;
import org.example.loficonnect.dto.mapper.emails.GoHighLevelEmailTemplateCreateRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@FeignClient(name = "emailClient", url = "https://services.leadconnectorhq.com", configuration = FeignLoggingConfig.class)
public interface EmailClient {
    @GetMapping(
            value = "/conversations/messages/email/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    JsonNode getEmailById(
            @RequestHeader("Authorization") String authorization,
            @RequestHeader("Version") String version,
            @PathVariable("id") String id
    );

    @DeleteMapping(
            value = "/conversations/messages/email/{emailMessageId}/schedule",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    JsonNode cancelScheduledEmail(
            @RequestHeader("Authorization") String authorization,
            @RequestHeader("Version") String version,
            @PathVariable("emailMessageId") String emailMessageId
    );

    @GetMapping(
            value = "/emails/schedule",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    JsonNode getCampaigns(
            @RequestHeader("Authorization") String authorization,
            @RequestHeader("Version") String version,
            @RequestParam Map<String, Object> queryParams
    );

    @PostMapping(
            value = "/emails/builder",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    JsonNode createEmailTemplate(
            @RequestHeader("Authorization") String authorization,
            @RequestHeader("Version") String version,
            @RequestBody GoHighLevelEmailTemplateCreateRequest request
    );

    @GetMapping("/emails/builder")
    JsonNode fetchEmailTemplates(
            @RequestHeader("Authorization") String authorization,
            @RequestHeader("Version") String version,
            @RequestParam("archived") Boolean archived,
            @RequestParam("builderVersion") String builderVersion,
            @RequestParam("limit") Integer limit,
            @RequestParam(value = "name", required = false) String name,
            @RequestParam("offset") Integer offset,
            @RequestParam(value = "originId", required = false) String originId,
            @RequestParam(value = "parentId", required = false) String parentId,
            @RequestParam(value = "search", required = false) String search,
            @RequestParam("sortByDate") String sortByDate,
            @RequestParam("templatesOnly") Boolean templatesOnly,
            @RequestParam("locationId") String locationId
    );

    @DeleteMapping("/emails/builder/{locationId}/{templateId}")
    JsonNode deleteTemplate(
            @RequestHeader("Authorization") String authorization,
            @RequestHeader("Version") String version,
            @PathVariable("locationId") String locationId,
            @PathVariable("templateId") String templateId
    );




}
