package org.example.loficonnect.auth.model.mapper;

import lombok.experimental.UtilityClass;
import org.example.loficonnect.auth.dto.request.scope.CreateScopeRequest;
import org.example.loficonnect.model.entity.ScopeEntity;

@UtilityClass
public class ScopeMapper {
    public static ScopeEntity fromRequest(CreateScopeRequest request) {
        ScopeEntity entity = new ScopeEntity();
        entity.setName(request.getName());
        entity.setDescription(request.getDescription());
        return entity;
    }
}
