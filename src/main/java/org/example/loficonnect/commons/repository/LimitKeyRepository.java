package org.example.loficonnect.commons.repository;

import org.example.loficonnect.commons.model.entity.LimitKeyEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LimitKeyRepository extends JpaRepository<LimitKeyEntity, Long> {
    Optional<LimitKeyEntity> findByIdAndIsActiveAndIsDeleted(Long id, Boolean isActive, Boolean isDeleted);

    List<LimitKeyEntity> findAllByIsActiveAndIsDeleted(Boolean isActive, Boolean isDeleted);
}
