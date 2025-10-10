package org.example.loficonnect.service;

import com.fasterxml.jackson.databind.JsonNode;
import org.example.loficonnect.dto.request.documentstemplate.TemplateSendRequest;

import java.util.Map;

public interface DocumentsTemplateService {
    JsonNode getTemplates(Map<String, Object> queryParams);
    JsonNode sendTemplate(TemplateSendRequest request);
}
