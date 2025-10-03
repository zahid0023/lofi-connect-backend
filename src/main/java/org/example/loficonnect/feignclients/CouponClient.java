package org.example.loficonnect.feignclients;

import com.fasterxml.jackson.databind.JsonNode;
import org.example.loficonnect.config.FeignLoggingConfig;
import org.example.loficonnect.dto.mapper.coupon.GoHighLevelCouponCreateRequest;
import org.example.loficonnect.dto.mapper.coupon.GoHighLevelCouponUpdateRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@FeignClient(name = "couponClient", url = "https://services.leadconnectorhq.com", configuration = FeignLoggingConfig.class)
public interface CouponClient {

    @GetMapping(
            value = "/payments/coupon/list",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    JsonNode getCoupons(
            @RequestHeader("Authorization") String authorization,
            @RequestHeader("Version") String version,
            @RequestParam Map<String, Object> queryParams
    );

    @PostMapping(
            value = "/payments/coupon",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    JsonNode createCoupon(
            @RequestHeader("Authorization") String authorization,
            @RequestHeader("Version") String version,
            @RequestBody GoHighLevelCouponCreateRequest request
    );

    @PutMapping(
            value = "/payments/coupon",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    JsonNode updateCoupon(
            @RequestHeader("Authorization") String authorization,
            @RequestHeader("Version") String version,
            @RequestBody GoHighLevelCouponUpdateRequest request
    );

    @DeleteMapping(
            value = "/payments/coupon",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    JsonNode deleteCoupon(
            @RequestHeader("Authorization") String authorization,
            @RequestHeader("Version") String version,
            @RequestBody Map<String, Object> requestBody
    );

    @GetMapping(
            value = "/payments/coupon",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    JsonNode getCouponDetails(
            @RequestHeader("Authorization") String authorization,
            @RequestHeader("Version") String version,
            @RequestParam Map<String, Object> queryParams
    );

}
