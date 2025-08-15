package org.example.loficonnect;

import org.example.loficonnect.service.SecretKeyService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class SecretKeyServiceImplTest {

    private final SecretKeyService secretKeyService;

    @Autowired
    public SecretKeyServiceImplTest(
            @Qualifier("secretKeyServiceImpl") SecretKeyService secretKeyService
    ) {
        this.secretKeyService = secretKeyService;
    }

    @Test
    void testGenerateSecretKey_NotNullOrEmpty() {
        String secretKey = secretKeyService.generateSecretKey();
        assertNotNull(secretKey, "Secret key should not be null");
        assertFalse(secretKey.isEmpty(), "Secret key should not be empty");
    }

    @Test
    void testGenerateSecretKey_IsRandom() {
        String key1 = secretKeyService.generateSecretKey();
        String key2 = secretKeyService.generateSecretKey();
        assertNotEquals(key1, key2, "Two generated keys should be different");
    }

    @Test
    void testGenerateSecretKey_Length() {
        String secretKey = secretKeyService.generateSecretKey();
        assertTrue(secretKey.length() >= 39, "Secret key should be at least 39 characters long");
    }
}
