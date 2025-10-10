package org.example.loficonnect.serviceImpl;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import org.example.loficonnect.dto.mapper.shippingzone.GoHighLevelShippingRateRequest;
import org.example.loficonnect.dto.mapper.shippingzone.GoHighLevelShippingZoneCreateRequest;
import org.example.loficonnect.dto.mapper.shippingzone.GoHighLevelShippingZoneUpdateRequest;
import org.example.loficonnect.dto.request.shippingzone.ShippingRateRequest;
import org.example.loficonnect.dto.request.shippingzone.ShippingZoneCreateRequest;
import org.example.loficonnect.dto.request.shippingzone.ShippingZoneUpdateRequest;
import org.example.loficonnect.feignclients.ShippingZoneClient;
import org.example.loficonnect.service.AuthorizationService;
import org.example.loficonnect.service.ShippingZoneService;
import org.example.loficonnect.util.AppKeyContext;
import org.example.loficonnect.util.VersionContext;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@Slf4j
public class ShippingZoneServiceImpl implements ShippingZoneService {

    private final ShippingZoneClient shippingZoneClient;
    private final AuthorizationService authorizationService;

    public ShippingZoneServiceImpl(ShippingZoneClient shippingZoneClient, AuthorizationService authorizationService) {
        this.shippingZoneClient = shippingZoneClient;
        this.authorizationService = authorizationService;
    }

    @Override
    public JsonNode createShippingZone(ShippingZoneCreateRequest request) {
        String accessKey = authorizationService.getAccessToken(AppKeyContext.getAppKey());
        String version = VersionContext.getVersion();
        GoHighLevelShippingZoneCreateRequest ghlRequest = GoHighLevelShippingZoneCreateRequest.fromRequest(request);
        return shippingZoneClient.createShippingZone(accessKey, version, ghlRequest);
    }

    @Override
    public JsonNode listShippingZones(Map<String, Object> queryParams) {
        String accessKey = authorizationService.getAccessToken(AppKeyContext.getAppKey());
        String version = VersionContext.getVersion();
        return shippingZoneClient.listShippingZones(accessKey, version, queryParams);
    }

    @Override
    public JsonNode getShippingZoneById(String shippingZoneId, Map<String, Object> queryParams) {
        String accessKey = authorizationService.getAccessToken(AppKeyContext.getAppKey());
        String version = VersionContext.getVersion();
        return shippingZoneClient.getShippingZoneById(accessKey, version, shippingZoneId, queryParams);
    }

    @Override
    public JsonNode updateShippingZone(String shippingZoneId, ShippingZoneUpdateRequest request) {
        String accessKey = authorizationService.getAccessToken(AppKeyContext.getAppKey());
        String version = VersionContext.getVersion();
        GoHighLevelShippingZoneUpdateRequest ghlRequest = GoHighLevelShippingZoneUpdateRequest.fromRequest(request);
        return shippingZoneClient.updateShippingZone(accessKey, version, shippingZoneId, ghlRequest);
    }

    @Override
    public JsonNode deleteShippingZone(String shippingZoneId, Map<String, Object> queryParams) {
        String accessKey = authorizationService.getAccessToken(AppKeyContext.getAppKey());
        String version = VersionContext.getVersion();
        return shippingZoneClient.deleteShippingZone(accessKey, version, shippingZoneId, queryParams);
    }

    @Override
    public JsonNode getShippingRates(ShippingRateRequest request) {
        String accessKey = authorizationService.getAccessToken(AppKeyContext.getAppKey());
        String version = VersionContext.getVersion();
        GoHighLevelShippingRateRequest ghlRequest = GoHighLevelShippingRateRequest.fromRequest(request);
        return shippingZoneClient.getShippingRates(accessKey, version, ghlRequest);
    }

}
