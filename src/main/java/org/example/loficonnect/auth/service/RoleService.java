package org.example.loficonnect.auth.service;

import org.example.loficonnect.auth.dto.request.role.CreateRoleRequest;
import org.example.loficonnect.auth.model.enitty.RoleEntity;
import org.example.loficonnect.commons.dto.response.SuccessResponse;

public interface RoleService {
    SuccessResponse createRole(CreateRoleRequest request);

    RoleEntity getRoleEntity(String name);
}
