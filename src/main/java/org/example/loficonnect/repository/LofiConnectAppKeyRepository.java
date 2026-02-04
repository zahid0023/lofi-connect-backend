package org.example.loficonnect.repository;

import org.example.loficonnect.model.entity.LofiConnectAppKeyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LofiConnectAppKeyRepository extends JpaRepository<LofiConnectAppKeyEntity, Long> {

    // Find by app_key value
    Optional<LofiConnectAppKeyEntity> findByAppKeyAndIsActive(String appKey, Boolean isActive);

    @Query("""
                SELECT DISTINCT a
                FROM LofiConnectAppKeyEntity a
                WHERE a.subAccountId = :locationId
                  AND a.isActive = true
            """)
    List<LofiConnectAppKeyEntity> findAllActiveForLocationId(@Param("locationId") String locationId);
}
