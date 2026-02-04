package org.example.loficonnect.feignclients;

import com.fasterxml.jackson.databind.JsonNode;
import org.example.loficonnect.commons.config.FeignLoggingConfig;
import org.example.loficonnect.dto.mapper.product.GoHighLevelProductBulkUpdateRequest;
import org.example.loficonnect.dto.mapper.product.GoHighLevelProductCreateRequest;
import org.example.loficonnect.dto.mapper.product.GoHighLevelProductUpdateRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@FeignClient(name = "productClient", url = "https://services.leadconnectorhq.com", configuration = FeignLoggingConfig.class)
public interface ProductClient {

    @PostMapping(
        value = "/products/bulk-update",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    JsonNode bulkUpdateProducts(
        @RequestHeader("Authorization") String authorization,
        @RequestHeader("Version") String version,
        @RequestBody GoHighLevelProductBulkUpdateRequest request
    );

    @GetMapping(
            value = "/products/{productId}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    JsonNode getProductById(
            @RequestHeader("Authorization") String authorization,
            @RequestHeader("Version") String version,
            @PathVariable("productId") String productId,
            @RequestParam Map<String, Object> queryParams
    );

    @DeleteMapping(
            value = "/products/{productId}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    JsonNode deleteProductById(
            @RequestHeader("Authorization") String authorization,
            @RequestHeader("Version") String version,
            @PathVariable("productId") String productId,
            @RequestParam Map<String, Object> queryParams
    );

    @PutMapping(
            value = "/products/{productId}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    JsonNode updateProductById(
            @RequestHeader("Authorization") String authorization,
            @RequestHeader("Version") String version,
            @PathVariable("productId") String productId,
            @RequestBody GoHighLevelProductUpdateRequest request
    );

    @PostMapping(
            value = "/products",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    JsonNode createProduct(
            @RequestHeader("Authorization") String authorization,
            @RequestHeader("Version") String version,
            @RequestBody GoHighLevelProductCreateRequest request
    );

    @GetMapping(
            value = "/products",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    JsonNode getProducts(
            @RequestHeader("Authorization") String authorization,
            @RequestHeader("Version") String version,
            @RequestParam Map<String, Object> queryParams
    );

}
