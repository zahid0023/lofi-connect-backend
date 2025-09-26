package org.example.loficonnect.serviceImpl;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import org.example.loficonnect.dto.mapper.medialibrary.GoHighLevelFileUploadRequest;
import org.example.loficonnect.dto.request.medialibrary.FileUploadRequest;
import org.example.loficonnect.feignclients.MediaLibraryClient;
import org.example.loficonnect.service.AuthorizationService;
import org.example.loficonnect.service.MediaLibraryService;
import org.example.loficonnect.util.AppKeyContext;
import org.example.loficonnect.util.VersionContext;
import org.springframework.stereotype.Service;
import java.util.Map;

@Service
@Slf4j
public class MediaLibraryServiceImpl implements MediaLibraryService {

    private final MediaLibraryClient mediaLibraryClient;
    private final AuthorizationService authorizationService;

    public MediaLibraryServiceImpl(MediaLibraryClient mediaLibraryClient, AuthorizationService authorizationService) {
        this.mediaLibraryClient = mediaLibraryClient;
        this.authorizationService = authorizationService;
    }

    @Override
    public JsonNode getFiles(Map<String, Object> queryParams) {
        String accessKey = authorizationService.getAccessToken(AppKeyContext.getAppKey());
        String version = VersionContext.getVersion();
        return mediaLibraryClient.getFiles(accessKey, version, queryParams);
    }

    @Override
    public JsonNode uploadFile(FileUploadRequest request) {
        String accessKey = authorizationService.getAccessToken(AppKeyContext.getAppKey());
        String version = VersionContext.getVersion();
        GoHighLevelFileUploadRequest ghlRequest = GoHighLevelFileUploadRequest.fromRequest(request);
        return mediaLibraryClient.uploadFile(accessKey, version, ghlRequest.getFile(), ghlRequest.getHosted(), ghlRequest.getFileUrl(), ghlRequest.getName(), ghlRequest.getParentId());
    }

    @Override
    public JsonNode deleteFileOrFolder(String id, String altId, String altType) {
        String accessKey = authorizationService.getAccessToken(AppKeyContext.getAppKey());
        String version = VersionContext.getVersion();
        return mediaLibraryClient.deleteFileOrFolder(accessKey, version, id, altId, altType);
    }

}
