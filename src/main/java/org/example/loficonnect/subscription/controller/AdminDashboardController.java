package org.example.loficonnect.subscription.controller;

import org.example.loficonnect.auth.model.dto.CustomUserDetails;
import org.example.loficonnect.subscription.service.AdminDashboardService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

/**
 * Admin-only endpoints for the subscription dashboard.
 *
 * <p>All endpoints require the ADMIN role.
 *
 * <ul>
 *   <li>{@code GET  /api/v1/admin/dashboard/stats}                       — aggregate KPIs</li>
 *   <li>{@code GET  /api/v1/admin/dashboard/provisioning-queue}          — bundled plans awaiting GHL setup</li>
 *   <li>{@code POST /api/v1/admin/subscriptions/{id}/provisioning/start} — mark provisioning in progress</li>
 *   <li>{@code POST /api/v1/admin/subscriptions/{id}/provisioning/complete} — mark provisioning done</li>
 * </ul>
 */
@RestController
@RequestMapping("/api/v1/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminDashboardController {

    private final AdminDashboardService adminDashboardService;

    public AdminDashboardController(AdminDashboardService adminDashboardService) {
        this.adminDashboardService = adminDashboardService;
    }

    /** Aggregate KPIs: MRR, active/trial/past-due counts, provisioning queue size, etc. */
    @GetMapping("/dashboard/stats")
    public ResponseEntity<?> getStats() {
        return ResponseEntity.ok(adminDashboardService.getStats());
    }

    /** List of BUNDLED subscriptions with PENDING or IN_PROGRESS provisioning status. */
    @GetMapping("/dashboard/provisioning-queue")
    public ResponseEntity<?> getProvisioningQueue() {
        return ResponseEntity.ok(adminDashboardService.getProvisioningQueue());
    }

    /** Admin marks a bundled subscription as provisioning started (sets IN_PROGRESS). */
    @PostMapping("/subscriptions/{id}/provisioning/start")
    public ResponseEntity<?> startProvisioning(
            @PathVariable Long id,
            @AuthenticationPrincipal CustomUserDetails adminDetails) {
        return ResponseEntity.ok(
                adminDashboardService.markProvisioningInProgress(id, adminDetails.getId()));
    }

    /** Admin marks a bundled subscription as provisioning complete (sets PROVISIONED + ACTIVE). */
    @PostMapping("/subscriptions/{id}/provisioning/complete")
    public ResponseEntity<?> completeProvisioning(
            @PathVariable Long id,
            @AuthenticationPrincipal CustomUserDetails adminDetails) {
        return ResponseEntity.ok(
                adminDashboardService.completeProvisioning(id, adminDetails.getId()));
    }
}
