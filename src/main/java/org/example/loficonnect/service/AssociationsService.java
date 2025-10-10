package org.example.loficonnect.service;

import com.fasterxml.jackson.databind.JsonNode;
import org.example.loficonnect.dto.request.associations.AssociationUpdateRequest;
import org.example.loficonnect.dto.request.associations.AssociationCreateRequest;

public interface AssociationsService {
    JsonNode getAssociationKeyByName(String keyName, String locationId);
    JsonNode getAssociationByObjectKey(String objectKey, String locationId);
    JsonNode updateAssociation(String associationId, AssociationUpdateRequest request);
    JsonNode deleteAssociation(String associationId);
    JsonNode getAssociationById(String associationId);
    JsonNode createAssociation(AssociationCreateRequest request);
    JsonNode getAllAssociations(int limit, int skip, String locationId);
}
