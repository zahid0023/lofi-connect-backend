package org.example.loficonnect.serviceImpl;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import org.example.loficonnect.dto.mapper.forms.GoHighLevelFileUploadRequest;
import org.example.loficonnect.dto.request.forms.FileUploadRequest;
import org.example.loficonnect.feignclients.FormsClient;
import org.example.loficonnect.service.AuthorizationService;
import org.example.loficonnect.service.FormService;
import org.example.loficonnect.util.AppKeyContext;
import org.example.loficonnect.util.VersionContext;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@Slf4j
public class FormServiceImpl implements FormService {

    private final FormsClient formsClient;
    private final AuthorizationService authorizationService;

    public FormServiceImpl(AuthorizationService authorizationService, FormsClient formsClient) {
        this.authorizationService = authorizationService;
        this.formsClient = formsClient;
    }

    @Override
    public JsonNode getFormSubmissions(Map<String, Object> queryParams) {
        String accessKey = authorizationService.getAccessToken(AppKeyContext.getAppKey());
        String version = VersionContext.getVersion();
        return formsClient.getFormSubmissions(accessKey, version, queryParams);
    }

    @Override
    public JsonNode uploadCustomFiles(FileUploadRequest request) {
        String accessKey = authorizationService.getAccessToken(AppKeyContext.getAppKey());
        String version = VersionContext.getVersion();
        GoHighLevelFileUploadRequest ghlRequest = GoHighLevelFileUploadRequest.fromRequest(request);
        return formsClient.uploadCustomFiles(accessKey, version, Map.of(), ghlRequest);
    }

    @Override
    public JsonNode getForms(Map<String, Object> queryParams) {
        String accessKey = authorizationService.getAccessToken(AppKeyContext.getAppKey());
        String version = VersionContext.getVersion();
        return formsClient.getForms(accessKey, version, queryParams);
    }
}
