package org.example.loficonnect.auth.model.mapper;

import lombok.experimental.UtilityClass;
import org.example.loficonnect.auth.model.enitty.PasswordResetOtpEntity;
import org.example.loficonnect.auth.model.enitty.UserEntity;

import java.time.OffsetDateTime;
import java.util.UUID;

@UtilityClass
public class OtpMapper {
    public static PasswordResetOtpEntity toOtpEntity(UserEntity userEntity, String otp, Integer otpExpiryInMinutes) {
        PasswordResetOtpEntity passwordResetOtpEntity = new PasswordResetOtpEntity();
        passwordResetOtpEntity.setUserEntity(userEntity);
        passwordResetOtpEntity.setOtp(otp);
        passwordResetOtpEntity.setResetToken(generateResetToken());
        passwordResetOtpEntity.setIsUsed(false);
        passwordResetOtpEntity.setExpiresAt(OffsetDateTime.now().plusMinutes(otpExpiryInMinutes));
        return passwordResetOtpEntity;
    }

    private String generateResetToken() {
        return UUID.randomUUID().toString();
    }
}
