package org.example.loficonnect.subscription.service;

import org.example.loficonnect.subscription.dto.request.tenantsubscription.ReviewRefundRequest;
import org.example.loficonnect.subscription.dto.request.tenantsubscription.SubmitRefundRequest;
import org.example.loficonnect.subscription.dto.response.RefundRequestResponse;

import java.util.List;

public interface RefundRequestService {

    /** User submits a refund request for their active subscription. */
    RefundRequestResponse submit(Long userId, SubmitRefundRequest request);

    /** Returns all refund requests submitted by a user. */
    List<RefundRequestResponse> getMyRequests(Long userId);

    /** Admin: approve a pending refund request. */
    RefundRequestResponse approve(Long requestId, Long adminId, ReviewRefundRequest review);

    /** Admin: reject a pending refund request. */
    RefundRequestResponse reject(Long requestId, Long adminId, ReviewRefundRequest review);

    /** Admin: list all refund requests (optionally filtered by PENDING). */
    List<RefundRequestResponse> getAllPending();
}
