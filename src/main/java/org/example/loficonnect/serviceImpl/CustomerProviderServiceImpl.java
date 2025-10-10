package org.example.loficonnect.serviceImpl;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import org.example.loficonnect.dto.mapper.customerprovider.GoHighLevelCustomProviderCreateRequest;
import org.example.loficonnect.dto.mapper.customerprovider.GoHighLevelCustomProviderDisconnectRequest;
import org.example.loficonnect.dto.mapper.customerprovider.GoHighLevelCustomerProviderCreateRequest;
import org.example.loficonnect.dto.request.customerprovider.CustomProviderCreateRequest;
import org.example.loficonnect.dto.request.customerprovider.CustomProviderDisconnectRequest;
import org.example.loficonnect.dto.request.customerprovider.CustomerProviderCreateRequest;
import org.example.loficonnect.feignclients.CustomerProviderClient;
import org.example.loficonnect.service.AuthorizationService;
import org.example.loficonnect.service.CustomerProviderService;
import org.example.loficonnect.util.AppKeyContext;
import org.example.loficonnect.util.VersionContext;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CustomerProviderServiceImpl implements CustomerProviderService {

    private final CustomerProviderClient customerProviderClient;
    private final AuthorizationService authorizationService;

    public CustomerProviderServiceImpl(CustomerProviderClient customerProviderClient, AuthorizationService authorizationService) {
        this.customerProviderClient = customerProviderClient;
        this.authorizationService = authorizationService;
    }

    @Override
    public JsonNode createCustomProvider(String locationId, CustomerProviderCreateRequest request) {
        String accessKey = authorizationService.getAccessToken(AppKeyContext.getAppKey());
        String version = VersionContext.getVersion();
        GoHighLevelCustomerProviderCreateRequest ghlRequest = GoHighLevelCustomerProviderCreateRequest.fromRequest(request);
        return customerProviderClient.createCustomProvider(accessKey, version, locationId, ghlRequest);
    }

    @Override
    public JsonNode deleteCustomProvider(String locationId) {
        String token = authorizationService.getAccessToken(AppKeyContext.getAppKey());
        String version = VersionContext.getVersion();
        return customerProviderClient.deleteCustomProvider(token, version, locationId);
    }

    @Override
    public JsonNode getCustomProviderConfig(String locationId) {
        String token = authorizationService.getAccessToken(AppKeyContext.getAppKey());
        String version = VersionContext.getVersion();
        return customerProviderClient.getCustomProviderConfig(token, version, locationId);
    }

    @Override
    public JsonNode createCustomProviderConfig(String locationId, CustomProviderCreateRequest request) {
        String accessKey = authorizationService.getAccessToken(AppKeyContext.getAppKey());
        String version = VersionContext.getVersion();
        GoHighLevelCustomProviderCreateRequest ghlRequest = GoHighLevelCustomProviderCreateRequest.fromRequest(request);
        return customerProviderClient.createCustomProviderConfig(accessKey, version, locationId, ghlRequest);
    }

    @Override
    public JsonNode disconnectCustomProviderConfig(String locationId, CustomProviderDisconnectRequest request) {
        String accessKey = authorizationService.getAccessToken(AppKeyContext.getAppKey());
        String version = VersionContext.getVersion();
        GoHighLevelCustomProviderDisconnectRequest ghlRequest = GoHighLevelCustomProviderDisconnectRequest.fromRequest(request);
        return customerProviderClient.disconnectCustomProviderConfig(accessKey, version, locationId, ghlRequest);
    }

}
