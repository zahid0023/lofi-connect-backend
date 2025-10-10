package org.example.loficonnect.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import org.example.loficonnect.dto.request.objectschema.CustomObjectCreateRequest;
import org.example.loficonnect.dto.request.objectschema.ObjectSchemaUpdateRequest;
import org.example.loficonnect.service.ObjectSchemaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class ObjectSchemaControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ObjectSchemaService objectSchemaService;

    @InjectMocks
    private ObjectSchemaController objectSchemaController;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(objectSchemaController).build();
    }

    @Test
    void testGetObjectSchema() throws Exception {
        // Arrange
        String key = "contacts";
        String locationId = "loc123";
        String fetchProperties = "true";

        JsonNode mockResponse = JsonNodeFactory.instance.objectNode()
            .put("objectType", "contact")
            .put("propertyCount", 5);

        when(objectSchemaService.getObjectSchema(key, locationId, fetchProperties)).thenReturn(mockResponse);

        // Act & Assert
        mockMvc.perform(get("/api/v1/objects/{key}", key)
                        .param("locationId", locationId)
                        .param("fetchProperties", fetchProperties)
                        .header("Authorization", "Bearer token")
                        .header("Version", "2021-07-28"))
                .andExpect(status().isOk())
                .andExpect(content().json("{ \"objectType\": \"contact\", \"propertyCount\": 5 }"));

        verify(objectSchemaService, times(1)).getObjectSchema(key, locationId, fetchProperties);
    }

    @Test
    void testUpdateObjectSchema() throws Exception {
        // Arrange
        String key = "contacts";

        ObjectSchemaUpdateRequest.Labels labels = new ObjectSchemaUpdateRequest.Labels();
        labels.setSingular("Contact");
        labels.setPlural("Contacts");

        ObjectSchemaUpdateRequest request = new ObjectSchemaUpdateRequest();
        request.setLabels(labels);
        request.setDescription("Updated schema for contacts");
        request.setLocationId("loc123");
        request.setSearchableProperties(List.of("email", "phone"));

        JsonNode mockResponse = JsonNodeFactory.instance.objectNode()
                .put("status", "updated");

        when(objectSchemaService.updateObjectSchema(eq(key), any(ObjectSchemaUpdateRequest.class)))
                .thenReturn(mockResponse);

        // Act & Assert
        mockMvc.perform(put("/api/v1/objects/{key}", key)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"status\":\"updated\"}"));

        verify(objectSchemaService, times(1)).updateObjectSchema(eq(key), any(ObjectSchemaUpdateRequest.class));
    }

    @Test
    void testGetAllObjects() throws Exception {
        // Arrange
        String locationId = "loc123";

        JsonNode mockResponse = JsonNodeFactory.instance.objectNode()
                .put("total", 3);

        when(objectSchemaService.getAllObjects(locationId)).thenReturn(mockResponse);

        // Act & Assert
        mockMvc.perform(get("/api/v1/objects")
                        .param("location-id", locationId)
                        .header("Authorization", "Bearer token")
                        .header("Version", "2021-07-28"))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"total\":3}"));

        verify(objectSchemaService, times(1)).getAllObjects(locationId);
    }

    @Test
    void testCreateCustomObject() throws Exception {
        // Arrange
        CustomObjectCreateRequest.Labels labels = new CustomObjectCreateRequest.Labels();
        labels.setSingular("Deal");
        labels.setPlural("Deals");

        CustomObjectCreateRequest.PrimaryDisplayPropertyDetails primaryDisplay = new CustomObjectCreateRequest.PrimaryDisplayPropertyDetails();
        primaryDisplay.setKey("deal_name");
        primaryDisplay.setName("Deal Name");
        primaryDisplay.setDataType("string");

        CustomObjectCreateRequest request = new CustomObjectCreateRequest();
        request.setLabels(labels);
        request.setKey("deals");
        request.setDescription("Custom object for deals");
        request.setLocationId("loc123");
        request.setPrimaryDisplayPropertyDetails(primaryDisplay);

        JsonNode mockResponse = JsonNodeFactory.instance.objectNode()
                .put("status", "created");

        when(objectSchemaService.createCustomObject(any(CustomObjectCreateRequest.class)))
                .thenReturn(mockResponse);

        // Act & Assert
        mockMvc.perform(post("/api/v1/objects")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(content().json("{\"status\":\"created\"}"));

        verify(objectSchemaService, times(1)).createCustomObject(any(CustomObjectCreateRequest.class));
    }
}
