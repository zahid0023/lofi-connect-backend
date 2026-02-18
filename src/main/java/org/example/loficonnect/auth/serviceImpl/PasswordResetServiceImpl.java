package org.example.loficonnect.auth.serviceImpl;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.example.loficonnect.auth.dto.request.ResetPasswordRequest;
import org.example.loficonnect.auth.dto.response.VerifyOtpResponse;
import org.example.loficonnect.auth.event.PasswordResetOtpEvent;
import org.example.loficonnect.auth.model.enitty.PasswordResetOtpEntity;
import org.example.loficonnect.auth.model.enitty.UserEntity;
import org.example.loficonnect.auth.model.mapper.OtpMapper;
import org.example.loficonnect.auth.repository.OtpRepository;
import org.example.loficonnect.auth.service.PasswordResetService;
import org.example.loficonnect.auth.util.OtpGenerator;
import org.example.loficonnect.commons.dto.response.SuccessResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class PasswordResetServiceImpl implements PasswordResetService {
    private final OtpRepository otpRepository;
    private final ApplicationEventPublisher applicationEventPublisher;

    @Value("${jwt.otp.expiration-minutes}")
    private Integer otpExpiryTimeMinutes;

    public PasswordResetServiceImpl(OtpRepository otpRepository,
                                    ApplicationEventPublisher applicationEventPublisher) {
        this.otpRepository = otpRepository;
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @Override
    @Transactional
    public SuccessResponse forgotPassword(UserEntity userEntity) {
        String otp = generateOtpEntity(userEntity);
        log.info("OTP generated for user: {}", userEntity.getUsername());
        applicationEventPublisher.publishEvent(new PasswordResetOtpEvent(userEntity.getUsername(), otp));
        return new SuccessResponse(true, 0L);
    }

    private String generateOtpEntity(UserEntity userEntity) {
        PasswordResetOtpEntity passwordResetOtpEntity = OtpMapper.toOtpEntity(userEntity, OtpGenerator.generate6DigitOtp(), otpExpiryTimeMinutes);
        passwordResetOtpEntity = otpRepository.save(passwordResetOtpEntity);
        return passwordResetOtpEntity.getOtp();
    }

    @Override
    @Transactional
    public VerifyOtpResponse verifyOtpAndGetResetToken(PasswordResetOtpEntity passwordResetOtpEntity) {
        return null;
    }

    @Override
    @Transactional
    public SuccessResponse resetPassword(ResetPasswordRequest request) {
        return null;
    }

}

