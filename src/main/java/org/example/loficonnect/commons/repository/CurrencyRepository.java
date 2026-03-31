package org.example.loficonnect.commons.repository;

import org.example.loficonnect.commons.model.entity.CurrencyEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CurrencyRepository extends JpaRepository<CurrencyEntity, Long> {
    Optional<CurrencyEntity> findByIdAndIsActiveAndIsDeleted(Long id, Boolean isActive, Boolean isDeleted);

    List<CurrencyEntity> findAllByIsActiveAndIsDeleted(Boolean isActive, Boolean isDeleted);
}
