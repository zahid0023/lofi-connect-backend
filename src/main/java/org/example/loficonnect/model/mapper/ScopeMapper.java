package org.example.loficonnect.model.mapper;

import lombok.experimental.UtilityClass;
import org.example.loficonnect.dto.request.scope.CreateScopeRequest;
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
