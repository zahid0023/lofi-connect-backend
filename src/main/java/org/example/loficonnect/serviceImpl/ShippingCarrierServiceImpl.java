package org.example.loficonnect.serviceImpl;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import org.example.loficonnect.dto.mapper.shippingcarrier.GoHighLevelShippingCarrierCreateRequest;
import org.example.loficonnect.dto.mapper.shippingcarrier.GoHighLevelShippingCarrierUpdateRequest;
import org.example.loficonnect.dto.request.shippingcarrier.ShippingCarrierCreateRequest;
import org.example.loficonnect.dto.request.shippingcarrier.ShippingCarrierUpdateRequest;
import org.example.loficonnect.feignclients.ShippingCarrierClient;
import org.example.loficonnect.service.ShippingCarrierService;
import org.example.loficonnect.service.AuthorizationService;
import org.example.loficonnect.util.AppKeyContext;
import org.example.loficonnect.util.VersionContext;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@Slf4j
public class ShippingCarrierServiceImpl implements ShippingCarrierService {

    private final ShippingCarrierClient shippingCarrierClient;
    private final AuthorizationService authorizationService;

    public ShippingCarrierServiceImpl(ShippingCarrierClient shippingCarrierClient, AuthorizationService authorizationService) {
        this.shippingCarrierClient = shippingCarrierClient;
        this.authorizationService = authorizationService;
    }

    @Override
    public JsonNode createShippingCarrier(ShippingCarrierCreateRequest request) {
        String accessKey = authorizationService.getAccessToken(AppKeyContext.getAppKey());
        String version = VersionContext.getVersion();
        GoHighLevelShippingCarrierCreateRequest ghlRequest = GoHighLevelShippingCarrierCreateRequest.fromRequest(request);
        return shippingCarrierClient.createShippingCarrier(accessKey, version, ghlRequest);
    }

    @Override
    public JsonNode listShippingCarriers(Map<String, Object> queryParams) {
        String accessKey = authorizationService.getAccessToken(AppKeyContext.getAppKey());
        String version = VersionContext.getVersion();
        return shippingCarrierClient.listShippingCarriers(accessKey, version, queryParams);
    }

    @Override
    public JsonNode getShippingCarrier(String shippingCarrierId, Map<String, Object> queryParams) {
        String accessKey = authorizationService.getAccessToken(AppKeyContext.getAppKey());
        String version = VersionContext.getVersion();
        return shippingCarrierClient.getShippingCarrier(accessKey, version, shippingCarrierId, queryParams);
    }

    @Override
    public JsonNode updateShippingCarrier(String shippingCarrierId, ShippingCarrierUpdateRequest request) {
        String accessKey = authorizationService.getAccessToken(AppKeyContext.getAppKey());
        String version = VersionContext.getVersion();
        GoHighLevelShippingCarrierUpdateRequest ghlRequest = GoHighLevelShippingCarrierUpdateRequest.fromRequest(request);
        return shippingCarrierClient.updateShippingCarrier(accessKey, version, shippingCarrierId, ghlRequest);
    }

    @Override
    public JsonNode deleteShippingCarrier(String shippingCarrierId, Map<String, Object> queryParams) {
        String accessKey = authorizationService.getAccessToken(AppKeyContext.getAppKey());
        String version = VersionContext.getVersion();
        return shippingCarrierClient.deleteShippingCarrier(accessKey, version, shippingCarrierId, queryParams);
    }

}
