package org.example.loficonnect.serviceImpl;

import lombok.extern.slf4j.Slf4j;
import org.example.loficonnect.service.SecretKeyService;
import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

@Service
@Slf4j
public class SecretKeyServiceImpl implements SecretKeyService {
    @Override
    public String generateSecretKey() {
        try {
            byte[] randomBytes = new byte[32];
            SecureRandom secureRandom = new SecureRandom();
            secureRandom.nextBytes(randomBytes);

            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(randomBytes);

            return Base64.getUrlEncoder().withoutPadding().encodeToString(hash);
        } catch (NoSuchAlgorithmException ex) {
            throw new RuntimeException("Error generating secret key", ex);
        }
    }

}
