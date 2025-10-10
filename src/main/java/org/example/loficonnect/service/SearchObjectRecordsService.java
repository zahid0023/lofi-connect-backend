package org.example.loficonnect.service;

import com.fasterxml.jackson.databind.JsonNode;
import org.example.loficonnect.dto.request.searchobjectrecords.SearchObjectRecordsRequest;

public interface SearchObjectRecordsService {
    JsonNode searchObjectRecords(String schemaKey, SearchObjectRecordsRequest request);
}
