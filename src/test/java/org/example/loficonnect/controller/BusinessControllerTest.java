package org.example.loficonnect.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import lombok.extern.slf4j.Slf4j;
import org.example.loficonnect.service.BusinessService;
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

@Slf4j
@ExtendWith(MockitoExtension.class)
class BusinessControllerTest {

    private MockMvc mockMvc;

    @Mock
    private BusinessService businessService;

    @InjectMocks
    private BusinessController businessController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(businessController).build();
    }

    @Test
    void testGetBusiness() throws Exception {
        // Arrange
        String businessId = "123";
        JsonNode mockResponse = JsonNodeFactory.instance.objectNode().put("name", "Test Business");

        when(businessService.getBusiness(businessId)).thenReturn(mockResponse);

        // Act & Assert
        mockMvc.perform(get("/api/v1/business/{business-id}", businessId)
                        .header("Authorization", "Bearer token")
                        .header("Version", "1.0"))
                .andExpect(status().isOk())
                .andExpect(content().json("{ \"name\": \"Test Business\" }"));

        verify(businessService, times(1)).getBusiness(businessId);

        log.info("ok");
    }

    @Test
    void testCreateBusiness() throws Exception {
        // Arrange
        JsonNode mockResponse = JsonNodeFactory.instance.objectNode().put("name", "Test Business");

        when(businessService.createBusiness(any())).thenReturn(mockResponse);

        // Act & Assert
        mockMvc.perform(post("/api/v1/businesses")
                        .header("Authorization", "Bearer token")
                        .header("Version", "1.0")
                        .contentType("application/json")
                        .content("{ \"name\": \"Test Business\" }"))
                .andExpect(status().isCreated())
                .andExpect(content().json("{ \"name\": \"Test Business\" }"));

        verify(businessService, times(1)).createBusiness(any());
    }

    @Test
    void testUpdateBusiness() throws Exception {
        // Arrange
        String businessId = "123";
        JsonNode mockResponse = JsonNodeFactory.instance.objectNode().put("name", "Updated Business");

        when(businessService.updateBusiness(eq(businessId), any())).thenReturn(mockResponse);

        // Act & Assert
        mockMvc.perform(put("/api/v1/businesses/{business-id}", businessId)
                        .header("Authorization", "Bearer token")
                        .header("Version", "1.0")
                        .contentType("application/json")
                        .content("{ \"name\": \"Updated Business\" }"))
                .andExpect(status().isOk())
                .andExpect(content().json("{ \"name\": \"Updated Business\" }"));

        verify(businessService, times(1)).updateBusiness(eq(businessId), any());
    }

    @Test
    void testDeleteBusiness() throws Exception {
        // Arrange
        String businessId = "123";
        JsonNode mockResponse = JsonNodeFactory.instance.objectNode().put("status", "deleted");

        when(businessService.deleteBusiness(businessId)).thenReturn(mockResponse);

        // Act & Assert
        mockMvc.perform(delete("/api/v1/businesses/{business-id}", businessId)
                        .header("Authorization", "Bearer token")
                        .header("Version", "1.0"))
                .andExpect(status().isOk())
                .andExpect(content().json("{ \"status\": \"deleted\" }"));

        verify(businessService, times(1)).deleteBusiness(businessId);
    }
}