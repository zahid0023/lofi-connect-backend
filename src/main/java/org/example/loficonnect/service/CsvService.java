package org.example.loficonnect.service;

import com.fasterxml.jackson.databind.JsonNode;
import org.example.loficonnect.dto.request.csv.CsvFinalizeRequest;
import org.example.loficonnect.dto.request.csv.CsvSetAccountsRequest;
import org.example.loficonnect.dto.request.csv.CsvUploadRequest;

import java.util.Map;

public interface CsvService {
    JsonNode uploadCsv(String locationId, CsvUploadRequest request);
    JsonNode getCsvUploadStatus(String locationId, Map<String, Object> queryParams);
    JsonNode setAccounts(String locationId, CsvSetAccountsRequest request);
    JsonNode getCsvPost(String locationId, String csvId, Map<String, Object> queryParams);
    JsonNode finalizeCsv(String locationId, String csvId, CsvFinalizeRequest request);
    JsonNode deleteCsv(String locationId, String csvId);
    JsonNode deleteCsvPost(String locationId, String csvId, String postId);
}
