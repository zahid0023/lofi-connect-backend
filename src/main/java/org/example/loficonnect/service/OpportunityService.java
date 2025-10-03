package org.example.loficonnect.service;

import com.fasterxml.jackson.databind.JsonNode;
import org.example.loficonnect.dto.request.opportunity.OpportunityCreateRequest;
import org.example.loficonnect.dto.request.opportunity.OpportunityStatusUpdateRequest;
import org.example.loficonnect.dto.request.opportunity.OpportunityUpdateRequest;
import org.example.loficonnect.dto.request.opportunity.OpportunityUpsertRequest;

public interface OpportunityService {
    JsonNode getOpportunityById(String opportunityId);
    JsonNode deleteOpportunityById(String opportunityId);
    JsonNode updateOpportunityById(String id, OpportunityUpdateRequest request);
    JsonNode updateOpportunityStatus(String id, OpportunityStatusUpdateRequest request);
    JsonNode upsertOpportunity(OpportunityUpsertRequest request);
    JsonNode createOpportunity(OpportunityCreateRequest request);
}
