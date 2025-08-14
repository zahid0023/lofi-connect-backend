package org.example.loficonnect.repository;

import org.example.loficonnect.model.entity.GoHighLevelTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GoHighLevelTokenRepository extends JpaRepository<GoHighLevelTokenEntity, Long> {
}
