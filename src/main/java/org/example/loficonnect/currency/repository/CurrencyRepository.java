package org.example.loficonnect.currency.repository;

import org.example.loficonnect.currency.model.entity.CurrencyEntity;
import org.example.loficonnect.currency.model.projection.CurrencySummary;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CurrencyRepository extends JpaRepository<CurrencyEntity, Long> {
    Optional<CurrencyEntity> findByIdAndIsActiveAndIsDeleted(Long id, Boolean isActive, Boolean isDeleted);

    Page<@NonNull CurrencySummary> findAllByIsActiveAndIsDeleted(Boolean isActive, Boolean isDeleted, Pageable pageable);
}
