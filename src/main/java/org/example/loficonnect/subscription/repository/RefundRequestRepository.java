package org.example.loficonnect.subscription.repository;

import org.example.loficonnect.subscription.model.entity.RefundRequestEntity;
import org.example.loficonnect.subscription.model.enums.RefundRequestStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RefundRequestRepository extends JpaRepository<RefundRequestEntity, Long> {

    Optional<RefundRequestEntity> findByTenantSubscriptionIdAndStatus(Long subscriptionId, RefundRequestStatus status);

    List<RefundRequestEntity> findByUserId(Long userId);

    Page<RefundRequestEntity> findByStatus(RefundRequestStatus status, Pageable pageable);
}
