package org.example.loficonnect.controller;

import org.example.loficonnect.config.AppKey;
import org.example.loficonnect.dto.request.product.ProductBulkUpdateRequest;
import org.example.loficonnect.dto.request.product.ProductCreateRequest;
import org.example.loficonnect.dto.request.product.ProductUpdateRequest;
import org.example.loficonnect.service.ProductService;
import org.example.loficonnect.util.MapUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @AppKey
    @PostMapping("/bulk-update")
    public ResponseEntity<?> bulkUpdateProducts(@RequestBody ProductBulkUpdateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(productService.bulkUpdateProducts(request));
    }

    @AppKey
    @GetMapping("/{product-id}")
    public ResponseEntity<?> getProductById(
            @PathVariable("product-id") String productId,
            @RequestParam("location-id") String locationId
    ) {
        Map<String, Object> queryParams = new HashMap<>();
        MapUtil.putIfNotNull(queryParams, "locationId", locationId);

        return ResponseEntity.ok(productService.getProductById(productId, queryParams));
    }

    @AppKey
    @DeleteMapping("/{product-id}")
    public ResponseEntity<?> deleteProductById(
            @PathVariable("product-id") String productId,
            @RequestParam("location-id") String locationId
    ) {
        Map<String, Object> queryParams = new HashMap<>();
        MapUtil.putIfNotNull(queryParams, "locationId", locationId);

        return ResponseEntity.ok(productService.deleteProductById(productId, queryParams));
    }

    @AppKey
    @PutMapping("/{product-id}")
    public ResponseEntity<?> updateProductById(
            @PathVariable("product-id") String productId,
            @RequestBody ProductUpdateRequest request
    ) {
        return ResponseEntity.ok(productService.updateProductById(productId, request));
    }

    @AppKey
    @PostMapping
    public ResponseEntity<?> createProduct(@RequestBody ProductCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(productService.createProduct(request));
    }

    @AppKey
    @GetMapping
    public ResponseEntity<?> getProducts(
            @RequestParam("location-id") String locationId,
            @RequestParam(value = "limit", required = false) Integer limit,
            @RequestParam(value = "offset", required = false) Integer offset,
            @RequestParam(value = "available-in-store", required = false) Boolean availableInStore,
            @RequestParam(value = "collection-ids", required = false) String collectionIds,
            @RequestParam(value = "collection-slug", required = false) String collectionSlug,
            @RequestParam(value = "expand", required = false) List<String> expand,
            @RequestParam(value = "included-in-store", required = false) Boolean includedInStore,
            @RequestParam(value = "product-ids", required = false) List<String> productIds,
            @RequestParam(value = "search", required = false) String search,
            @RequestParam(value = "sort-order", required = false) String sortOrder,
            @RequestParam(value = "store-id", required = false) String storeId
    ) {
        Map<String, Object> queryParams = new HashMap<>();
        MapUtil.putIfNotNull(queryParams, "locationId", locationId);
        MapUtil.putIfNotNull(queryParams, "limit", limit);
        MapUtil.putIfNotNull(queryParams, "offset", offset);
        MapUtil.putIfNotNull(queryParams, "availableInStore", availableInStore);
        MapUtil.putIfNotNull(queryParams, "collectionIds", collectionIds);
        MapUtil.putIfNotNull(queryParams, "collectionSlug", collectionSlug);
        MapUtil.putIfNotNull(queryParams, "expand", expand);
        MapUtil.putIfNotNull(queryParams, "includedInStore", includedInStore);
        MapUtil.putIfNotNull(queryParams, "productIds", productIds);
        MapUtil.putIfNotNull(queryParams, "search", search);
        MapUtil.putIfNotNull(queryParams, "sortOrder", sortOrder);
        MapUtil.putIfNotNull(queryParams, "storeId", storeId);

        return ResponseEntity.ok(productService.getProducts(queryParams));
    }

}
