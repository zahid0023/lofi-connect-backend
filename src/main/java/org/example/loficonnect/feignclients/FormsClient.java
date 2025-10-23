package org.example.loficonnect.feignclients;

import com.fasterxml.jackson.databind.JsonNode;
import org.example.loficonnect.config.FeignLoggingConfig;
import org.example.loficonnect.dto.mapper.forms.GoHighLevelFileUploadRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@FeignClient(name = "formsClient", url = "https://services.leadconnectorhq.com", configuration = FeignLoggingConfig.class)
public interface FormsClient {

    @GetMapping(value = "/forms/submissions", produces = MediaType.APPLICATION_JSON_VALUE)
    JsonNode getFormSubmissions(
        @RequestHeader("Authorization") String authorization,
        @RequestHeader("Version") String version,
        @RequestParam Map<String, Object> queryParams
    );

    @PostMapping(value = "/forms/upload-custom-files", produces = MediaType.APPLICATION_JSON_VALUE)
    JsonNode uploadCustomFiles(
            @RequestHeader("Authorization") String authorization,
            @RequestHeader("Version") String version,
            @RequestParam Map<String, Object> queryParams,
            @RequestBody GoHighLevelFileUploadRequest request
    );

    @GetMapping(value = "/forms/", produces = MediaType.APPLICATION_JSON_VALUE)
    JsonNode getForms(
            @RequestHeader("Authorization") String authorization,
            @RequestHeader("Version") String version,
            @RequestParam Map<String, Object> queryParams
    );
}
