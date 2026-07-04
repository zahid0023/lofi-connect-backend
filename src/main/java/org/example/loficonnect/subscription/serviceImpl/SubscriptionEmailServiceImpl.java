package org.example.loficonnect.subscription.serviceImpl;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.example.loficonnect.subscription.model.entity.TenantSubscriptionEntity;
import org.example.loficonnect.subscription.service.SubscriptionEmailService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@Slf4j
@Service
public class SubscriptionEmailServiceImpl implements SubscriptionEmailService {

    private static final DateTimeFormatter DATE_FMT =
            DateTimeFormatter.ofPattern("MMM dd, yyyy").withZone(ZoneId.of("UTC"));

    private final JavaMailSender mailSender;
    private final String senderEmail;
    private final String senderName;
    private final String frontendUrl;

    public SubscriptionEmailServiceImpl(
            JavaMailSender mailSender,
            @Value("${spring.mail.username}") String senderEmail,
            @Value("${spring.mail.properties.mail.sender.name}") String senderName,
            @Value("${frontend.url}") String frontendUrl) {
        this.mailSender = mailSender;
        this.senderEmail = senderEmail;
        this.senderName = senderName;
        this.frontendUrl = frontendUrl;
    }

    @Async
    @Override
    public void sendTrialWelcome(String toEmail, TenantSubscriptionEntity sub) {
        String trialEnd = sub.getTrialEndsAt() != null ? DATE_FMT.format(sub.getTrialEndsAt()) : "N/A";
        send(toEmail, "Welcome to Your Free Trial!",
                html("Your Free Trial Has Started",
                        "Your 7-day free trial for <strong>" + sub.getSubscriptionPlan().getName() + "</strong> is now active.",
                        "Your trial ends on <strong>" + trialEnd + "</strong>. Enjoy full access until then.",
                        "Go to Dashboard", frontendUrl + "/dashboard"));
    }

    @Async
    @Override
    public void sendPaymentFailed(String toEmail, TenantSubscriptionEntity sub) {
        send(toEmail, "Action Required: Payment Failed",
                html("Payment Failed",
                        "We were unable to process your payment for <strong>" + sub.getSubscriptionPlan().getName() + "</strong>.",
                        "Please update your payment method to avoid losing access. You are in a short grace period.",
                        "Update Payment", frontendUrl + "/billing"));
    }

    @Async
    @Override
    public void sendGraceWarning(String toEmail, TenantSubscriptionEntity sub) {
        send(toEmail, "Warning: Your Access Will Be Limited Soon",
                html("Grace Period Active",
                        "Your payment for <strong>" + sub.getSubscriptionPlan().getName() + "</strong> is still outstanding.",
                        "Your account will move to read-only mode soon. Update your payment method now to retain full access.",
                        "Update Payment", frontendUrl + "/billing"));
    }

    @Async
    @Override
    public void sendAccessLimited(String toEmail, TenantSubscriptionEntity sub) {
        send(toEmail, "Your Account Has Been Limited",
                html("Access Limited",
                        "Due to non-payment, your API access for <strong>" + sub.getSubscriptionPlan().getName() + "</strong> is now suspended.",
                        "Your dashboard is still accessible. Update your billing information to restore full access.",
                        "Update Payment", frontendUrl + "/billing"));
    }

    @Async
    @Override
    public void sendSuspended(String toEmail, TenantSubscriptionEntity sub) {
        send(toEmail, "Your Account Has Been Suspended",
                html("Account Suspended",
                        "All access to your <strong>" + sub.getSubscriptionPlan().getName() + "</strong> subscription has been suspended due to non-payment.",
                        "To restore access, please update your billing information.",
                        "Restore Access", frontendUrl + "/billing"));
    }

    @Async
    @Override
    public void sendCancellationConfirmed(String toEmail, TenantSubscriptionEntity sub) {
        String endDate = sub.getEndDate() != null ? DATE_FMT.format(sub.getEndDate()) : "immediately";
        send(toEmail, "Subscription Cancellation Confirmed",
                html("Cancellation Confirmed",
                        "Your <strong>" + sub.getSubscriptionPlan().getName() + "</strong> subscription has been cancelled.",
                        "You will retain full access until <strong>" + endDate + "</strong>.",
                        "View Account", frontendUrl + "/account"));
    }

