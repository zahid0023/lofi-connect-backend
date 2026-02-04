package org.example.loficonnect.auth.service;

import org.example.loficonnect.auth.dto.request.scope.CreateScopeRequest;
import org.example.loficonnect.commons.dto.response.SuccessResponse;
import org.example.loficonnect.model.entity.ScopeEntity;

import java.util.List;

public interface ScopeService {
    SuccessResponse createScope(CreateScopeRequest request);

    List<ScopeEntity> getAllScopes();

    List<String> getAllScopesNames();
}
