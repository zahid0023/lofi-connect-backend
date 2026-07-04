package org.example.loficonnect.subscription.controller;

import jakarta.validation.Valid;
import org.example.loficonnect.auth.model.dto.CustomUserDetails;
import org.example.loficonnect.subscription.dto.request.tenantsubscription.ReviewRefundRequest;
import org.example.loficonnect.subscription.dto.request.tenantsubscription.SubmitRefundRequest;
import org.example.loficonnect.subscription.service.RefundRequestService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

/**
 * Refund request lifecycle endpoints.
 *
 * <p>User flow:
 * <ol>
 *   <li>User submits refund request: {@code POST /api/v1/subscriptions/refund-requests}</li>
 *   <li>User views their requests: {@code GET /api/v1/subscriptions/refund-requests/me}</li>
 * </ol>
 *
 * <p>Admin flow:
 * <ol>
 *   <li>Admin lists pending requests: {@code GET /api/v1/admin/refund-requests}</li>
 *   <li>Admin approves: {@code POST /api/v1/admin/refund-requests/{id}/approve}</li>
 *   <li>Admin rejects:  {@code POST /api/v1/admin/refund-requests/{id}/reject}</li>
 *   <li>Admin then manually processes the approved refund in the Paddle dashboard.</li>
 * </ol>
 */
@RestController
public class RefundRequestController {

    private final RefundRequestService refundRequestService;

    public RefundRequestController(RefundRequestService refundRequestService) {
        this.refundRequestService = refundRequestService;
    }

    // ─── User endpoints ───────────────────────────────────────────────────────

    @PostMapping("/api/v1/subscriptions/refund-requests")
    public ResponseEntity<?> submit(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @Valid @RequestBody SubmitRefundRequest request) {
        return ResponseEntity.ok(refundRequestService.submit(userDetails.getId(), request));
    }

    @GetMapping("/api/v1/subscriptions/refund-requests/me")
    public ResponseEntity<?> getMyRequests(
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        return ResponseEntity.ok(refundRequestService.getMyRequests(userDetails.getId()));
    }

    // ─── Admin endpoints ──────────────────────────────────────────────────────

    @GetMapping("/api/v1/admin/refund-requests")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getAllPending() {
        return ResponseEntity.ok(refundRequestService.getAllPending());
    }

    @PostMapping("/api/v1/admin/refund-requests/{id}/approve")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> approve(
            @PathVariable Long id,
            @AuthenticationPrincipal CustomUserDetails adminDetails,
            @Valid @RequestBody(required = false) ReviewRefundRequest review) {
        return ResponseEntity.ok(
                refundRequestService.approve(id, adminDetails.getId(),
                        review != null ? review : new ReviewRefundRequest()));
    }

    @PostMapping("/api/v1/admin/refund-requests/{id}/reject")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> reject(
            @PathVariable Long id,
            @AuthenticationPrincipal CustomUserDetails adminDetails,
            @Valid @RequestBody(required = false) ReviewRefundRequest review) {
        return ResponseEntity.ok(
                refundRequestService.reject(id, adminDetails.getId(),
                        review != null ? review : new ReviewRefundRequest()));
    }
}
