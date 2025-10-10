package org.example.loficonnect.serviceImpl;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import org.example.loficonnect.feignclients.OrderClient;
import org.example.loficonnect.service.AuthorizationService;
import org.example.loficonnect.service.OrderService;
import org.example.loficonnect.util.AppKeyContext;
import org.example.loficonnect.util.VersionContext;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@Slf4j
public class OrderServiceImpl implements OrderService {

    private final OrderClient orderClient;
    private final AuthorizationService authorizationService;

    public OrderServiceImpl(OrderClient orderClient, AuthorizationService authorizationService) {
        this.orderClient = orderClient;
        this.authorizationService = authorizationService;
    }

    @Override
    public JsonNode getOrders(Map<String, Object> queryParams) {
        String accessKey = authorizationService.getAccessToken(AppKeyContext.getAppKey());
        String version = VersionContext.getVersion();
        return orderClient.getOrders(accessKey, version, queryParams);
    }

    @Override
    public JsonNode getOrderById(String orderId, Map<String, Object> queryParams) {
        String accessKey = authorizationService.getAccessToken(AppKeyContext.getAppKey());
        String version = VersionContext.getVersion();
        return orderClient.getOrderById(accessKey, version, orderId, queryParams);
    }

}
