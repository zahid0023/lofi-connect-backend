package org.example.loficonnect.service;

import com.fasterxml.jackson.databind.JsonNode;
import org.example.loficonnect.dto.request.custom.value.CustomValueCreateRequest;
import org.example.loficonnect.dto.request.custom.value.CustomValueUpdateRequest;

public interface CustomValueService {
    JsonNode getCustomValues();

    JsonNode createCustomValue(CustomValueCreateRequest request);

    JsonNode getCustomValue(String id);

    JsonNode updateCustomValue(String id, CustomValueUpdateRequest request);

    JsonNode deleteCustomValue(String id);
}
