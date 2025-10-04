package org.example.loficonnect.serviceImpl;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import org.example.loficonnect.dto.mapper.product.GoHighLevelProductBulkUpdateRequest;
import org.example.loficonnect.dto.mapper.product.GoHighLevelProductCreateRequest;
import org.example.loficonnect.dto.mapper.product.GoHighLevelProductUpdateRequest;
import org.example.loficonnect.dto.request.product.ProductBulkUpdateRequest;
import org.example.loficonnect.dto.request.product.ProductCreateRequest;
import org.example.loficonnect.dto.request.product.ProductUpdateRequest;
import org.example.loficonnect.feignclients.ProductClient;
import org.example.loficonnect.service.AuthorizationService;
import org.example.loficonnect.service.ProductService;
import org.example.loficonnect.util.AppKeyContext;
import org.example.loficonnect.util.VersionContext;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@Slf4j
public class ProductServiceImpl implements ProductService {

    private final ProductClient productClient;
    private final AuthorizationService authorizationService;

    public ProductServiceImpl(ProductClient productClient, AuthorizationService authorizationService) {
        this.productClient = productClient;
        this.authorizationService = authorizationService;
    }

    @Override
    public JsonNode bulkUpdateProducts(ProductBulkUpdateRequest request) {
        String accessKey = authorizationService.getAccessToken(AppKeyContext.getAppKey());
        String version = VersionContext.getVersion();
        GoHighLevelProductBulkUpdateRequest ghlRequest = GoHighLevelProductBulkUpdateRequest.fromRequest(request);
        return productClient.bulkUpdateProducts(accessKey, version, ghlRequest);
    }

    @Override
    public JsonNode getProductById(String productId, Map<String, Object> queryParams) {
        String accessKey = authorizationService.getAccessToken(AppKeyContext.getAppKey());
        String version = VersionContext.getVersion();
        return productClient.getProductById(accessKey, version, productId, queryParams);
    }

    @Override
    public JsonNode deleteProductById(String productId, Map<String, Object> queryParams) {
        String accessKey = authorizationService.getAccessToken(AppKeyContext.getAppKey());
        String version = VersionContext.getVersion();
        return productClient.deleteProductById(accessKey, version, productId, queryParams);
    }

    @Override
    public JsonNode updateProductById(String productId, ProductUpdateRequest request) {
        String accessKey = authorizationService.getAccessToken(AppKeyContext.getAppKey());
        String version = VersionContext.getVersion();
        GoHighLevelProductUpdateRequest ghlRequest = GoHighLevelProductUpdateRequest.fromRequest(request);
        return productClient.updateProductById(accessKey, version, productId, ghlRequest);
    }

    @Override
    public JsonNode createProduct(ProductCreateRequest request) {
        String accessKey = authorizationService.getAccessToken(AppKeyContext.getAppKey());
        String version = VersionContext.getVersion();
        GoHighLevelProductCreateRequest ghlRequest = GoHighLevelProductCreateRequest.fromRequest(request);
        return productClient.createProduct(accessKey, version, ghlRequest);
    }

    @Override
    public JsonNode getProducts(Map<String, Object> queryParams) {
        String accessKey = authorizationService.getAccessToken(AppKeyContext.getAppKey());
        String version = VersionContext.getVersion();
        return productClient.getProducts(accessKey, version, queryParams);
    }

}
