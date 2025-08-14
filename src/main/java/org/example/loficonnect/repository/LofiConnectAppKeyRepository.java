package org.example.loficonnect.repository;

import org.example.loficonnect.model.entity.LofiConnectAppKeyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LofiConnectAppKeyRepository extends JpaRepository<LofiConnectAppKeyEntity, Long> {

    // Find by app_key value
    Optional<LofiConnectAppKeyEntity> findByAppKey(String appKey);
}
