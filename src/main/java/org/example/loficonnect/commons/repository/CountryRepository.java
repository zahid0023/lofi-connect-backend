package org.example.loficonnect.commons.repository;

import org.example.loficonnect.commons.model.entity.CountryEntity;
import org.example.loficonnect.commons.model.projection.CountrySummary;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CountryRepository extends JpaRepository<@NonNull CountryEntity, @NonNull Long> {
    Optional<CountryEntity> findByIdAndIsActiveAndIsDeleted(Long id, Boolean isActive, Boolean isDeleted);

    Page<@NonNull CountrySummary> findAllByIsActiveAndIsDeleted(Boolean isActive, Boolean isDeleted, Pageable pageable);
}
