package org.example.loficonnect.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import org.example.loficonnect.dto.request.courses.ImportCourseRequest;
import org.example.loficonnect.service.CoursesService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class CoursesControllerTest {

    private MockMvc mockMvc;

    @Mock
    private CoursesService coursesService;

    @InjectMocks
    private CoursesController coursesController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(coursesController).build();
    }

    @Test
    void testImportCourses() throws Exception {
        String requestBody = """
            {
                "location_id": "loc_123",
                "user_id": "user_456",
                "products": [
                    {
                        "title": "Java Basics",
                        "description": "Learn Java from scratch",
                        "image_url": "https://image.url",
                        "categories": [
                            {
                                "title": "Core",
                                "visibility": "public",
                                "thumbnail_url": "https://thumb.url",
                                "posts": [],
                                "sub_categories": []
                            }
                        ],
                        "instructor_details": {
                            "name": "John Doe",
                            "description": "Senior Java Instructor"
                        }
                    }
                ]
            }
        """;

        JsonNode mockResponse = JsonNodeFactory.instance.objectNode().put("status", "imported");

        when(coursesService.importCourses(any(ImportCourseRequest.class))).thenReturn(mockResponse);

        mockMvc.perform(post("/api/v1/courses/courses-exporter/public/import")
                        .header("Authorization", "Bearer dummy-token")
                        .header("Version", "1.0")
                        .contentType("application/json")
                        .content(requestBody))
                .andExpect(status().isCreated())
                .andExpect(content().json("{\"status\":\"imported\"}"));

        verify(coursesService, times(1)).importCourses(any(ImportCourseRequest.class));
    }
}
