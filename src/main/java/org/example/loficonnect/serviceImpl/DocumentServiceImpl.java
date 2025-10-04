package org.example.loficonnect.serviceImpl;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import org.example.loficonnect.dto.mapper.documents.GoHighLevelDocumentSendRequest;
import org.example.loficonnect.dto.request.documents.DocumentSendRequest;
import org.example.loficonnect.feignclients.DocumentClient;
import org.example.loficonnect.service.AuthorizationService;
import org.example.loficonnect.service.DocumentService;
import org.example.loficonnect.util.AppKeyContext;
import org.example.loficonnect.util.VersionContext;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@Slf4j
public class DocumentServiceImpl implements DocumentService {

    private final DocumentClient documentClient;
    private final AuthorizationService authorizationService;

    public DocumentServiceImpl(DocumentClient documentClient, AuthorizationService authorizationService) {
        this.documentClient = documentClient;
        this.authorizationService = authorizationService;
    }

    @Override
    public JsonNode getDocuments(Map<String, Object> queryParams) {
        String accessKey = authorizationService.getAccessToken(AppKeyContext.getAppKey());
        String version = VersionContext.getVersion();
        return documentClient.getDocuments(accessKey, version, queryParams);
    }

    @Override
    public JsonNode sendDocument(DocumentSendRequest request) {
        String accessKey = authorizationService.getAccessToken(AppKeyContext.getAppKey());
        String version = VersionContext.getVersion();
        GoHighLevelDocumentSendRequest ghlRequest = GoHighLevelDocumentSendRequest.fromRequest(request);
        return documentClient.sendDocument(accessKey, version, ghlRequest);
    }

}
