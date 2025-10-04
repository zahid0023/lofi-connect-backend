package org.example.loficonnect.serviceImpl;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import org.example.loficonnect.dto.mapper.prices.GoHighLevelPriceCreateRequest;
import org.example.loficonnect.dto.mapper.prices.GoHighLevelPriceInventoryUpdateRequest;
import org.example.loficonnect.dto.mapper.prices.GoHighLevelPriceUpdateRequest;
import org.example.loficonnect.dto.request.prices.PriceCreateRequest;
import org.example.loficonnect.dto.request.prices.PriceInventoryUpdateRequest;
import org.example.loficonnect.dto.request.prices.PriceUpdateRequest;
import org.example.loficonnect.feignclients.PriceClient;
import org.example.loficonnect.service.AuthorizationService;
import org.example.loficonnect.service.PriceService;
import org.example.loficonnect.util.AppKeyContext;
import org.example.loficonnect.util.VersionContext;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@Slf4j
public class PriceServiceImpl implements PriceService {

    private final PriceClient priceClient;
    private final AuthorizationService authorizationService;

    public PriceServiceImpl(PriceClient priceClient, AuthorizationService authorizationService) {
        this.priceClient = priceClient;
        this.authorizationService = authorizationService;
    }

    @Override
    public JsonNode createPrice(String productId, PriceCreateRequest request) {
        String accessKey = authorizationService.getAccessToken(AppKeyContext.getAppKey());
        String version = VersionContext.getVersion();
        GoHighLevelPriceCreateRequest ghlRequest = GoHighLevelPriceCreateRequest.fromRequest(request);
        return priceClient.createPrice(accessKey, version, productId, ghlRequest);
    }

    @Override
    public JsonNode getPrices(String productId, Map<String, Object> queryParams) {
        String accessKey = authorizationService.getAccessToken(AppKeyContext.getAppKey());
        String version = VersionContext.getVersion();
        return priceClient.getPrices(accessKey, version, productId, queryParams);
    }

    @Override
    public JsonNode listInventory(Map<String, Object> queryParams) {
        String accessKey = authorizationService.getAccessToken(AppKeyContext.getAppKey());
        String version = VersionContext.getVersion();
        return priceClient.listInventory(accessKey, version, queryParams);
    }

    @Override
    public JsonNode updateInventory(PriceInventoryUpdateRequest request) {
        String accessKey = authorizationService.getAccessToken(AppKeyContext.getAppKey());
        String version = VersionContext.getVersion();
        GoHighLevelPriceInventoryUpdateRequest ghlRequest = GoHighLevelPriceInventoryUpdateRequest.fromRequest(request);
        return priceClient.updateInventory(accessKey, version, ghlRequest);
    }

    @Override
    public JsonNode getPriceById(String productId, String priceId, Map<String, Object> queryParams) {
        String accessKey = authorizationService.getAccessToken(AppKeyContext.getAppKey());
        String version = VersionContext.getVersion();
        return priceClient.getPriceById(accessKey, version, productId, priceId, queryParams);
    }

    @Override
    public JsonNode updatePrice(String productId, String priceId, PriceUpdateRequest request) {
        String accessKey = authorizationService.getAccessToken(AppKeyContext.getAppKey());
        String version = VersionContext.getVersion();
        GoHighLevelPriceUpdateRequest ghlRequest = GoHighLevelPriceUpdateRequest.fromRequest(request);
        return priceClient.updatePrice(accessKey, version, productId, priceId, ghlRequest);
    }

    @Override
    public JsonNode deletePrice(String productId, String priceId, Map<String, Object> queryParams) {
        String accessKey = authorizationService.getAccessToken(AppKeyContext.getAppKey());
        String version = VersionContext.getVersion();
        return priceClient.deletePrice(accessKey, version, productId, priceId, queryParams);
    }

}
