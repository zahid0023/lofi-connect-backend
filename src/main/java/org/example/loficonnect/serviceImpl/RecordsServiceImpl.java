package org.example.loficonnect.serviceImpl;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import org.example.loficonnect.dto.mapper.records.GoHighLevelCreateRecordRequest;
import org.example.loficonnect.dto.mapper.records.GoHighLevelUpdateRecordRequest;
import org.example.loficonnect.dto.request.records.RecordCreateRequest;
import org.example.loficonnect.dto.request.records.RecordUpdateRequest;
import org.example.loficonnect.feignclients.RecordsClient;
import org.example.loficonnect.service.AuthorizationService;
import org.example.loficonnect.service.RecordsService;
import org.example.loficonnect.util.AppKeyContext;
import org.example.loficonnect.util.VersionContext;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class RecordsServiceImpl implements RecordsService {

    private final RecordsClient recordsClient;
    private final AuthorizationService authorizationService;

    public RecordsServiceImpl(RecordsClient recordsClient, AuthorizationService authorizationService) {
        this.recordsClient = recordsClient;
        this.authorizationService = authorizationService;
    }

    @Override
    public JsonNode getRecordById(String schemaKey, String id) {
        String accessKey = authorizationService.getAccessToken(AppKeyContext.getAppKey());
        String version = VersionContext.getVersion();
        return recordsClient.getRecordById(accessKey, version, schemaKey, id);
    }

    @Override
    public JsonNode updateRecord(String schemaKey, String id, RecordUpdateRequest request) {
        String accessKey = authorizationService.getAccessToken(AppKeyContext.getAppKey());
        String version = VersionContext.getVersion();
        GoHighLevelUpdateRecordRequest ghlRequest = GoHighLevelUpdateRecordRequest.fromRequest(request);
        return recordsClient.updateRecord(accessKey, version, schemaKey, id, ghlRequest);
    }

    @Override
    public JsonNode deleteRecord(String schemaKey, String id) {
        String accessKey = authorizationService.getAccessToken(AppKeyContext.getAppKey());
        String version = VersionContext.getVersion();
        return recordsClient.deleteRecord(accessKey, version, schemaKey, id);
    }

    @Override
    public JsonNode createRecord(String schemaKey, RecordCreateRequest request) {
        String accessKey = authorizationService.getAccessToken(AppKeyContext.getAppKey());
        String version = VersionContext.getVersion();
        GoHighLevelCreateRecordRequest ghlRequest = GoHighLevelCreateRecordRequest.fromRequest(request);
        return recordsClient.createRecord(accessKey, version, schemaKey, ghlRequest);
    }
}
