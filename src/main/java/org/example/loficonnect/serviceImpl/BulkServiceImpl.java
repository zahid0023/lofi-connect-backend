package org.example.loficonnect.serviceImpl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.example.loficonnect.dto.mapper.bulk.GoHighLevelBulkBusinessUpdateRequest;
import org.example.loficonnect.dto.mapper.bulk.GoHighLevelBulkTagUpdateRequest;
import org.example.loficonnect.dto.request.bulk.BulkBusinessUpdateRequest;
import org.example.loficonnect.dto.request.bulk.BulkTagUpdateRequest;
import org.example.loficonnect.feignclients.BulkClient;
import org.example.loficonnect.service.AuthorizationService;
import org.example.loficonnect.service.BulkService;
import org.example.loficonnect.util.AppKeyContext;
import org.example.loficonnect.util.VersionContext;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class BulkServiceImpl implements BulkService {
    private final AuthorizationService authorizationService;
    private final BulkClient bulkClient;
    private final ObjectMapper objectMapper;

    public BulkServiceImpl(AuthorizationService authorizationService,
                           BulkClient bulkClient,
                           ObjectMapper objectMapper) {
        this.authorizationService = authorizationService;
        this.bulkClient = bulkClient;
        this.objectMapper = objectMapper;
    }

    @Override
    public JsonNode updateBulkTags(String type, BulkTagUpdateRequest request) {
        String accessKey = authorizationService.getAccessToken(AppKeyContext.getAppKey());
        String version = VersionContext.getVersion();
        GoHighLevelBulkTagUpdateRequest ghlRequest = GoHighLevelBulkTagUpdateRequest.fromRequest(request, objectMapper);
        return bulkClient.updateBulkTags(accessKey, version, type, ghlRequest);
    }

    @Override
    public JsonNode updateBulkBusiness(BulkBusinessUpdateRequest request) {
        String accessKey = authorizationService.getAccessToken(AppKeyContext.getAppKey());
        String version = VersionContext.getVersion();
        GoHighLevelBulkBusinessUpdateRequest ghlRequest = GoHighLevelBulkBusinessUpdateRequest.fromRequest(request, objectMapper);
        return bulkClient.updateBulkBusiness(accessKey, version, ghlRequest);
    }


}
