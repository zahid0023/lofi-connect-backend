package org.example.loficonnect.service;

import com.fasterxml.jackson.databind.JsonNode;
import org.example.loficonnect.dto.request.custom.fields.CustomFieldCreateRequest;
import org.example.loficonnect.dto.request.custom.fields.CustomFieldUpdateRequest;
import org.example.loficonnect.dto.request.custom.fields.UploadCustomFieldFileRequest;
import org.example.loficonnect.dto.response.customfields.CustomFieldResponse;
import org.example.loficonnect.dto.response.customfields.CustomFieldsResponse;

import java.util.Map;

public interface CustomFieldsService {
    JsonNode getCustomFields(Map<String, Object> queryParams);

    CustomFieldsResponse getCustomFieldsTypes(Map<String, Object> queryParams);

    JsonNode createCustomField(CustomFieldCreateRequest request);

    JsonNode getCustomField(String id);

    JsonNode updateCustomField(String id, CustomFieldUpdateRequest request);

    JsonNode deleteCustomField(String id);

    JsonNode uploadCustomFieldFile(UploadCustomFieldFileRequest request);
}
