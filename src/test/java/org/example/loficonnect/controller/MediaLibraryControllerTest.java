package org.example.loficonnect.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import org.example.loficonnect.service.MediaLibraryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.mock.web.MockPart;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class MediaLibraryControllerTest {

    private MockMvc mockMvc;

    @Mock
    private MediaLibraryService mediaLibraryService;

    @InjectMocks
    private MediaLibraryController mediaLibraryController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(mediaLibraryController).build();
    }

    @Test
    void testGetFiles() throws Exception {
        String limit = "10";
        String offset = "0";
        String query = "sample";
        String type = "image";
        String altId = "123";
        String altType = "contact";
        String sortBy = "createdAt";
        String sortOrder = "desc";
        String parentId = "999";

        Map<String, Object> expectedParams = new HashMap<>();
        expectedParams.put("limit", limit);
        expectedParams.put("offset", offset);
        expectedParams.put("query", query);
        expectedParams.put("type", type);
        expectedParams.put("alt-id", altId);
        expectedParams.put("alt-type", altType);
        expectedParams.put("sort-by", sortBy);
        expectedParams.put("sort-order", sortOrder);
        expectedParams.put("parent-id", parentId);

        JsonNode mockResponse = JsonNodeFactory.instance.objectNode()
                .put("count", 5);

        when(mediaLibraryService.getFiles(expectedParams)).thenReturn(mockResponse);

        mockMvc.perform(get("/api/v1/files")
                        .param("limit", limit)
                        .param("offset", offset)
                        .param("query", query)
                        .param("type", type)
                        .param("alt-id", altId)
                        .param("alt-type", altType)
                        .param("sort-by", sortBy)
                        .param("sort-order", sortOrder)
                        .param("parent-id", parentId)
                        .header("Authorization", "Bearer token")
                        .header("Version", "1.0"))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"count\":5}"));

        verify(mediaLibraryService, times(1)).getFiles(expectedParams);
    }

    @Test
    void testUploadFile() throws Exception {
        MockMultipartFile mockFile =
                new MockMultipartFile("file", "test-image.jpg", "image/jpeg", "dummy image content".getBytes());

        MockPart hostedPart = new MockPart("hosted", "true".getBytes());
        hostedPart.getHeaders().setContentType(MediaType.APPLICATION_JSON);

        MockPart fileUrlPart = new MockPart("fileUrl", "\"https://example.com/image.jpg\"".getBytes());
        fileUrlPart.getHeaders().setContentType(MediaType.APPLICATION_JSON);

        MockPart namePart = new MockPart("name", "\"Test Image\"".getBytes());
        namePart.getHeaders().setContentType(MediaType.APPLICATION_JSON);

        MockPart parentIdPart = new MockPart("parent-id", "\"parent123\"".getBytes());
        parentIdPart.getHeaders().setContentType(MediaType.APPLICATION_JSON);

        JsonNode mockResponse = JsonNodeFactory.instance.objectNode()
                .put("status", "uploaded")
                .put("name", "Test Image");

        when(mediaLibraryService.uploadFile(any())).thenReturn(mockResponse);

        mockMvc.perform(multipart("/api/v1/upload-file")
                        .file(mockFile)
                        .part(hostedPart)
                        .part(fileUrlPart)
                        .part(namePart)
                        .part(parentIdPart)
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .header("Authorization", "Bearer token")
                        .header("Version", "1.0"))
                .andExpect(status().isCreated())
                .andExpect(content().json("{\"status\":\"uploaded\",\"name\":\"Test Image\"}"));

        verify(mediaLibraryService, times(1)).uploadFile(any());
    }

    @Test
    void testDeleteFileOrFolder() throws Exception {
        String id = "file123";
        String altId = "alt456";
        String altType = "contact";

        JsonNode mockResponse = JsonNodeFactory.instance.objectNode()
                .put("status", "deleted")
                .put("id", id);

        when(mediaLibraryService.deleteFileOrFolder(id, altId, altType)).thenReturn(mockResponse);

        mockMvc.perform(delete("/api/v1/{id}", id)
                        .param("alt-id", altId)
                        .param("alt-type", altType)
                        .header("Authorization", "Bearer token")
                        .header("Version", "1.0"))
                .andExpect(status().isNoContent())
                .andExpect(content().json("{\"status\":\"deleted\",\"id\":\"file123\"}"));

        verify(mediaLibraryService, times(1)).deleteFileOrFolder(id, altId, altType);
    }

}
