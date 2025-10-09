package org.example.loficonnect.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import org.example.loficonnect.dto.request.relations.RelationCreateRequest;
import org.example.loficonnect.service.RelationsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class RelationsControllerTest {

    private MockMvc mockMvc;

    @Mock
    private RelationsService relationsService;

    @InjectMocks
    private RelationsController relationsController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(relationsController).build();
    }

    @Test
    void testCreateRelation() throws Exception {
        String requestBody = """
            {
                "location_id": "loc_123",
                "association_id": "assoc_789",
                "first_record_id": "rec_001",
                "second_record_id": "rec_002"
            }
        """;

        JsonNode mockResponse = JsonNodeFactory.instance.objectNode().put("status", "created");

        when(relationsService.createRelation(any(RelationCreateRequest.class))).thenReturn(mockResponse);

        mockMvc.perform(post("/api/v1/associations/relations")
                        .header("Authorization", "Bearer dummy-token")
                        .header("Version", "1.0")
                        .contentType("application/json")
                        .content(requestBody))
                .andExpect(status().isCreated())
                .andExpect(content().json("{\"status\":\"created\"}"));

        verify(relationsService, times(1)).createRelation(any(RelationCreateRequest.class));
    }

    @Test
    void testGetAllRelationsByRecordId() throws Exception {
        String recordId = "rec_001";
        String[] associationIds = {"assoc_1", "assoc_2"};
        JsonNode mockResponse = JsonNodeFactory.instance.objectNode().put("total", 2);

        when(relationsService.getAllRelationsByRecordId(eq(recordId), any(String[].class), eq(10), eq("loc_123"), eq(0)))
                .thenReturn(mockResponse);

        mockMvc.perform(get("/api/v1/associations/relations/{record-id}", recordId)
                        .param("association-ids", "assoc_1", "assoc_2")
                        .param("limit", "10")
                        .param("location-id", "loc_123")
                        .param("skip", "0")
                        .header("Authorization", "Bearer dummy-token")
                        .header("Version", "1.0"))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"total\":2}"));

        verify(relationsService, times(1))
                .getAllRelationsByRecordId(eq(recordId), any(String[].class), eq(10), eq("loc_123"), eq(0));
    }

    @Test
    void testDeleteRelation() throws Exception {
        String relationId = "rel_456";
        String locationId = "loc_123";
        JsonNode mockResponse = JsonNodeFactory.instance.objectNode().put("status", "deleted");

        when(relationsService.deleteRelation(relationId, locationId)).thenReturn(mockResponse);

        mockMvc.perform(delete("/api/v1/associations/relations/{relation-id}", relationId)
                        .param("location-id", locationId)
                        .header("Authorization", "Bearer dummy-token")
                        .header("Version", "1.0"))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"status\":\"deleted\"}"));

        verify(relationsService, times(1)).deleteRelation(relationId, locationId);
    }
}
