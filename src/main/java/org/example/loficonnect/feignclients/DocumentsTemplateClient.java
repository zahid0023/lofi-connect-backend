package org.example.loficonnect.feignclients;

import com.fasterxml.jackson.databind.JsonNode;
import org.example.loficonnect.commons.config.FeignLoggingConfig;
import org.example.loficonnect.dto.mapper.documentstemplate.GoHighLevelTemplateSendRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@FeignClient(name = "documentsTemplateClient", url = "https://services.leadconnectorhq.com", configuration = FeignLoggingConfig.class)
public interface DocumentsTemplateClient {

    @GetMapping(
            value = "/proposals/templates",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    JsonNode getTemplates(
            @RequestHeader("Authorization") String authorization,
            @RequestHeader("Version") String version,
            @RequestParam Map<String, Object> queryParams
    );

    @PostMapping(
            value = "/proposals/templates/send",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    JsonNode sendTemplate(
            @RequestHeader("Authorization") String authorization,
            @RequestHeader("Version") String version,
            @RequestBody GoHighLevelTemplateSendRequest request
    );

}
