package org.example.loficonnect.serviceImpl;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import org.example.loficonnect.feignclients.SubscriptionClient;
import org.example.loficonnect.service.AuthorizationService;
import org.example.loficonnect.service.SubscriptionService;
import org.example.loficonnect.util.AppKeyContext;
import org.example.loficonnect.util.VersionContext;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@Slf4j
public class SubscriptionServiceImpl implements SubscriptionService {

    private final SubscriptionClient subscriptionClient;
    private final AuthorizationService authorizationService;

    public SubscriptionServiceImpl(SubscriptionClient subscriptionClient, AuthorizationService authorizationService) {
        this.subscriptionClient = subscriptionClient;
        this.authorizationService = authorizationService;
    }

    @Override
    public JsonNode getSubscriptions(Map<String, Object> queryParams) {
        String accessKey = authorizationService.getAccessToken(AppKeyContext.getAppKey());
        String version = VersionContext.getVersion();
        return subscriptionClient.getSubscriptions(accessKey, version, queryParams);
    }

    @Override
    public JsonNode getSubscriptionById(String subscriptionId, Map<String, Object> queryParams) {
        String accessKey = authorizationService.getAccessToken(AppKeyContext.getAppKey());
        String version = VersionContext.getVersion();
        return subscriptionClient.getSubscriptionById(accessKey, version, subscriptionId, queryParams);
    }

}
