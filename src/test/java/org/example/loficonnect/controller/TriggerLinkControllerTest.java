package org.example.loficonnect.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import org.example.loficonnect.dto.request.triggerlinks.LinkUpdateRequest;
import org.example.loficonnect.service.TriggerLinkService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class TriggerLinkControllerTest {

    private MockMvc mockMvc;

    @Mock
    private TriggerLinkService triggerLinkService;

    @InjectMocks
    private TriggerLinkController triggerLinkController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(triggerLinkController).build();
    }

    @Test
    void testUpdateLink() throws Exception {
        String linkId = "abc123";
        JsonNode mockResponse = JsonNodeFactory.instance.objectNode()
                .put("name", "Updated Link")
                .put("redirectTo", "https://example.com");

        when(triggerLinkService.updateLink(eq(linkId), any(LinkUpdateRequest.class)))
                .thenReturn(mockResponse);

        mockMvc.perform(put("/api/v1/{link-id}", linkId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer token")
                        .header("Version", "1.0")
                        .content("{ \"name\": \"Updated Link\", \"redirect_to\": \"https://example.com\" }"))
                .andExpect(status().isOk())
                .andExpect(content().json("{ \"name\": \"Updated Link\", \"redirectTo\": \"https://example.com\" }"));

        verify(triggerLinkService, times(1)).updateLink(eq(linkId), any(LinkUpdateRequest.class));
    }

    @Test
    void testDeleteLink() throws Exception {
        String linkId = "abc123";
        JsonNode mockResponse = JsonNodeFactory.instance.objectNode()
                .put("status", "deleted");

        when(triggerLinkService.deleteLink(linkId)).thenReturn(mockResponse);

        mockMvc.perform(delete("/api/v1/{link-id}", linkId)
                        .header("Authorization", "Bearer token")
                        .header("Version", "1.0"))
                .andExpect(status().isNoContent())
                .andExpect(content().json("{\"status\":\"deleted\"}"));

        verify(triggerLinkService, times(1)).deleteLink(linkId);
    }

    @Test
    void testGetLinks() throws Exception {
        String locationId = "loc123";
        JsonNode mockResponse = JsonNodeFactory.instance.objectNode()
                .put("count", 2);

        Map<String, Object> expectedQueryParams = new HashMap<>();
        expectedQueryParams.put("locationId", locationId);

        when(triggerLinkService.getLinks(expectedQueryParams)).thenReturn(mockResponse);

        mockMvc.perform(get("/api/v1")
                        .param("location-id", locationId)
                        .header("Authorization", "Bearer token")
                        .header("Version", "1.0"))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"count\":2}"));

        verify(triggerLinkService, times(1)).getLinks(expectedQueryParams);
    }

    @Test
    void testCreateLink() throws Exception {
        JsonNode mockResponse = JsonNodeFactory.instance.objectNode()
                .put("name", "New Link")
                .put("redirectTo", "https://example.com");

        when(triggerLinkService.createLink(any())).thenReturn(mockResponse);

        mockMvc.perform(post("/api/v1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer token")
                        .header("Version", "1.0")
                        .content("{ \"location_id\": \"loc123\", \"name\": \"New Link\", \"redirect_to\": \"https://example.com\" }"))
                .andExpect(status().isCreated())
                .andExpect(content().json("{ \"name\": \"New Link\", \"redirectTo\": \"https://example.com\" }"));

        verify(triggerLinkService, times(1)).createLink(any());
    }

}
