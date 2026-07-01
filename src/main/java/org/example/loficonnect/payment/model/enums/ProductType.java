package org.example.loficonnect.payment.model.enums;

/**
 * Distinguishes between plan products:
 * <ul>
 *   <li>STANDALONE — fully automated provisioning after successful Paddle checkout.</li>
 *   <li>BUNDLED    — manual GHL subaccount provisioning by Admin/Finance.</li>
 * </ul>
 */
public enum ProductType {
    STANDALONE,
    BUNDLED
}
