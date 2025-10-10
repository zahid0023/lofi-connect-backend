package org.example.loficonnect.feignclients;

import com.fasterxml.jackson.databind.JsonNode;
import org.example.loficonnect.config.FeignLoggingConfig;
import org.example.loficonnect.dto.mapper.csv.GoHighLevelCsvFinalizeRequest;
import org.example.loficonnect.dto.mapper.csv.GoHighLevelCsvSetAccountsRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@FeignClient(name = "csvClient", url = "https://services.leadconnectorhq.com", configuration = FeignLoggingConfig.class)
public interface CsvClient {

    @PostMapping(
        value = "/social-media-posting/{locationId}/csv",
        consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    JsonNode uploadCsv(
        @RequestHeader("Authorization") String authorization,
        @RequestHeader("Version") String version,
        @PathVariable("locationId") String locationId,
        @RequestPart("file") MultipartFile file
    );

    @GetMapping(
            value = "/social-media-posting/{locationId}/csv",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    JsonNode getCsvUploadStatus(
            @RequestHeader("Authorization") String authorization,
            @RequestHeader("Version") String version,
            @PathVariable("locationId") String locationId,
            @RequestParam Map<String, Object> queryParams
    );

    @PostMapping(
            value = "/social-media-posting/{locationId}/set-accounts",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    JsonNode setAccounts(
            @RequestHeader("Authorization") String authorization,
            @RequestHeader("Version") String version,
            @PathVariable("locationId") String locationId,
            @RequestBody GoHighLevelCsvSetAccountsRequest request
    );

    @GetMapping(
            value = "/social-media-posting/{locationId}/csv/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    JsonNode getCsvPost(
            @RequestHeader("Authorization") String authorization,
            @RequestHeader("Version") String version,
            @PathVariable("locationId") String locationId,
            @PathVariable("id") String csvId,
            @RequestParam Map<String, Object> queryParams
    );

    @PatchMapping(
            value = "/social-media-posting/{locationId}/csv/{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    JsonNode finalizeCsv(
            @RequestHeader("Authorization") String authorization,
            @RequestHeader("Version") String version,
            @PathVariable("locationId") String locationId,
            @PathVariable("id") String csvId,
            @RequestBody GoHighLevelCsvFinalizeRequest request
    );

    @DeleteMapping(
            value = "/social-media-posting/{locationId}/csv/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    JsonNode deleteCsv(
            @RequestHeader("Authorization") String authorization,
            @RequestHeader("Version") String version,
            @PathVariable("locationId") String locationId,
            @PathVariable("id") String csvId
    );

    @DeleteMapping(
            value = "/social-media-posting/{locationId}/csv/{csvId}/post/{postId}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    JsonNode deleteCsvPost(
            @RequestHeader("Authorization") String authorization,
            @RequestHeader("Version") String version,
            @PathVariable("locationId") String locationId,
            @PathVariable("csvId") String csvId,
            @PathVariable("postId") String postId
    );

}
