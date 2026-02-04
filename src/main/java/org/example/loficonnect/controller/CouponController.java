package org.example.loficonnect.controller;

import org.example.loficonnect.commons.annotation.AppKey;
import org.example.loficonnect.dto.request.coupon.CouponCreateRequest;
import org.example.loficonnect.dto.request.coupon.CouponUpdateRequest;
import org.example.loficonnect.service.CouponService;
import org.example.loficonnect.util.MapUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/ghl")
public class CouponController {

    private final CouponService couponService;

    public CouponController(CouponService couponService) {
        this.couponService = couponService;
    }

    @AppKey
    @GetMapping("/payments/coupons")
    public ResponseEntity<?> getCoupons(
            @RequestParam("alt-id") String altId,
            @RequestParam("alt-type") String altType,
            @RequestParam(value = "limit", required = false, defaultValue = "100") Integer limit,
            @RequestParam(value = "offset", required = false, defaultValue = "0") Integer offset,
            @RequestParam(value = "search", required = false) String search,
            @RequestParam(value = "status", required = false) String status
    ) {
        Map<String, Object> queryParams = new HashMap<>();
        MapUtil.putIfNotNull(queryParams, "altId", altId);
        MapUtil.putIfNotNull(queryParams, "altType", altType);
        MapUtil.putIfNotNull(queryParams, "limit", limit);
        MapUtil.putIfNotNull(queryParams, "offset", offset);
        MapUtil.putIfNotNull(queryParams, "search", search);
        MapUtil.putIfNotNull(queryParams, "status", status);

        return ResponseEntity.ok(couponService.getCoupons(queryParams));
    }

    @AppKey
    @PostMapping("/payments/coupons")
    public ResponseEntity<?> createCoupon(@RequestBody CouponCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(couponService.createCoupon(request));
    }

    @AppKey
    @PutMapping("/payments/coupons")
    public ResponseEntity<?> updateCoupon(@RequestBody CouponUpdateRequest request) {
        return ResponseEntity.ok(couponService.updateCoupon(request));
    }

    @AppKey
    @DeleteMapping("/payments/coupons")
    public ResponseEntity<?> deleteCoupon(
            @RequestParam("alt-id") String altId,
            @RequestParam("alt-type") String altType,
            @RequestParam("id") String id
    ) {
        return ResponseEntity.ok(couponService.deleteCoupon(altId, altType, id));
    }

    @AppKey
    @GetMapping("/payments/coupons/details")
    public ResponseEntity<?> getCouponDetails(
            @RequestParam("alt-id") String altId,
            @RequestParam("alt-type") String altType,
            @RequestParam("code") String code,
            @RequestParam("id") String id
    ) {
        Map<String, Object> queryParams = new HashMap<>();
        MapUtil.putIfNotNull(queryParams, "altId", altId);
        MapUtil.putIfNotNull(queryParams, "altType", altType);
        MapUtil.putIfNotNull(queryParams, "code", code);
        MapUtil.putIfNotNull(queryParams, "id", id);

        return ResponseEntity.ok(couponService.getCouponDetails(queryParams));
    }

}
