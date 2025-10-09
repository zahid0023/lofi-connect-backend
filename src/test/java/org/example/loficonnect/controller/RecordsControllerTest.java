package org.example.loficonnect.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import org.example.loficonnect.dto.request.records.RecordCreateRequest;
import org.example.loficonnect.dto.request.records.RecordUpdateRequest;
import org.example.loficonnect.service.RecordsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class RecordsControllerTest {

    private MockMvc mockMvc;

    @Mock
    private RecordsService recordsService;

    @InjectMocks
    private RecordsController recordsController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(recordsController).build();
    }

    @Test
    void testGetRecordById() throws Exception {
        // Arrange
        String schemaKey = "contact";
        String id = "abc123";
        JsonNode mockResponse = JsonNodeFactory.instance.objectNode().put("recordName", "Test Record");

        when(recordsService.getRecordById(schemaKey, id)).thenReturn(mockResponse);

        // Act & Assert
        mockMvc.perform(get("/api/v1/objects/{schema-key}/records/{id}", schemaKey, id)
                        .header("Authorization", "Bearer test-token")
                        .header("Version", "1.0")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("{ \"recordName\": \"Test Record\" }"));

        verify(recordsService, times(1)).getRecordById(schemaKey, id);
    }

    @Test
    void testUpdateRecord() throws Exception {
        // Arrange
        String schemaKey = "contact";
        String id = "abc123";

        String requestJson = """
        {
            "key": "abc123",
            "description": "Updated record",
            "location_id": "loc_789",
            "updated_property": "email"
        }
    """;

        JsonNode mockResponse = JsonNodeFactory.instance.objectNode().put("status", "success");

        when(recordsService.updateRecord(eq(schemaKey), eq(id), any(RecordUpdateRequest.class)))
                .thenReturn(mockResponse);

        // Act & Assert
        mockMvc.perform(put("/api/v1/objects/{schema-key}/records/{id}", schemaKey, id)
                        .header("Authorization", "Bearer test-token")
                        .header("Version", "1.0")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"status\": \"success\"}"));

        verify(recordsService, times(1)).updateRecord(eq(schemaKey), eq(id), any(RecordUpdateRequest.class));
    }

    @Test
    void testDeleteRecord() throws Exception {
        // Arrange
        String schemaKey = "contact";
        String id = "abc123";

        JsonNode mockResponse = JsonNodeFactory.instance.objectNode().put("status", "deleted");

        when(recordsService.deleteRecord(schemaKey, id)).thenReturn(mockResponse);

        // Act & Assert
        mockMvc.perform(delete("/api/v1/objects/{schema-key}/records/{id}", schemaKey, id)
                        .header("Authorization", "Bearer test-token")
                        .header("Version", "1.0"))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"status\": \"deleted\"}"));

        verify(recordsService, times(1)).deleteRecord(schemaKey, id);
    }

    @Test
    void testCreateRecord() throws Exception {
        // Arrange
        String schemaKey = "contact";

        String requestJson = """
        {
            "key": "abc123",
            "description": "New record created",
            "location_id": "loc_789",
            "updated_property": "phone"
        }
    """;

        JsonNode mockResponse = JsonNodeFactory.instance.objectNode().put("status", "created");

        when(recordsService.createRecord(eq(schemaKey), any(RecordCreateRequest.class)))
                .thenReturn(mockResponse);

        // Act & Assert
        mockMvc.perform(post("/api/v1/objects/{schema-key}/records", schemaKey)
                        .header("Authorization", "Bearer test-token")
                        .header("Version", "1.0")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isCreated())
                .andExpect(content().json("{\"status\": \"created\"}"));

        verify(recordsService, times(1)).createRecord(eq(schemaKey), any(RecordCreateRequest.class));
    }

}
