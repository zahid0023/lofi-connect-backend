package org.example.loficonnect.service;

import com.fasterxml.jackson.databind.JsonNode;
import org.example.loficonnect.dto.request.medialibrary.FileUploadRequest;

import java.util.Map;

public interface MediaLibraryService {
    JsonNode getFiles(Map<String, Object> queryParams);
    JsonNode uploadFile(FileUploadRequest request);
    JsonNode deleteFileOrFolder(String id, String altId, String altType);
}
