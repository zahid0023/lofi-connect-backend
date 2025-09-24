package org.example.loficonnect.controller;

import org.example.loficonnect.config.AppKey;
import org.example.loficonnect.service.CoursesService;
import org.example.loficonnect.dto.request.courses.ImportCourseRequest;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class CoursesController {

    private final CoursesService coursesService;

    public CoursesController(CoursesService coursesService) {
        this.coursesService = coursesService;
    }

    @AppKey
    @PostMapping("/courses/courses-exporter/public/import")
    public ResponseEntity<?> importCourses(@RequestBody ImportCourseRequest request) {
        return ResponseEntity.status(201).body(coursesService.importCourses(request));
    }
}
