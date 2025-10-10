package org.example.loficonnect.serviceImpl;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import org.example.loficonnect.dto.mapper.businesses.GoHighLevelBusinessCreateRequest;
import org.example.loficonnect.dto.mapper.businesses.GoHighLevelBusinessUpdateRequest;
import org.example.loficonnect.dto.request.businesses.BusinessCreateRequest;
import org.example.loficonnect.dto.request.businesses.BusinessUpdateRequest;
import org.example.loficonnect.feignclients.BusinessClient;
import org.example.loficonnect.service.AuthorizationService;
import org.example.loficonnect.service.BusinessService;
import org.example.loficonnect.util.AppKeyContext;
import org.example.loficonnect.util.VersionContext;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@Slf4j
public class BusinessServiceImpl implements BusinessService {
    private final AuthorizationService authorizationService;
    private final BusinessClient businessClient;

    public BusinessServiceImpl(AuthorizationService authorizationService,BusinessClient businessClient) {
        this.authorizationService = authorizationService;
        this.businessClient = businessClient;
    }

    @Override
    public JsonNode getBusiness(String businessId) {
        String accessKey = authorizationService.getAccessToken(AppKeyContext.getAppKey());
        String version = VersionContext.getVersion();
        return businessClient.getBusiness(accessKey, version, businessId);
    }

    @Override
    public JsonNode getBusinessesByLocation(Map<String, Object> queryParams) {
        String accessKey = authorizationService.getAccessToken(AppKeyContext.getAppKey());
        String version = VersionContext.getVersion();
        return businessClient.getBusinessesByLocation(accessKey, version, queryParams);
    }

    @Override
    public JsonNode createBusiness(BusinessCreateRequest request) {
        String accessKey = authorizationService.getAccessToken(AppKeyContext.getAppKey());
        String version = VersionContext.getVersion();
        GoHighLevelBusinessCreateRequest ghlRequest = GoHighLevelBusinessCreateRequest.fromRequest(request);
        return businessClient.createBusiness(accessKey, version, ghlRequest);
    }

    @Override
    public JsonNode updateBusiness(String businessId, BusinessUpdateRequest request) {
        String accessKey = authorizationService.getAccessToken(AppKeyContext.getAppKey());
        String version = VersionContext.getVersion();
        GoHighLevelBusinessUpdateRequest ghlRequest = GoHighLevelBusinessUpdateRequest.fromRequest(request);
        return businessClient.updateBusiness(accessKey, version, businessId, ghlRequest);
    }

    @Override
    public JsonNode deleteBusiness(String businessId) {
        String accessKey = authorizationService.getAccessToken(AppKeyContext.getAppKey());
        String version = VersionContext.getVersion();
        return businessClient.deleteBusiness(accessKey, version, businessId);
    }

}