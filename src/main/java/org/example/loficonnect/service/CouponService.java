package org.example.loficonnect.service;

import com.fasterxml.jackson.databind.JsonNode;
import org.example.loficonnect.dto.request.coupon.CouponCreateRequest;
import org.example.loficonnect.dto.request.coupon.CouponUpdateRequest;

import java.util.Map;

public interface CouponService {
    JsonNode getCoupons(Map<String, Object> queryParams);
    JsonNode createCoupon(CouponCreateRequest request);
    JsonNode updateCoupon(CouponUpdateRequest request);
    JsonNode deleteCoupon(String altId, String altType, String id);
    JsonNode getCouponDetails(Map<String, Object> queryParams);
}
