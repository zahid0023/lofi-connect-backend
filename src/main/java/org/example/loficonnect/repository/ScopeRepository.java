package org.example.loficonnect.repository;

import org.example.loficonnect.model.entity.ScopeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ScopeRepository extends JpaRepository<ScopeEntity, Long> {
    @Query(value = "select name from scopes", nativeQuery = true)
    List<String> findAllNames();
}
