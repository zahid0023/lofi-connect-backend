package org.example.loficonnect.serviceImpl;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import org.example.loficonnect.dto.mapper.emailverification.GoHighLevelEmailVerificationRequest;
import org.example.loficonnect.dto.request.emailverification.EmailVerificationRequest;
import org.example.loficonnect.feignclients.EmailVerificationClient;
import org.example.loficonnect.service.AuthorizationService;
import org.example.loficonnect.service.EmailVerificationService;
import org.example.loficonnect.util.AppKeyContext;
import org.example.loficonnect.util.VersionContext;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@Slf4j
public class EmailVerificationServiceImpl implements EmailVerificationService {

    private final EmailVerificationClient emailVerificationClient;
    private final AuthorizationService authorizationService;

    public EmailVerificationServiceImpl(EmailVerificationClient emailVerificationClient, AuthorizationService authorizationService) {
        this.emailVerificationClient = emailVerificationClient;
        this.authorizationService = authorizationService;
    }

    @Override
    public JsonNode verifyEmail(Map<String, Object> queryParams, EmailVerificationRequest request) {
        String accessKey = authorizationService.getAccessToken(AppKeyContext.getAppKey());
        String version = VersionContext.getVersion();
        GoHighLevelEmailVerificationRequest ghlRequest = GoHighLevelEmailVerificationRequest.fromRequest(request);
        return emailVerificationClient.verifyEmail(accessKey, version, queryParams, ghlRequest);
    }
}
