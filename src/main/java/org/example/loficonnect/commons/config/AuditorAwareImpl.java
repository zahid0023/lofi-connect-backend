package org.example.loficonnect.commons.config;

import org.example.loficonnect.auth.model.dto.CustomUserDetails;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Optional;

@Component("auditorProvider")
public class AuditorAwareImpl implements AuditorAware<@NonNull Long> {

    @Override
    public @NonNull Optional<Long> getCurrentAuditor() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || Objects.equals(auth.getPrincipal(), "anonymousUser")) {
            return Optional.of(1L); // system user
        }

        CustomUserDetails userDetails = (CustomUserDetails) auth.getPrincipal();
        return Optional.of(userDetails.getId());
    }
}