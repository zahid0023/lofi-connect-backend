package org.example.loficonnect.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import org.example.loficonnect.dto.request.associations.AssociationCreateRequest;
import org.example.loficonnect.dto.request.associations.AssociationUpdateRequest;
import org.example.loficonnect.service.AssociationsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class AssociationsControllerTest {

    private MockMvc mockMvc;

    @Mock
    private AssociationsService associationsService;

    @InjectMocks
    private AssociationsController associationsController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(associationsController).build();
    }

    @Test
    void testGetAssociationKeyByName() throws Exception {
        String keyName = "example_key";
        String locationId = "loc_456";
        JsonNode mockResponse = JsonNodeFactory.instance.objectNode().put("key", "example_key");

        when(associationsService.getAssociationKeyByName(keyName, locationId)).thenReturn(mockResponse);

        mockMvc.perform(get("/api/v1/associations/key/{key_name}", keyName)
                        .param("location-id", locationId)
                        .header("Authorization", "Bearer dummy-token")
                        .header("Version", "1.0"))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"key\":\"example_key\"}"));

        verify(associationsService, times(1)).getAssociationKeyByName(keyName, locationId);
    }

    @Test
    void testGetAssociationByObjectKey() throws Exception {
        String objectKey = "contact_789";
        String locationId = "loc_123";
        JsonNode mockResponse = JsonNodeFactory.instance.objectNode().put("objectKey", "contact_789");

        when(associationsService.getAssociationByObjectKey(objectKey, locationId)).thenReturn(mockResponse);

        mockMvc.perform(get("/api/v1/associations/object-key/{object-key}", objectKey)
                        .param("location-id", locationId)
                        .header("Authorization", "Bearer dummy-token")
                        .header("Version", "1.0"))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"objectKey\":\"contact_789\"}"));

        verify(associationsService, times(1)).getAssociationByObjectKey(objectKey, locationId);
    }

    @Test
    void testUpdateAssociation() throws Exception {
        String associationId = "assoc_001";
        String requestBody = """
        {
            "first_object_label": "Contact",
            "second_object_label": "Deal"
        }
    """;

        JsonNode mockResponse = JsonNodeFactory.instance.objectNode().put("status", "updated");

        when(associationsService.updateAssociation(eq(associationId), any(AssociationUpdateRequest.class)))
                .thenReturn(mockResponse);

        mockMvc.perform(put("/api/v1/associations/{association-id}", associationId)
                        .header("Authorization", "Bearer dummy-token")
                        .header("Version", "1.0")
                        .contentType("application/json")
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"status\":\"updated\"}"));

        verify(associationsService, times(1)).updateAssociation(eq(associationId), any(AssociationUpdateRequest.class));
    }

    @Test
    void testDeleteAssociation() throws Exception {
        String associationId = "assoc_002";
        JsonNode mockResponse = JsonNodeFactory.instance.objectNode().put("status", "deleted");

        when(associationsService.deleteAssociation(associationId)).thenReturn(mockResponse);

        mockMvc.perform(delete("/api/v1/associations/{association-id}", associationId)
                        .header("Authorization", "Bearer dummy-token")
                        .header("Version", "1.0"))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"status\":\"deleted\"}"));

        verify(associationsService, times(1)).deleteAssociation(associationId);
    }

    @Test
    void testGetAssociationById() throws Exception {
        String associationId = "assoc_003";
        JsonNode mockResponse = JsonNodeFactory.instance.objectNode().put("id", associationId);

        when(associationsService.getAssociationById(associationId)).thenReturn(mockResponse);

        mockMvc.perform(get("/api/v1/associations/{association-id}", associationId)
                        .header("Authorization", "Bearer dummy-token")
                        .header("Version", "1.0"))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"id\":\"assoc_003\"}"));

        verify(associationsService, times(1)).getAssociationById(associationId);
    }

    @Test
    void testCreateAssociation() throws Exception {
        String requestBody = """
        {
            "location_id": "loc_123",
            "key": "contact_to_deal",
            "first_object_label": "Contact",
            "first_object_key": "contact",
            "second_object_label": "Deal",
            "second_object_key": "deal"
        }
    """;

        JsonNode mockResponse = JsonNodeFactory.instance.objectNode()
                .put("key", "contact_to_deal")
                .put("status", "created");

        when(associationsService.createAssociation(any(AssociationCreateRequest.class)))
                .thenReturn(mockResponse);

        mockMvc.perform(post("/api/v1/associations")
                        .header("Authorization", "Bearer dummy-token")
                        .header("Version", "1.0")
                        .contentType("application/json")
                        .content(requestBody))
                .andExpect(status().isCreated())
                .andExpect(content().json("{\"key\":\"contact_to_deal\",\"status\":\"created\"}"));

        verify(associationsService, times(1)).createAssociation(any(AssociationCreateRequest.class));
    }

    @Test
    void testGetAllAssociations() throws Exception {
        int limit = 10;
        int skip = 0;
        String locationId = "loc_123";

        JsonNode mockResponse = JsonNodeFactory.instance.objectNode()
                .put("total", 2);

        when(associationsService.getAllAssociations(limit, skip, locationId))
                .thenReturn(mockResponse);

        mockMvc.perform(get("/api/v1/associations")
                        .param("limit", String.valueOf(limit))
                        .param("skip", String.valueOf(skip))
                        .param("locationId", locationId)
                        .header("Authorization", "Bearer dummy-token")
                        .header("Version", "1.0"))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"total\":2}"));

        verify(associationsService, times(1)).getAllAssociations(limit, skip, locationId);
    }

}
