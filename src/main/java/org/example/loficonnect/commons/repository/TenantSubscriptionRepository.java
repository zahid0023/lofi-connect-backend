package org.example.loficonnect.commons.repository;

import org.example.loficonnect.auth.model.enitty.UserEntity;
import org.example.loficonnect.commons.model.entity.TenantSubscriptionEntity;
import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.OffsetDateTime;
import java.util.Optional;

public interface TenantSubscriptionRepository extends JpaRepository<@NonNull TenantSubscriptionEntity, @NonNull Long> {
    Optional<TenantSubscriptionEntity> findFirstByTenantEntityAndStatusAndEndAtAfterAndIsActiveAndIsDeleted(
            UserEntity userEntity, String status, OffsetDateTime now, Boolean isActive, Boolean isDeleted);

    boolean existsByTenantEntityAndStatusAndEndAtAfterAndIsActiveAndIsDeleted(
            UserEntity userEntity, String status, OffsetDateTime now, Boolean isActive, Boolean isDeleted);

    Optional<TenantSubscriptionEntity> findFirstByTenantEntityAndStatusAndEndAtAfterAndIsActiveAndIsDeletedOrderByStartAtDesc(
            UserEntity userEntity, String status, OffsetDateTime now, Boolean isActive, Boolean isDeleted);
}
