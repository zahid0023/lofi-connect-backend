package org.example.loficonnect.payment.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.example.loficonnect.payment.dto.webhook.PaddleWebhookPayload;
import org.example.loficonnect.payment.webhook.PaddleSignatureVerifier;
import org.example.loficonnect.payment.webhook.PaddleWebhookProcessor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;

/**
 * Receives Paddle webhook events.
 * The endpoint is intentionally unauthenticated — Paddle calls it server-to-server.
 * All trust is established via HMAC-SHA256 signature verification.
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/payments/webhooks")
public class PaddleWebhookController {

    private final PaddleSignatureVerifier signatureVerifier;
    private final PaddleWebhookProcessor webhookProcessor;
    private final ObjectMapper objectMapper;

    public PaddleWebhookController(
            PaddleSignatureVerifier signatureVerifier,
            PaddleWebhookProcessor webhookProcessor,
            ObjectMapper objectMapper) {
        this.signatureVerifier = signatureVerifier;
        this.webhookProcessor = webhookProcessor;
        this.objectMapper = objectMapper;
    }

    /**
     * POST /api/v1/payments/webhooks/paddle
     *
     * <p>We receive the body as a raw String so that we can:
     * <ol>
     *   <li>Verify the HMAC signature against the original bytes.</li>
     *   <li>Persist the raw payload for debugging.</li>
     *   <li>Deserialize into the typed DTO.</li>
     * </ol>
     */
    @PostMapping("/paddle")
    public ResponseEntity<Void> handlePaddleWebhook(
            @RequestHeader("Paddle-Signature") String signature,
            @RequestBody String rawBody) {

        try {
            signatureVerifier.verify(signature, rawBody.getBytes(StandardCharsets.UTF_8));
            PaddleWebhookPayload payload = objectMapper.readValue(rawBody, PaddleWebhookPayload.class);
                webhookProcessor.process(payload, rawBody);
            return ResponseEntity.ok().build();

        } catch (Exception ex) {
            // Always return 200 to Paddle to prevent retries for business-logic rejections.
            // Signature failures are logged but swallowed here; the event is not persisted.
            log.error("Failed to process Paddle webhook: {}", ex.getMessage(), ex);
            return ResponseEntity.ok().build();
        }
    }
}
