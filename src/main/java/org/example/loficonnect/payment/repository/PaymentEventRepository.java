package org.example.loficonnect.payment.repository;

import org.example.loficonnect.payment.model.entity.PaymentEventEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentEventRepository extends JpaRepository<PaymentEventEntity, Long> {

    boolean existsByEventId(String eventId);
}
