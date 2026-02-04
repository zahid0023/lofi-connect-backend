package org.example.loficonnect.auth.service;

import org.example.loficonnect.auth.dto.request.ResetPasswordRequest;
import org.example.loficonnect.auth.dto.response.VerifyOtpResponse;
import org.example.loficonnect.auth.model.enitty.PasswordResetOtpEntity;
import org.example.loficonnect.auth.model.enitty.UserEntity;
import org.example.loficonnect.commons.dto.response.SuccessResponse;

public interface PasswordResetService {
    SuccessResponse forgotPassword(UserEntity userEntity);

    VerifyOtpResponse verifyOtpAndGetResetToken(PasswordResetOtpEntity passwordResetOtpEntity);

    SuccessResponse resetPassword(ResetPasswordRequest request);
}

