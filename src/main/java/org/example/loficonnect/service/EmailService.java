package org.example.loficonnect.service;

import com.fasterxml.jackson.databind.JsonNode;
import org.example.loficonnect.dto.request.emails.EmailTemplateCreateRequest;

import java.util.Map;

public interface EmailService {
    JsonNode getEmailById(String id);

    JsonNode cancelScheduledEmail(String emailMessageId);

    JsonNode getCampaigns(Map<String, Object> queryParams);

    JsonNode createEmailTemplate(EmailTemplateCreateRequest request);

    JsonNode fetchEmailTemplates(Boolean archived,
                                 String builderVersion,
                                 Integer limit,
                                 String name,
                                 Integer offset,
                                 String originId,
                                 String parentId,
                                 String search,
                                 String sortByDate,
                                 Boolean templatesOnly,
                                 String locationId);

    JsonNode deleteTemplate(String locationId, String templateId);
}