    @Async
    @Override
    public void sendRefundRequested(String toEmail, TenantSubscriptionEntity sub) {
        send(toEmail, "Refund Request Received",
                html("We Received Your Refund Request",
                        "Your refund request for <strong>" + sub.getSubscriptionPlan().getName() + "</strong> has been submitted.",
                        "Our team will review it and contact you within 3–5 business days.",
                        "View Subscription", frontendUrl + "/subscription"));
    }

    @Async
    @Override
    public void sendRefundDecision(String toEmail, TenantSubscriptionEntity sub, boolean approved) {
        if (approved) {
            send(toEmail, "Refund Approved",
                    html("Your Refund Has Been Approved",
                            "Your refund request for <strong>" + sub.getSubscriptionPlan().getName() + "</strong> was approved.",
                            "The refund will appear on your original payment method within 5–10 business days.",
                            "View Account", frontendUrl + "/account"));
        } else {
            send(toEmail, "Refund Request Not Approved",
                    html("Refund Request Rejected",
                            "We were unable to approve your refund request for <strong>" + sub.getSubscriptionPlan().getName() + "</strong>.",
                            "If you have questions, please reach out to our support team.",
                            "Contact Support", frontendUrl + "/support"));
        }
    }

    @Async
    @Override
    public void sendBundledSetupInProgress(String toEmail, TenantSubscriptionEntity sub) {
        send(toEmail, "Your CRM Setup Is In Progress",
                html("Setup In Progress",
                        "Thank you for subscribing to <strong>" + sub.getSubscriptionPlan().getName() + "</strong>.",
                        "Our team is setting up your CRM account. You will receive another email when it is ready — usually within 1 business day.",
                        "View Subscription", frontendUrl + "/subscription"));
    }

    @Async
    @Override
    public void sendBundledSetupComplete(String toEmail, TenantSubscriptionEntity sub) {
        send(toEmail, "Your CRM Account Is Ready!",
                html("Your CRM Is Ready",
                        "Great news! Your CRM account for <strong>" + sub.getSubscriptionPlan().getName() + "</strong> has been set up.",
                        "You can now log in and start using all features.",
                        "Go to Dashboard", frontendUrl + "/dashboard"));
    }

    @Async
    @Override
    public void sendPlanChanged(String toEmail, TenantSubscriptionEntity sub,
                                String oldPlanName, String newPlanName) {
        send(toEmail, "Your Plan Has Been Updated",
                html("Plan Update Confirmation",
                        "Your subscription has been updated from <strong>" + oldPlanName + "</strong> to <strong>" + newPlanName + "</strong>.",
                        "Your new plan is now active and all limits have been updated accordingly.",
                        "View Subscription", frontendUrl + "/subscription"));
    }

    @Async
    @Override
    public void sendCheckoutReminder(String toEmail, String planName) {
        send(toEmail, "Don't Forget to Complete Your Subscription",
                html("You Left Something Behind",
                        "You started subscribing to <strong>" + planName + "</strong> but did not complete checkout.",
                        "Your session is still valid. Click below to complete your free trial activation.",
                        "Complete Checkout", frontendUrl + "/pricing"));
    }

    // ─── Internal helpers ─────────────────────────────────────────────────────

    private void send(String toEmail, String subject, String htmlBody) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setFrom(senderEmail, senderName);
            helper.setTo(toEmail);
            helper.setSubject(subject);
            helper.setText(htmlBody, true);
            mailSender.send(message);
        } catch (MessagingException | UnsupportedEncodingException e) {
            log.error("Failed to send subscription email to {}: {}", toEmail, e.getMessage());
        }
    }

    private String html(String title, String intro, String detail, String ctaText, String ctaUrl) {
        return """
                <html><body style="font-family:Arial,sans-serif;color:#333;line-height:1.6;max-width:600px;margin:0 auto;">
                  <h2 style="color:#1a1a2e;">%s</h2>
                  <p>%s</p>
                  <p>%s</p>
                  <p style="margin-top:24px;">
                    <a href="%s" style="background:#4f46e5;color:#fff;padding:12px 24px;
                       text-decoration:none;border-radius:6px;font-weight:bold;">%s</a>
                  </p>
                  <hr style="margin-top:40px;border:none;border-top:1px solid #eee;"/>
                  <p style="color:#aaa;font-size:12px;">
                    If you have any questions, contact our support team.
                  </p>
                </body></html>
                """.formatted(title, intro, detail, ctaUrl, ctaText);
    }
}
