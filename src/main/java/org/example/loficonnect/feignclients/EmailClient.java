package org.example.loficonnect.feignclients;

import com.fasterxml.jackson.databind.JsonNode;
import org.example.loficonnect.config.FeignLoggingConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "emailClient", url = "https://services.leadconnectorhq.com", configuration = FeignLoggingConfig.class)
public interface EmailClient {
    @GetMapping(
            value = "/conversations/messages/email/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    JsonNode getEmailById(
            @RequestHeader("Authorization") String authorization,
            @RequestHeader("Version") String version,
            @PathVariable("id") String id
    );

    @DeleteMapping(
            value = "/conversations/messages/email/{emailMessageId}/schedule",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    JsonNode cancelScheduledEmail(
            @RequestHeader("Authorization") String authorization,
            @RequestHeader("Version") String version,
            @PathVariable("emailMessageId") String emailMessageId
    );


}
