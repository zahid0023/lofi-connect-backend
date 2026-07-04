package org.example.loficonnect.payment.repository;

import org.example.loficonnect.payment.model.entity.CheckoutIntentEntity;
import org.example.loficonnect.payment.model.enums.CheckoutIntentStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface CheckoutIntentRepository extends JpaRepository<CheckoutIntentEntity, Long> {

    /** All PENDING intents that have passed their expiry time — used by lifecycle scheduler. */
    List<CheckoutIntentEntity> findByStatusAndExpiresAtBefore(CheckoutIntentStatus status, Instant cutoff);

    /** PENDING intents older than 24h with no reminder sent yet — used for reminder emails. */
    List<CheckoutIntentEntity> findByStatusAndReminderSentAtIsNullAndCreatedAtBefore(
            CheckoutIntentStatus status, Instant cutoff);

    /** Look up a PENDING intent by Paddle transaction ID when a webhook arrives. */
    Optional<CheckoutIntentEntity> findByPaddleTransactionIdAndStatus(
            String paddleTransactionId, CheckoutIntentStatus status);
}
