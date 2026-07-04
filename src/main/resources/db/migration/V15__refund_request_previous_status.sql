-- V15: Add previous_status to refund_requests
-- Stores the subscription status before the refund was submitted,
-- so rejection can restore the correct status instead of always setting CANCELLED.

ALTER TABLE refund_requests
    ADD COLUMN IF NOT EXISTS previous_status VARCHAR(30);
