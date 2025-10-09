package org.example.loficonnect.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import org.example.loficonnect.dto.request.searchobjectrecords.SearchObjectRecordsRequest;
import org.example.loficonnect.service.SearchObjectRecordsService;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class SearchObjectRecordsControllerTest {

    private MockMvc mockMvc;

    @Mock
    private SearchObjectRecordsService searchObjectRecordsService;

    @InjectMocks
    private SearchObjectRecordsController searchObjectRecordsController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(searchObjectRecordsController).build();
    }

    @Test
    void testSearchObjectRecords() throws Exception {
        // Arrange
        String schemaKey = "contact";

        String requestJson = """
        {
            "location_id": "loc_123",
            "page": 1,
            "page_limit": 10,
            "query": "email:test@example.com",
            "search_after": ["cursor123"]
        }
        """;

        JsonNode mockResponse = JsonNodeFactory.instance.objectNode().put("result", "success");

        when(searchObjectRecordsService.searchObjectRecords(eq(schemaKey), any(SearchObjectRecordsRequest.class)))
                .thenReturn(mockResponse);

        // Act & Assert
        mockMvc.perform(post("/api/v1/objects/{schema-key}/records/search", schemaKey)
                        .header("Authorization", "Bearer test-token")
                        .header("Version", "1.0")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"result\":\"success\"}"));

        verify(searchObjectRecordsService, times(1)).searchObjectRecords(eq(schemaKey), any(SearchObjectRecordsRequest.class));
    }
}
