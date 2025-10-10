package org.example.loficonnect.service;

import com.fasterxml.jackson.databind.JsonNode;
import org.example.loficonnect.dto.request.collections.CollectionCreateRequest;
import org.example.loficonnect.dto.request.collections.CollectionUpdateRequest;

import java.util.Map;

public interface CollectionService {
    JsonNode getProductCollections(Map<String, Object> queryParams);
    JsonNode createCollection(CollectionCreateRequest request);
    JsonNode getCollectionById(String collectionId);
    JsonNode updateCollection(String collectionId, CollectionUpdateRequest request);
    JsonNode deleteCollection(String collectionId, Map<String, Object> queryParams);
}
