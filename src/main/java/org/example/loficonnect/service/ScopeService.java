package org.example.loficonnect.service;

import org.example.loficonnect.dto.request.scope.CreateScopeRequest;
import org.example.loficonnect.commons.dto.response.SuccessResponse;
import org.example.loficonnect.model.entity.ScopeEntity;

import java.util.List;

public interface ScopeService {
    SuccessResponse createScope(CreateScopeRequest request);

    List<ScopeEntity> getAllScopes();

    List<String> getAllScopesNames();
}
