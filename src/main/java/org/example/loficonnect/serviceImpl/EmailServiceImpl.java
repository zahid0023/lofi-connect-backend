package org.example.loficonnect.serviceImpl;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import org.example.loficonnect.dto.mapper.emails.GoHighLevelEmailTemplateCreateRequest;
import org.example.loficonnect.dto.request.emails.EmailTemplateCreateRequest;
import org.example.loficonnect.feignclients.EmailClient;
import org.example.loficonnect.service.AuthorizationService;
import org.example.loficonnect.service.EmailService;
import org.example.loficonnect.util.AppKeyContext;
import org.example.loficonnect.util.VersionContext;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@Slf4j
public class EmailServiceImpl implements EmailService {
    private final AuthorizationService authorizationService;
    private final EmailClient emailClient;

    public EmailServiceImpl(AuthorizationService authorizationService, EmailClient emailClient) {
        this.authorizationService = authorizationService;
        this.emailClient = emailClient;
    }

    @Override
    public JsonNode getEmailById(String id) {
        String accessKey = authorizationService.getAccessToken(AppKeyContext.getAppKey());
        String version = VersionContext.getVersion();
        return emailClient.getEmailById(accessKey, version, id);
    }

    @Override
    public JsonNode cancelScheduledEmail(String emailMessageId) {
        String accessKey = authorizationService.getAccessToken(AppKeyContext.getAppKey());
        String version = VersionContext.getVersion();
        return emailClient.cancelScheduledEmail(accessKey, version, emailMessageId);
    }

    @Override
    public JsonNode getCampaigns(Map<String, Object> queryParams) {
        String accessKey = authorizationService.getAccessToken(AppKeyContext.getAppKey());
        String version = VersionContext.getVersion();
        return emailClient.getCampaigns(accessKey, version, queryParams);
    }

    @Override
    public JsonNode createEmailTemplate(EmailTemplateCreateRequest request) {
        String accessKey = authorizationService.getAccessToken(AppKeyContext.getAppKey());
        String version = VersionContext.getVersion();
        GoHighLevelEmailTemplateCreateRequest ghlRequest = GoHighLevelEmailTemplateCreateRequest.fromRequest(request);
        return emailClient.createEmailTemplate(accessKey, version, ghlRequest);
    }

    @Override
    public JsonNode fetchEmailTemplates(Boolean archived,
                                        String builderVersion,
                                        Integer limit,
                                        String name,
                                        Integer offset,
                                        String originId,
                                        String parentId,
                                        String search,
                                        String sortByDate,
                                        Boolean templatesOnly,
                                        String locationId) {

        String accessKey = authorizationService.getAccessToken(AppKeyContext.getAppKey());
        String version = VersionContext.getVersion();

        return emailClient.fetchEmailTemplates(
                accessKey,
                version,
                archived,
                builderVersion,
                limit,
                name,
                offset,
                originId,
                parentId,
                search,
                sortByDate,
                templatesOnly,
                locationId
        );
    }

    @Override
    public JsonNode deleteTemplate(String locationId, String templateId) {
        String accessKey = authorizationService.getAccessToken(AppKeyContext.getAppKey());
        String version = VersionContext.getVersion();
        return emailClient.deleteTemplate(accessKey, version, locationId, templateId);
    }


}
