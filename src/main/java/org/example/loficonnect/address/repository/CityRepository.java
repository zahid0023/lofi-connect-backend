package org.example.loficonnect.address.repository;

import org.example.loficonnect.address.model.entity.CityEntity;
import org.example.loficonnect.address.model.projection.CitySummary;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CityRepository extends JpaRepository<@NonNull CityEntity, @NonNull Long> {
    Optional<CityEntity> findByCountryEntity_IdAndIdAndIsActiveAndIsDeleted(Long countryId, Long id, Boolean isActive, Boolean isDeleted);

    Page<@NonNull CitySummary> findAllByCountryEntity_IdAndIsActiveAndIsDeleted(Long countryId, Boolean isActive, Boolean isDeleted, Pageable pageable);
}
