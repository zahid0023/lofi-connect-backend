package org.example.loficonnect.feignclients;

import com.fasterxml.jackson.databind.JsonNode;
import org.example.loficonnect.config.FeignLoggingConfig;
import org.example.loficonnect.dto.mapper.courses.GoHighLevelImportCourseRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "CoursesClient", url = "https://services.leadconnectorhq.com", configuration = FeignLoggingConfig.class)
public interface CoursesClient {

    @PostMapping(
        value = "/courses/courses-exporter/public/import",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    JsonNode importCourses(
        @RequestHeader("Authorization") String authorization,
        @RequestHeader("Version") String version,
        @RequestBody GoHighLevelImportCourseRequest request
    );
}
