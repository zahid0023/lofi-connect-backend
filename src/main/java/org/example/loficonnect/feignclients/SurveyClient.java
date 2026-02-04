package org.example.loficonnect.feignclients;

import com.fasterxml.jackson.databind.JsonNode;
import org.example.loficonnect.commons.config.FeignLoggingConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@FeignClient(name = "surveyClient", url = "https://services.leadconnectorhq.com", configuration = FeignLoggingConfig.class)
public interface SurveyClient {

    @GetMapping(
        value = "/surveys/submissions",
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    JsonNode getSurveySubmissions(
        @RequestHeader("Authorization") String authorization,
        @RequestHeader("Version") String version,
        @RequestParam Map<String, Object> queryParams
    );

    @GetMapping(
            value = "/surveys/",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    JsonNode getSurveys(
            @RequestHeader("Authorization") String authorization,
            @RequestHeader("Version") String version,
            @RequestParam Map<String, Object> queryParams
    );

}
