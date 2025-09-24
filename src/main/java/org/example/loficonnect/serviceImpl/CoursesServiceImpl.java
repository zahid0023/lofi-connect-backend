package org.example.loficonnect.serviceImpl;

import com.fasterxml.jackson.databind.JsonNode;
import org.example.loficonnect.feignclients.CoursesClient;
import org.example.loficonnect.service.AuthorizationService;
import org.example.loficonnect.service.CoursesService;
import org.example.loficonnect.dto.request.courses.ImportCourseRequest;
import org.example.loficonnect.dto.mapper.courses.GoHighLevelImportCourseRequest;
import org.example.loficonnect.util.AppKeyContext;
import org.example.loficonnect.util.VersionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CoursesServiceImpl implements CoursesService {

    private final CoursesClient coursesClient;
    private final AuthorizationService authorizationService;

    @Autowired
    public CoursesServiceImpl(CoursesClient coursesClient, AuthorizationService authorizationService) {
        this.coursesClient = coursesClient;
        this.authorizationService = authorizationService;
    }

    @Override
    public JsonNode importCourses(ImportCourseRequest request) {
        String accessKey = authorizationService.getAccessToken(AppKeyContext.getAppKey());
        String version = VersionContext.getVersion();
        GoHighLevelImportCourseRequest ghlRequest = GoHighLevelImportCourseRequest.fromRequest(request);
        return coursesClient.importCourses(accessKey, version, ghlRequest);
    }
}
