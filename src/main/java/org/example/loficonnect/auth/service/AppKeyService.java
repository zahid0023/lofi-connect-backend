package org.example.loficonnect.auth.service;

import org.example.loficonnect.auth.dto.request.appkey.CreateAppKeyRequest;
import org.example.loficonnect.auth.dto.response.appkey.AppKeyListResponse;
import org.example.loficonnect.auth.dto.response.appkey.GenerateAppKeyResponse;
import org.example.loficonnect.auth.model.enitty.LofiConnectAppKeyEntity;

public interface AppKeyService {
    GenerateAppKeyResponse generateAppKey(CreateAppKeyRequest request);

    AppKeyListResponse getAllAppKeys(Long userId);

    LofiConnectAppKeyEntity getAppKeyEntity(String appKey);

    LofiConnectAppKeyEntity getAppKeyEntityById(Long id);
}
