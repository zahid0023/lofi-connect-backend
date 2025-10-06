package org.example.loficonnect.serviceImpl;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import org.example.loficonnect.feignclients.CategoryClient;
import org.example.loficonnect.service.AuthorizationService;
import org.example.loficonnect.service.CategoryService;
import org.example.loficonnect.util.AppKeyContext;
import org.example.loficonnect.util.VersionContext;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@Slf4j
public class CategoryServiceImpl implements CategoryService {

    private final CategoryClient categoryClient;
    private final AuthorizationService authorizationService;

    public CategoryServiceImpl(CategoryClient categoryClient, AuthorizationService authorizationService) {
        this.categoryClient = categoryClient;
        this.authorizationService = authorizationService;
    }

    @Override
    public JsonNode getCategories(String locationId, Map<String, Object> queryParams) {
        String accessKey = authorizationService.getAccessToken(AppKeyContext.getAppKey());
        String version = VersionContext.getVersion();
        return categoryClient.getCategories(accessKey, version, locationId, queryParams);
    }

    @Override
    public JsonNode getCategoryById(String locationId, String categoryId) {
        String accessToken = authorizationService.getAccessToken(AppKeyContext.getAppKey());
        String version = VersionContext.getVersion();
        return categoryClient.getCategoryById(accessToken, version, locationId, categoryId);
    }

}
