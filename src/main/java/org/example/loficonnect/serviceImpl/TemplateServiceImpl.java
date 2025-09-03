package org.example.loficonnect.serviceImpl;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import org.example.loficonnect.feignclients.TemplateClient;
import org.example.loficonnect.service.AuthorizationService;
import org.example.loficonnect.service.TemplateService;
import org.example.loficonnect.util.AppKeyContext;
import org.example.loficonnect.util.VersionContext;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@Slf4j
public class TemplateServiceImpl implements TemplateService {
    private final AuthorizationService authorizationService;
    private final TemplateClient templateClient;

    public TemplateServiceImpl(AuthorizationService authorizationService, TemplateClient templateClient) {
        this.authorizationService = authorizationService;
        this.templateClient = templateClient;
    }

    @Override
    public JsonNode getTemplates(String locationId, Map<String, Object> queryParams) {
        String accessKey = authorizationService.getAccessToken(AppKeyContext.getAppKey());
        String version = VersionContext.getVersion();
        return templateClient.getTemplates(accessKey, version, locationId, queryParams);
    }

    @Override
    public JsonNode deleteTemplate(String locationId, String templateId) {
        String accessKey = authorizationService.getAccessToken(AppKeyContext.getAppKey());
        String version = VersionContext.getVersion();
        return templateClient.deleteTemplate(accessKey, version, locationId, templateId);
    }


}
