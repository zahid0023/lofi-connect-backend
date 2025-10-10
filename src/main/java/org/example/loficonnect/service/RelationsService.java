package org.example.loficonnect.service;

import com.fasterxml.jackson.databind.JsonNode;
import org.example.loficonnect.dto.request.relations.RelationCreateRequest;

public interface RelationsService {
    JsonNode createRelation(RelationCreateRequest request);
    JsonNode getAllRelationsByRecordId(String recordId, String[] associationIds, int limit, String locationId, int skip);
    JsonNode deleteRelation(String relationId, String locationId);
}
