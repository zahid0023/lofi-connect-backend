package org.example.loficonnect.serviceImpl;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import org.example.loficonnect.dto.mapper.shippingzonerate.GoHighLevelShippingZoneRateCreateRequest;
import org.example.loficonnect.dto.mapper.shippingzonerate.GoHighLevelShippingZoneRateUpdateRequest;
import org.example.loficonnect.dto.request.shippingzonerate.ShippingZoneRateCreateRequest;
import org.example.loficonnect.dto.request.shippingzonerate.ShippingZoneRateUpdateRequest;
import org.example.loficonnect.feignclients.ShippingZoneRateClient;
import org.example.loficonnect.service.AuthorizationService;
import org.example.loficonnect.service.ShippingZoneRateService;
import org.example.loficonnect.util.AppKeyContext;
import org.example.loficonnect.util.VersionContext;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@Slf4j
public class ShippingZoneRateServiceImpl implements ShippingZoneRateService {

    private final ShippingZoneRateClient shippingZoneRateClient;
    private final AuthorizationService authorizationService;

    public ShippingZoneRateServiceImpl(ShippingZoneRateClient shippingZoneRateClient, AuthorizationService authorizationService) {
        this.shippingZoneRateClient = shippingZoneRateClient;
        this.authorizationService = authorizationService;
    }

    @Override
    public JsonNode createShippingZoneRate(String shippingZoneId, ShippingZoneRateCreateRequest request) {
        String accessKey = authorizationService.getAccessToken(AppKeyContext.getAppKey());
        String version = VersionContext.getVersion();
        GoHighLevelShippingZoneRateCreateRequest ghlRequest = GoHighLevelShippingZoneRateCreateRequest.fromRequest(request);
        return shippingZoneRateClient.createShippingZoneRate(accessKey, version, shippingZoneId, ghlRequest);
    }

    @Override
    public JsonNode listShippingZoneRates(String shippingZoneId, Map<String, Object> queryParams) {
        String accessKey = authorizationService.getAccessToken(AppKeyContext.getAppKey());
        String version = VersionContext.getVersion();
        return shippingZoneRateClient.listShippingZoneRates(accessKey, version, shippingZoneId, queryParams);
    }

    @Override
    public JsonNode getShippingZoneRateById(String shippingZoneId, String shippingRateId, Map<String, Object> queryParams) {
        String accessKey = authorizationService.getAccessToken(AppKeyContext.getAppKey());
        String version = VersionContext.getVersion();
        return shippingZoneRateClient.getShippingZoneRateById(accessKey, version, shippingZoneId, shippingRateId, queryParams);
    }

    @Override
    public JsonNode updateShippingZoneRate(String shippingZoneId, String shippingRateId, ShippingZoneRateUpdateRequest request) {
        String accessKey = authorizationService.getAccessToken(AppKeyContext.getAppKey());
        String version = VersionContext.getVersion();
        GoHighLevelShippingZoneRateUpdateRequest ghlRequest = GoHighLevelShippingZoneRateUpdateRequest.fromRequest(request);
        return shippingZoneRateClient.updateShippingZoneRate(accessKey, version, shippingZoneId, shippingRateId, ghlRequest);
    }

    @Override
    public JsonNode deleteShippingZoneRate(String shippingZoneId, String shippingRateId, Map<String, Object> queryParams) {
        String accessKey = authorizationService.getAccessToken(AppKeyContext.getAppKey());
        String version = VersionContext.getVersion();
        return shippingZoneRateClient.deleteShippingZoneRate(accessKey, version, shippingZoneId, shippingRateId, queryParams);
    }

}
