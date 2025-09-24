package org.example.loficonnect.service;

import com.fasterxml.jackson.databind.JsonNode;
import org.example.loficonnect.dto.request.courses.ImportCourseRequest;

public interface CoursesService {
    JsonNode importCourses(ImportCourseRequest request);
}
