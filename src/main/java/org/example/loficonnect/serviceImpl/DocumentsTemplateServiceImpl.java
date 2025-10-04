package org.example.loficonnect.serviceImpl;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import org.example.loficonnect.dto.mapper.documentstemplate.GoHighLevelTemplateSendRequest;
import org.example.loficonnect.dto.request.documentstemplate.TemplateSendRequest;
import org.example.loficonnect.feignclients.DocumentsTemplateClient;
import org.example.loficonnect.service.AuthorizationService;
import org.example.loficonnect.service.DocumentsTemplateService;
import org.example.loficonnect.util.AppKeyContext;
import org.example.loficonnect.util.VersionContext;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@Slf4j
public class DocumentsTemplateServiceImpl implements DocumentsTemplateService {

    private final DocumentsTemplateClient documentsTemplateClient;
    private final AuthorizationService authorizationService;

    public DocumentsTemplateServiceImpl(DocumentsTemplateClient documentsTemplateClient, AuthorizationService authorizationService) {
        this.documentsTemplateClient = documentsTemplateClient;
        this.authorizationService = authorizationService;
    }

    @Override
    public JsonNode getTemplates(Map<String, Object> queryParams) {
        String accessKey = authorizationService.getAccessToken(AppKeyContext.getAppKey());
        String version = VersionContext.getVersion();
        return documentsTemplateClient.getTemplates(accessKey, version, queryParams);
    }

    @Override
    public JsonNode sendTemplate(TemplateSendRequest request) {
        String accessKey = authorizationService.getAccessToken(AppKeyContext.getAppKey());
        String version = VersionContext.getVersion();
        GoHighLevelTemplateSendRequest ghlRequest = GoHighLevelTemplateSendRequest.fromRequest(request);
        return documentsTemplateClient.sendTemplate(accessKey, version, ghlRequest);
    }

}
