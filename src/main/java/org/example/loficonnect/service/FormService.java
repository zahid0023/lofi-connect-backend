package org.example.loficonnect.service;

import com.fasterxml.jackson.databind.JsonNode;
import org.example.loficonnect.dto.request.forms.FileUploadRequest;

import java.util.Map;

public interface FormService {
    JsonNode getFormSubmissions(Map<String, Object> queryParams);
    JsonNode uploadCustomFiles(FileUploadRequest request);
    JsonNode getForms(Map<String, Object> queryParams);
}
