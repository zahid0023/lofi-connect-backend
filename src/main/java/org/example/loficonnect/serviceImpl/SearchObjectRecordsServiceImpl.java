package org.example.loficonnect.serviceImpl;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import org.example.loficonnect.dto.mapper.searchobjectrecords.GoHighLevelSearchObjectRecordsRequest;
import org.example.loficonnect.dto.request.searchobjectrecords.SearchObjectRecordsRequest;
import org.example.loficonnect.feignclients.SearchObjectRecordsClient;
import org.example.loficonnect.service.AuthorizationService;
import org.example.loficonnect.service.SearchObjectRecordsService;

import org.example.loficonnect.util.AppKeyContext;
import org.example.loficonnect.util.VersionContext;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class SearchObjectRecordsServiceImpl implements SearchObjectRecordsService {

    private final SearchObjectRecordsClient searchObjectRecordsClient;
    private final AuthorizationService authorizationService;

    public SearchObjectRecordsServiceImpl(SearchObjectRecordsClient searchObjectRecordsClient, AuthorizationService authorizationService) {
        this.searchObjectRecordsClient = searchObjectRecordsClient;
        this.authorizationService = authorizationService;
    }

    @Override
    public JsonNode searchObjectRecords(String schemaKey, SearchObjectRecordsRequest request) {
        String accessKey = authorizationService.getAccessToken(AppKeyContext.getAppKey());
        String version = VersionContext.getVersion();
        GoHighLevelSearchObjectRecordsRequest ghlRequest = GoHighLevelSearchObjectRecordsRequest.fromRequest(request);
        return searchObjectRecordsClient.searchObjectRecords(accessKey, version, schemaKey, ghlRequest);
    }
}
