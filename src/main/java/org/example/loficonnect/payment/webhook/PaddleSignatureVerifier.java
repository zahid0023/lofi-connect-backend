package org.example.loficonnect.payment.webhook;

import lombok.extern.slf4j.Slf4j;
import org.example.loficonnect.payment.config.PaddleProperties;
import org.example.loficonnect.payment.exception.WebhookVerificationException;
import org.springframework.stereotype.Component;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;

/**
 * Verifies the HMAC-SHA256 signature on incoming Paddle webhook requests.
 *
 * <p>Paddle sets the {@code Paddle-Signature} header in the format:
 * {@code ts=<timestamp>;h1=<hex_hmac>}
 *
 * <p>The signed payload is {@code <timestamp>:<raw_request_body>}.
 */
@Slf4j
@Component
public class PaddleSignatureVerifier {

    private static final String HMAC_ALGORITHM = "HmacSHA256";
    private static final int MAX_TIMESTAMP_DRIFT_SECONDS = 300;

    private final PaddleProperties paddleProperties;

    public PaddleSignatureVerifier(PaddleProperties paddleProperties) {
        this.paddleProperties = paddleProperties;
    }

    /**
     * Verifies the Paddle webhook signature.
     *
     * @param signatureHeader the value of the {@code Paddle-Signature} HTTP header
     * @param rawBody         the raw (unmodified) request body bytes
     * @throws WebhookVerificationException if the signature is invalid or the timestamp is too old
     */
    public void verify(String signatureHeader, byte[] rawBody) {
        if (signatureHeader == null || signatureHeader.isBlank()) {
            throw new WebhookVerificationException("Missing Paddle-Signature header");
        }

        String timestamp = extractPart(signatureHeader, "ts");
        String receivedHmac = extractPart(signatureHeader, "h1");

        validateTimestamp(timestamp);

        String signedPayload = timestamp + ":" + new String(rawBody, StandardCharsets.UTF_8);
        String expectedHmac = computeHmac(signedPayload);

        if (!MessageDigest.isEqual(
                HexFormat.of().parseHex(receivedHmac),
                HexFormat.of().parseHex(expectedHmac))) {
            throw new WebhookVerificationException("Paddle webhook signature mismatch");
        }
    }

    private String extractPart(String header, String key) {
        for (String part : header.split(";")) {
            if (part.startsWith(key + "=")) {
                return part.substring(key.length() + 1);
            }
        }
        throw new WebhookVerificationException(
                "Paddle-Signature header missing field: " + key);
    }

    private void validateTimestamp(String ts) {
        try {
            long eventTime = Long.parseLong(ts);
            long now = System.currentTimeMillis() / 1000;
            if (Math.abs(now - eventTime) > MAX_TIMESTAMP_DRIFT_SECONDS) {
                throw new WebhookVerificationException(
                        "Paddle webhook timestamp is too old (possible replay attack)");
            }
        } catch (NumberFormatException e) {
            throw new WebhookVerificationException("Invalid timestamp in Paddle-Signature header");
        }
    }

    private String computeHmac(String data) {
        try {
            Mac mac = Mac.getInstance(HMAC_ALGORITHM);
            mac.init(new SecretKeySpec(
                    paddleProperties.getWebhookSecret().getBytes(StandardCharsets.UTF_8),
                    HMAC_ALGORITHM));
            byte[] hash = mac.doFinal(data.getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(hash);
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            throw new WebhookVerificationException("HMAC computation failed: " + e.getMessage());
        }
    }
}
