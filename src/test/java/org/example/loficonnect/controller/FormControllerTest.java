package org.example.loficonnect.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import org.example.loficonnect.dto.request.forms.FileUploadRequest;
import org.example.loficonnect.service.FormService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Map;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class FormControllerTest {

    private MockMvc mockMvc;

    @Mock
    private FormService formService;

    @InjectMocks
    private FormController formController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(formController).build();
    }

    @Test
    void testGetFormSubmissions_withAllParams() throws Exception {
        JsonNode mockResponse = JsonNodeFactory.instance.objectNode().put("total", 2);

        when(formService.getFormSubmissions(any(Map.class))).thenReturn(mockResponse);

        mockMvc.perform(get("/api/v1/forms/submissions")
                        .param("end-at", "2023-12-31")
                        .param("form-id", "form123")
                        .param("limit", "10")
                        .param("page", "2")
                        .param("q", "lead")
                        .param("start-at", "2023-01-01")
                        .param("location-id", "loc_789")
                        .header("Authorization", "Bearer token")
                        .header("Version", "1.0"))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"total\":2}"));

        verify(formService, times(1)).getFormSubmissions(any(Map.class));
    }

    @Test
    void testGetFormSubmissions_withOnlyRequiredParam() throws Exception {
        JsonNode mockResponse = JsonNodeFactory.instance.objectNode().put("total", 0);

        when(formService.getFormSubmissions(any(Map.class))).thenReturn(mockResponse);

        mockMvc.perform(get("/api/v1/forms/submissions")
                        .param("location-id", "loc_789")
                        .header("Authorization", "Bearer token")
                        .header("Version", "1.0"))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"total\":0}"));

        verify(formService, times(1)).getFormSubmissions(any(Map.class));
    }

    @Test
    void testUploadCustomFiles() throws Exception {
        String requestBody = """
        {
            "contact_id": "contact_123",
            "location_id": "loc_456",
            "files": {
                "file1": "https://example.com/file1.pdf",
                "file2": "https://example.com/file2.jpg"
            }
        }
    """;

        JsonNode mockResponse = JsonNodeFactory.instance.objectNode().put("status", "uploaded");

        when(formService.uploadCustomFiles(any(FileUploadRequest.class)))
                .thenReturn(mockResponse);

        mockMvc.perform(post("/api/v1/forms/upload-custom-files")
                        .header("Authorization", "Bearer dummy-token")
                        .header("Version", "1.0")
                        .contentType("application/json")
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"status\":\"uploaded\"}"));

        verify(formService, times(1)).uploadCustomFiles(any(FileUploadRequest.class));
    }

    @Test
    void testGetForms_withAllParams() throws Exception {
        JsonNode mockResponse = JsonNodeFactory.instance.objectNode().put("total", 5);

        when(formService.getForms(any(Map.class))).thenReturn(mockResponse);

        mockMvc.perform(get("/api/v1/forms")
                        .param("limit", "15")
                        .param("skip", "5")
                        .param("type", "survey")
                        .param("location-id", "loc_123")
                        .header("Authorization", "Bearer dummy-token")
                        .header("Version", "1.0"))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"total\":5}"));

        verify(formService, times(1)).getForms(any(Map.class));
    }

    @Test
    void testGetForms_withOnlyRequiredParam() throws Exception {
        JsonNode mockResponse = JsonNodeFactory.instance.objectNode().put("total", 2);

        when(formService.getForms(any(Map.class))).thenReturn(mockResponse);

        mockMvc.perform(get("/api/v1/forms")
                        .param("location-id", "loc_456")
                        .header("Authorization", "Bearer dummy-token")
                        .header("Version", "1.0"))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"total\":2}"));

        verify(formService, times(1)).getForms(any(Map.class));
    }

}
