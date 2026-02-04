package org.example.loficonnect.feignclients;

import com.fasterxml.jackson.databind.JsonNode;
import org.example.loficonnect.commons.config.FeignLoggingConfig;
import org.example.loficonnect.dto.mapper.emailverification.GoHighLevelEmailVerificationRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@FeignClient(name = "emailVerificationClient", url = "https://services.leadconnectorhq.com", configuration = FeignLoggingConfig.class)
public interface EmailVerificationClient {

    @PostMapping(
        value = "/email/verify",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    JsonNode verifyEmail(
        @RequestHeader("Authorization") String authorization,
        @RequestHeader("Version") String version,
        @RequestParam Map<String, Object> queryParams,
        @RequestBody GoHighLevelEmailVerificationRequest request
    );
}
