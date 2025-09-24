package org.example.loficonnect.service;

import com.fasterxml.jackson.databind.JsonNode;
import org.example.loficonnect.dto.request.objectschema.CustomObjectCreateRequest;
import org.example.loficonnect.dto.request.objectschema.ObjectSchemaUpdateRequest;

public interface ObjectSchemaService {
    JsonNode getObjectSchema(String key, String locationId, String fetchProperties);
    JsonNode updateObjectSchema(String key, ObjectSchemaUpdateRequest request);
    JsonNode getAllObjects(String locationId);
    JsonNode createCustomObject(CustomObjectCreateRequest request);
}
