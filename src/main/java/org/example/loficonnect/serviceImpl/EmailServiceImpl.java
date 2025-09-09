package org.example.loficonnect.serviceImpl;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import org.example.loficonnect.feignclients.EmailClient;
import org.example.loficonnect.service.AuthorizationService;
import org.example.loficonnect.service.EmailService;
import org.example.loficonnect.util.AppKeyContext;
import org.example.loficonnect.util.VersionContext;
import org.springframework.stereotype.Service;

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


}
