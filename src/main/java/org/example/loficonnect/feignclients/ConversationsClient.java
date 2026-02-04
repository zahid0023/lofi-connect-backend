package org.example.loficonnect.feignclients;

import com.fasterxml.jackson.databind.JsonNode;
import org.example.loficonnect.commons.config.FeignLoggingConfig;
import org.example.loficonnect.dto.mapper.conversations.GoHighLevelConversationCreateRequest;
import org.example.loficonnect.dto.mapper.conversations.GoHighLevelConversationUpdateRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "conversationsClient", url = "https://services.leadconnectorhq.com", configuration = FeignLoggingConfig.class)
public interface ConversationsClient {
    @PostMapping(
            value = "/conversations",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    JsonNode createConversation(
            @RequestHeader("Authorization") String authorization,
            @RequestHeader("Version") String version,
            @RequestBody GoHighLevelConversationCreateRequest request
    );

    @GetMapping(
            value = "/conversations/{conversationId}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    JsonNode getConversation(
            @RequestHeader("Authorization") String authorization,
            @RequestHeader("Version") String version,
            @PathVariable("conversationId") String conversationId
    );

    @PutMapping(
            value = "/conversations/{conversationId}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    JsonNode updateConversation(
            @RequestHeader("Authorization") String authorization,
            @RequestHeader("Version") String version,
            @PathVariable("conversationId") String conversationId,
            @RequestBody GoHighLevelConversationUpdateRequest request
    );

    @DeleteMapping(
            value = "/conversations/{conversationId}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    JsonNode deleteConversation(
            @RequestHeader("Authorization") String authorization,
            @RequestHeader("Version") String version,
            @PathVariable("conversationId") String conversationId
    );


}
