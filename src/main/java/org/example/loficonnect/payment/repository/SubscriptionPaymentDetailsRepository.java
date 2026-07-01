package org.example.loficonnect.payment.repository;

import org.example.loficonnect.payment.model.entity.SubscriptionPaymentDetailsEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SubscriptionPaymentDetailsRepository extends JpaRepository<SubscriptionPaymentDetailsEntity, Long> {

    Optional<SubscriptionPaymentDetailsEntity> findByPaddleSubscriptionId(String paddleSubscriptionId);

    Optional<SubscriptionPaymentDetailsEntity> findByTenantSubscriptionId(Long tenantSubscriptionId);

    boolean existsByPaddleSubscriptionId(String paddleSubscriptionId);
}
