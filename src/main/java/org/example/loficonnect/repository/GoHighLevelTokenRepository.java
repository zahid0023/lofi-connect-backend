package org.example.loficonnect.repository;

import org.example.loficonnect.model.entity.GoHighLevelTokenEntity;
import org.example.loficonnect.model.entity.LofiConnectAppKeyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GoHighLevelTokenRepository extends JpaRepository<GoHighLevelTokenEntity, Long> {
    Optional<GoHighLevelTokenEntity> findFirstByAppKeyEntity(LofiConnectAppKeyEntity appKeyEntity);
}
