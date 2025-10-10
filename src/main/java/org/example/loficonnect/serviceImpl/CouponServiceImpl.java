package org.example.loficonnect.serviceImpl;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import org.example.loficonnect.dto.mapper.coupon.GoHighLevelCouponCreateRequest;
import org.example.loficonnect.dto.mapper.coupon.GoHighLevelCouponUpdateRequest;
import org.example.loficonnect.dto.request.coupon.CouponCreateRequest;
import org.example.loficonnect.dto.request.coupon.CouponUpdateRequest;
import org.example.loficonnect.feignclients.CouponClient;
import org.example.loficonnect.service.AuthorizationService;
import org.example.loficonnect.service.CouponService;
import org.example.loficonnect.util.AppKeyContext;
import org.example.loficonnect.util.VersionContext;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class CouponServiceImpl implements CouponService {

    private final CouponClient couponClient;
    private final AuthorizationService authorizationService;

    public CouponServiceImpl(CouponClient couponClient, AuthorizationService authorizationService) {
        this.couponClient = couponClient;
        this.authorizationService = authorizationService;
    }

    @Override
    public JsonNode getCoupons(Map<String, Object> queryParams) {
        String accessKey = authorizationService.getAccessToken(AppKeyContext.getAppKey());
        String version = VersionContext.getVersion();
        return couponClient.getCoupons(accessKey, version, queryParams);
    }

    @Override
    public JsonNode createCoupon(CouponCreateRequest request) {
        String accessKey = authorizationService.getAccessToken(AppKeyContext.getAppKey());
        String version = VersionContext.getVersion();
        GoHighLevelCouponCreateRequest ghlRequest = GoHighLevelCouponCreateRequest.fromRequest(request);
        return couponClient.createCoupon(accessKey, version, ghlRequest);
    }

    @Override
    public JsonNode updateCoupon(CouponUpdateRequest request) {
        String accessKey = authorizationService.getAccessToken(AppKeyContext.getAppKey());
        String version = VersionContext.getVersion();
        GoHighLevelCouponUpdateRequest ghlRequest = GoHighLevelCouponUpdateRequest.fromRequest(request);
        return couponClient.updateCoupon(accessKey, version, ghlRequest);
    }

    @Override
    public JsonNode deleteCoupon(String altId, String altType, String id) {
        String accessKey = authorizationService.getAccessToken(AppKeyContext.getAppKey());
        String version = VersionContext.getVersion();

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("altId", altId);
        requestBody.put("altType", altType);
        requestBody.put("id", id);

        return couponClient.deleteCoupon(accessKey, version, requestBody);
    }

    @Override
    public JsonNode getCouponDetails(Map<String, Object> queryParams) {
        String accessKey = authorizationService.getAccessToken(AppKeyContext.getAppKey());
        String version = VersionContext.getVersion();
        return couponClient.getCouponDetails(accessKey, version, queryParams);
    }

}
