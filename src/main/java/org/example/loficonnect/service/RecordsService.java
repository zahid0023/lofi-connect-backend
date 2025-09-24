package org.example.loficonnect.service;

import com.fasterxml.jackson.databind.JsonNode;
import org.example.loficonnect.dto.request.records.RecordCreateRequest;
import org.example.loficonnect.dto.request.records.RecordUpdateRequest;

public interface RecordsService {
    JsonNode getRecordById(String schemaKey, String id);
    JsonNode updateRecord(String schemaKey, String id, RecordUpdateRequest request);
    JsonNode deleteRecord(String schemaKey, String id);
    JsonNode createRecord(String schemaKey, RecordCreateRequest request);
}
