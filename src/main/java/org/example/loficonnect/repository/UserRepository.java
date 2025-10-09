package org.example.loficonnect.repository;

import jakarta.validation.constraints.NotNull;
import org.example.loficonnect.model.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByUsername(@NotNull String username);

    boolean existsByUsername(@NotNull String username);
}
