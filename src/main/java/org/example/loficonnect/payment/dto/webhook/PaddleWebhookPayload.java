package org.example.loficonnect.payment.dto.webhook;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.time.Instant;

/**
 * Generic Paddle webhook envelope.
 * The {@code data} field is kept as a raw {@link JsonNode} so each handler can
 * deserialize it into the appropriate event-specific DTO.
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class PaddleWebhookPayload {

    /** Unique event ID — used for idempotency. */
    private String eventId;

    /**
     * Paddle event type string.
     * e.g. "subscription.created", "subscription.activated",
     *      "subscription.cancelled", "subscription.past_due",
     *      "transaction.completed"
     */
    private String eventType;

    /** When the event occurred at Paddle. */
    private Instant occurredAt;

    /** Raw event-specific data; parsed per-handler. */
    private JsonNode data;
}
