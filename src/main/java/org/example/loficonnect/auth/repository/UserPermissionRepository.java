package org.example.loficonnect.auth.repository;

import org.example.loficonnect.auth.model.enitty.PermissionEntity;
import org.example.loficonnect.auth.model.enitty.UserEntity;
import org.example.loficonnect.auth.model.enitty.UserPermissionEntity;
import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserPermissionRepository extends JpaRepository<@NonNull UserPermissionEntity, @NonNull Long> {
    boolean existsByUserAndPermission(
            UserEntity userEntity,
            PermissionEntity permissionEntity
    );
}
