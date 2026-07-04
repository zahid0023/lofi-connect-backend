package org.example.loficonnect.commons.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Enables Spring's async task execution (used by audit log and email services)
 * and scheduled task execution (used by the subscription lifecycle scheduler).
 */
@Configuration
@EnableAsync
@EnableScheduling
public class AsyncSchedulingConfig {
}
