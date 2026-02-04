package org.example.loficonnect.auth.model.mapper;

import lombok.experimental.UtilityClass;
import org.example.loficonnect.auth.dto.request.role.CreateRoleRequest;
import org.example.loficonnect.auth.model.enitty.RoleEntity;

@UtilityClass
public class RoleMapper {
    public static RoleEntity fromRequest(CreateRoleRequest request) {
        RoleEntity roleEntity = new RoleEntity();
        roleEntity.setName(request.getName());
        return roleEntity;
    }
}
