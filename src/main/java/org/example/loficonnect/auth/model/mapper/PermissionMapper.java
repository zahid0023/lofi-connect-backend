package org.example.loficonnect.auth.model.mapper;

import lombok.experimental.UtilityClass;
import org.example.loficonnect.auth.dto.request.permission.CreatePermissionRequest;
import org.example.loficonnect.auth.model.enitty.PermissionEntity;

@UtilityClass
public class PermissionMapper {
    public static PermissionEntity fromRequest(CreatePermissionRequest request) {
        PermissionEntity permissionEntity = new PermissionEntity();
        permissionEntity.setName(request.getName());
        permissionEntity.setDescription(request.getDescription());
        return permissionEntity;
    }
}
