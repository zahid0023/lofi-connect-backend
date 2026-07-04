package org.example.loficonnect.repository;

import org.example.loficonnect.auth.model.enitty.LofiConnectAppKeyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LofiConnectAppKeyRepository extends JpaRepository<LofiConnectAppKeyEntity, Long> {

    // Find by app_key value
    Optional<LofiConnectAppKeyEntity> findByAppKeyAndIsActiveAndIsDeleted(String appKey, Boolean isActive, Boolean isDeleted);

    // Eagerly fetch tenantSubscription to avoid LazyInitializationException in interceptors
    @Query("SELECT k FROM LofiConnectAppKeyEntity k LEFT JOIN FETCH k.tenantSubscription WHERE k.appKey = :appKey AND k.isActive = true AND k.isDeleted = false")
    Optional<LofiConnectAppKeyEntity> findByAppKeyWithSubscription(@Param("appKey") String appKey);

    List<LofiConnectAppKeyEntity> findByCreatedByAndIsActiveAndIsDeleted(Long createdBy, Boolean isActive, Boolean isDeleted);

    Optional<LofiConnectAppKeyEntity> findByIdAndIsActiveAndIsDeleted(Long id, Boolean isActive, Boolean isDeleted);

    long countByCreatedByAndIsActiveAndIsDeleted(Long createdBy, Boolean isActive, Boolean isDeleted);
}
