package org.example.loficonnect.service;

import com.fasterxml.jackson.databind.JsonNode;
import org.example.loficonnect.dto.request.documents.DocumentSendRequest;

import java.util.Map;

public interface DocumentService {
    JsonNode getDocuments(Map<String, Object> queryParams);
    JsonNode sendDocument(DocumentSendRequest request);
}
