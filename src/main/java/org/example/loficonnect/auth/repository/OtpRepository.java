package org.example.loficonnect.auth.repository;

import org.example.loficonnect.auth.model.enitty.PasswordResetOtpEntity;
import org.example.loficonnect.auth.model.enitty.UserEntity;
import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OtpRepository extends JpaRepository<@NonNull PasswordResetOtpEntity, @NonNull Long> {
    Optional<PasswordResetOtpEntity> findByUserEntityAndIsUsedFalse(UserEntity user);

    Optional<PasswordResetOtpEntity> findByOtp(String otp);
}
