package org.example.loficonnect.controller;

import org.example.loficonnect.config.AppKey;
import org.example.loficonnect.dto.request.medialibrary.FileUploadRequest;
import org.example.loficonnect.service.MediaLibraryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/ghl")
public class MediaLibraryController {

    private final MediaLibraryService mediaLibraryService;

    public MediaLibraryController(MediaLibraryService mediaLibraryService) {
        this.mediaLibraryService = mediaLibraryService;
    }

    @AppKey
    @GetMapping("/medias/files")
    public ResponseEntity<?> getFiles(
        @RequestParam(value = "limit", required = false) String limit,
        @RequestParam(value = "offset", required = false) String offset,
        @RequestParam(value = "query", required = false) String query,
        @RequestParam(value = "type", required = false) String type,
        @RequestParam(value = "alt-id") String altId,
        @RequestParam(value = "alt-type") String altType,
        @RequestParam(value = "sort-by") String sortBy,
        @RequestParam(value = "sort-order") String sortOrder,
        @RequestParam(value = "parent-id", required = false) String parentId
    ) {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("limit", limit);
        queryParams.put("offset", offset);
        queryParams.put("query", query);
        queryParams.put("type", type);
        queryParams.put("alt-id", altId);
        queryParams.put("alt-type", altType);
        queryParams.put("sort-by", sortBy);
        queryParams.put("sort-order", sortOrder);
        queryParams.put("parent-id", parentId);

        return ResponseEntity.ok(mediaLibraryService.getFiles(queryParams));
    }

    @AppKey
    @PostMapping("/medias/upload-file")
    public ResponseEntity<?> uploadFile(
            @RequestPart("file") MultipartFile file,
            @RequestPart("hosted") Boolean hosted,
            @RequestPart("fileUrl") String fileUrl,
            @RequestPart("name") String name,
            @RequestPart("parent-id") String parentId
    ) {
        FileUploadRequest request = new FileUploadRequest();
        request.setFile(file);
        request.setHosted(hosted);
        request.setFileUrl(fileUrl);
        request.setName(name);
        request.setParentId(parentId);

        return ResponseEntity.status(HttpStatus.CREATED).body(mediaLibraryService.uploadFile(request));
    }

    @AppKey
    @DeleteMapping("/medias/{id}")
    public ResponseEntity<?> deleteFileOrFolder(
            @PathVariable("id") String id,
            @RequestParam("alt-id") String altId,
            @RequestParam("alt-type") String altType
    ) {
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(mediaLibraryService.deleteFileOrFolder(id, altId, altType));
    }

}
