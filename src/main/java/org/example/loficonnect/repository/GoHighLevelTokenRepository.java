package org.example.loficonnect.repository;

import jakarta.validation.constraints.NotNull;
import org.example.loficonnect.model.entity.GoHighLevelTokenEntity;
import org.example.loficonnect.auth.model.enitty.LofiConnectAppKeyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GoHighLevelTokenRepository extends JpaRepository<GoHighLevelTokenEntity, Long> {

    Optional<GoHighLevelTokenEntity> findByAppKeyEntityAndIsActiveAndIsDeleted(
            @NotNull LofiConnectAppKeyEntity lofiConnectAppKeyEntity,
            Boolean isActive, Boolean isDeleted);

    /**
     * Counts active GHL subaccount connections belonging to a user.
     * Joins through the app key's {@code created_by} column (set by JPA auditing).
     */
    @Query("""
            SELECT COUNT(g) FROM GoHighLevelTokenEntity g
            WHERE g.appKeyEntity.createdBy = :userId
            AND g.isActive = true AND g.isDeleted = false
            AND g.appKeyEntity.isActive = true AND g.appKeyEntity.isDeleted = false
            """)
    long countConnectedByUserId(@Param("userId") Long userId);
}
