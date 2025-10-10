package org.example.loficonnect.service;

import com.fasterxml.jackson.databind.JsonNode;
import org.example.loficonnect.dto.request.product.ProductBulkUpdateRequest;
import org.example.loficonnect.dto.request.product.ProductCreateRequest;
import org.example.loficonnect.dto.request.product.ProductUpdateRequest;

import java.util.Map;

public interface ProductService {
    JsonNode bulkUpdateProducts(ProductBulkUpdateRequest request);
    JsonNode getProductById(String productId, Map<String, Object> queryParams);
    JsonNode deleteProductById(String productId, Map<String, Object> queryParams);
    JsonNode updateProductById(String productId, ProductUpdateRequest request);
    JsonNode createProduct(ProductCreateRequest request);
    JsonNode getProducts(Map<String, Object> queryParams);
}
