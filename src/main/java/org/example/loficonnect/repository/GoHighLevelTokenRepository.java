package org.example.loficonnect.repository;

import jakarta.validation.constraints.NotNull;
import org.example.loficonnect.model.entity.GoHighLevelTokenEntity;
import org.example.loficonnect.auth.model.enitty.LofiConnectAppKeyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GoHighLevelTokenRepository extends JpaRepository<GoHighLevelTokenEntity, Long> {
    Optional<GoHighLevelTokenEntity> findByAppKeyEntityAndIsActiveAndIsDeleted(@NotNull LofiConnectAppKeyEntity lofiConnectAppKeyEntity, Boolean isActive, Boolean isDeleted);
}
