package org.example.loficonnect.serviceImpl;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import org.example.loficonnect.dto.mapper.providers.GoHighLevelLiveChatTypingRequest;
import org.example.loficonnect.dto.request.providers.LiveChatTypingRequest;
import org.example.loficonnect.feignclients.ProvidersClient;
import org.example.loficonnect.service.AuthorizationService;
import org.example.loficonnect.service.ProvidersService;
import org.example.loficonnect.util.AppKeyContext;
import org.example.loficonnect.util.VersionContext;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ProvidersServiceImpl implements ProvidersService {
    private final AuthorizationService authorizationService;
    private final ProvidersClient providersClient;

    public ProvidersServiceImpl(AuthorizationService authorizationService, ProvidersClient providersClient) {
        this.authorizationService = authorizationService;
        this.providersClient = providersClient;
    }

    @Override
    public JsonNode liveChatTyping(LiveChatTypingRequest request) {
        String accessKey = authorizationService.getAccessToken(AppKeyContext.getAppKey());
        String version = VersionContext.getVersion();
        GoHighLevelLiveChatTypingRequest ghlRequest = GoHighLevelLiveChatTypingRequest.fromRequest(request);
        return providersClient.liveChatTyping(accessKey, version, ghlRequest);
    }

}
