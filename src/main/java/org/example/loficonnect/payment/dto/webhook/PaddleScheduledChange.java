package org.example.loficonnect.payment.dto.webhook;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.time.Instant;

/**
 * A change scheduled to take effect on the next billing period (e.g. cancellation, pause).
 * Present on subscription.updated events when a future action is pending.
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class PaddleScheduledChange {

    /** The action scheduled: "cancel", "pause", or "resume". */
    private String action;

    /** When the scheduled change takes effect. */
    private Instant effectiveAt;

    /** For paused subscriptions: when they will resume. */
    private Instant resumeAt;
}
