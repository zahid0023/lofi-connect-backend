package org.example.loficonnect.subscription.repository;

import org.example.loficonnect.subscription.model.entity.LimitKeyEntity;
import org.example.loficonnect.subscription.model.projection.LimitKeySummary;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LimitKeyRepository extends JpaRepository<LimitKeyEntity, Long> {

    Optional<LimitKeyEntity> findByIdAndIsActiveAndIsDeleted(Long id, Boolean isActive, Boolean isDeleted);

    Page<@NonNull LimitKeySummary> findAllByIsActiveAndIsDeleted(Boolean isActive, Boolean isDeleted, Pageable pageable);
}
