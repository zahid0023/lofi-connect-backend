package org.example.loficonnect.auth.listener;

import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.example.loficonnect.auth.event.PasswordResetOtpEvent;
import org.example.loficonnect.auth.service.EmailService;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;

@Component
@RequiredArgsConstructor
public class PasswordResetOtpEmailListener {

    private final EmailService emailService;

    @EventListener
    public void handle(PasswordResetOtpEvent event) throws MessagingException, UnsupportedEncodingException {
        emailService.sendOtpEmail(event.getEmail(), event.getOtp());
    }
}
