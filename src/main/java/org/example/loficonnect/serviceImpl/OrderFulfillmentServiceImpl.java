package org.example.loficonnect.serviceImpl;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import org.example.loficonnect.dto.mapper.orderfulfillment.GoHighLevelOrderFulfillmentCreateRequest;
import org.example.loficonnect.dto.request.orderfulfillment.OrderFulfillmentCreateRequest;
import org.example.loficonnect.feignclients.OrderFulfillmentClient;
import org.example.loficonnect.service.AuthorizationService;
import org.example.loficonnect.service.OrderFulfillmentService;
import org.example.loficonnect.util.AppKeyContext;
import org.example.loficonnect.util.VersionContext;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@Slf4j
public class OrderFulfillmentServiceImpl implements OrderFulfillmentService {

    private final OrderFulfillmentClient orderFulfillmentClient;
    private final AuthorizationService authorizationService;

    public OrderFulfillmentServiceImpl(OrderFulfillmentClient orderFulfillmentClient, AuthorizationService authorizationService) {
        this.orderFulfillmentClient = orderFulfillmentClient;
        this.authorizationService = authorizationService;
    }

    @Override
    public JsonNode createFulfillment(String orderId, OrderFulfillmentCreateRequest request) {
        String accessKey = authorizationService.getAccessToken(AppKeyContext.getAppKey());
        String version = VersionContext.getVersion();
        GoHighLevelOrderFulfillmentCreateRequest ghlRequest = GoHighLevelOrderFulfillmentCreateRequest.fromRequest(request);
        return orderFulfillmentClient.createFulfillment(accessKey, version, orderId, ghlRequest);
    }

    @Override
    public JsonNode getFulfillments(String orderId, Map<String, Object> queryParams) {
        String accessKey = authorizationService.getAccessToken(AppKeyContext.getAppKey());
        String version = VersionContext.getVersion();
        return orderFulfillmentClient.getFulfillments(accessKey, version, orderId, queryParams);
    }

}
