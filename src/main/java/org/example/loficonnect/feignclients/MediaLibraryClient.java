package org.example.loficonnect.feignclients;

import com.fasterxml.jackson.databind.JsonNode;
import org.example.loficonnect.config.FeignLoggingConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@FeignClient(name = "mediaClient", url = "https://services.leadconnectorhq.com", configuration = FeignLoggingConfig.class)
public interface MediaLibraryClient {

    @GetMapping(
        value = "/medias/files",
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    JsonNode getFiles(
        @RequestHeader("Authorization") String authorization,
        @RequestHeader("Version") String version,
        @RequestParam Map<String, Object> queryParams
    );

    @PostMapping(
            value = "/medias/upload-file",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    JsonNode uploadFile(
            @RequestHeader("Authorization") String authorization,
            @RequestHeader("Version") String version,
            @RequestPart("file") MultipartFile file,
            @RequestPart("hosted") Boolean hosted,
            @RequestPart("fileUrl") String fileUrl,
            @RequestPart("name") String name,
            @RequestPart("parentId") String parentId
    );

    @DeleteMapping(
            value = "/medias/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    JsonNode deleteFileOrFolder(
            @RequestHeader("Authorization") String authorization,
            @RequestHeader("Version") String version,
            @PathVariable("id") String id,
            @RequestParam("altId") String altId,
            @RequestParam("altType") String altType
    );
}
