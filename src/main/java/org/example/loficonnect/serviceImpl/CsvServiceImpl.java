package org.example.loficonnect.serviceImpl;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import org.example.loficonnect.dto.mapper.csv.GoHighLevelCsvFinalizeRequest;
import org.example.loficonnect.dto.mapper.csv.GoHighLevelCsvSetAccountsRequest;
import org.example.loficonnect.dto.mapper.csv.GoHighLevelCsvUploadRequest;
import org.example.loficonnect.dto.request.csv.CsvFinalizeRequest;
import org.example.loficonnect.dto.request.csv.CsvSetAccountsRequest;
import org.example.loficonnect.dto.request.csv.CsvUploadRequest;
import org.example.loficonnect.feignclients.CsvClient;
import org.example.loficonnect.service.AuthorizationService;
import org.example.loficonnect.service.CsvService;
import org.example.loficonnect.util.AppKeyContext;
import org.example.loficonnect.util.VersionContext;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@Slf4j
public class CsvServiceImpl implements CsvService {

    private final CsvClient csvClient;
    private final AuthorizationService authorizationService;

    public CsvServiceImpl(CsvClient csvClient, AuthorizationService authorizationService) {
        this.csvClient = csvClient;
        this.authorizationService = authorizationService;
    }

    @Override
    public JsonNode uploadCsv(String locationId, CsvUploadRequest request) {
        String accessKey = authorizationService.getAccessToken(AppKeyContext.getAppKey());
        String version = VersionContext.getVersion();
        GoHighLevelCsvUploadRequest ghlRequest = GoHighLevelCsvUploadRequest.fromRequest(request);
        return csvClient.uploadCsv(accessKey, version, locationId, ghlRequest.getFile());
    }

    @Override
    public JsonNode getCsvUploadStatus(String locationId, Map<String, Object> queryParams) {
        String accessKey = authorizationService.getAccessToken(AppKeyContext.getAppKey());
        String version = VersionContext.getVersion();
        return csvClient.getCsvUploadStatus(accessKey, version, locationId, queryParams);
    }

    @Override
    public JsonNode setAccounts(String locationId, CsvSetAccountsRequest request) {
        String accessKey = authorizationService.getAccessToken(AppKeyContext.getAppKey());
        String version = VersionContext.getVersion();
        GoHighLevelCsvSetAccountsRequest ghlRequest = GoHighLevelCsvSetAccountsRequest.fromRequest(request);
        return csvClient.setAccounts(accessKey, version, locationId, ghlRequest);
    }

    @Override
    public JsonNode getCsvPost(String locationId, String csvId, Map<String, Object> queryParams) {
        String accessKey = authorizationService.getAccessToken(AppKeyContext.getAppKey());
        String version = VersionContext.getVersion();
        return csvClient.getCsvPost(accessKey, version, locationId, csvId, queryParams);
    }

    @Override
    public JsonNode finalizeCsv(String locationId, String csvId, CsvFinalizeRequest request) {
        String accessKey = authorizationService.getAccessToken(AppKeyContext.getAppKey());
        String version = VersionContext.getVersion();
        GoHighLevelCsvFinalizeRequest ghlRequest = GoHighLevelCsvFinalizeRequest.fromRequest(request);
        return csvClient.finalizeCsv(accessKey, version, locationId, csvId, ghlRequest);
    }

    @Override
    public JsonNode deleteCsv(String locationId, String csvId) {
        String accessKey = authorizationService.getAccessToken(AppKeyContext.getAppKey());
        String version = VersionContext.getVersion();
        return csvClient.deleteCsv(accessKey, version, locationId, csvId);
    }

    @Override
    public JsonNode deleteCsvPost(String locationId, String csvId, String postId) {
        String accessKey = authorizationService.getAccessToken(AppKeyContext.getAppKey());
        String version = VersionContext.getVersion();
        return csvClient.deleteCsvPost(accessKey, version, locationId, csvId, postId);
    }

}
