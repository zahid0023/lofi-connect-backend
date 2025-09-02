package org.example.loficonnect.service;

import com.fasterxml.jackson.databind.JsonNode;
import org.example.loficonnect.dto.request.custom.fields.CustomFieldCreateRequest;
import org.example.loficonnect.dto.request.custom.fields.CustomFieldUpdateRequest;
import org.example.loficonnect.dto.request.custom.fields.UploadCustomFieldFileRequest;

import java.util.Map;

public interface CustomFieldsService {
    JsonNode getCustomFields(String locationId, Map<String, Object> queryParams);

    JsonNode createCustomField(String locationId, CustomFieldCreateRequest request);

    JsonNode getCustomField(String locationId, String id);

    JsonNode updateCustomField(String locationId, String id, CustomFieldUpdateRequest request);

    JsonNode deleteCustomField(String locationId, String id);

    JsonNode uploadCustomFieldFile(String locationId, UploadCustomFieldFileRequest request);

}
