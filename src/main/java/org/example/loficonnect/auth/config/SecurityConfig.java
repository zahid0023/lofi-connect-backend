package org.example.loficonnect.auth.config;

import org.example.loficonnect.auth.exception.CustomAccessDeniedHandler;
import org.example.loficonnect.auth.exception.CustomAuthenticationEntryPoint;
import org.example.loficonnect.auth.filter.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtFilter;

    public SecurityConfig(JwtAuthenticationFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http,
                                    CustomAccessDeniedHandler accessDeniedHandler,
                                    CustomAuthenticationEntryPoint authenticationEntryPoint) throws Exception {

        http
                .cors(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable) // Disable CSRF for APIs
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint(authenticationEntryPoint)
                        .accessDeniedHandler(accessDeniedHandler)
                )
                .authorizeHttpRequests(auth -> auth
                        // Allow Swagger UI and API docs
                        .requestMatchers(
                                "/v3/api-docs/**",
                                "/swagger-ui/**",
                                "/swagger-ui.html",
                                "/webjars/**",
                                "/api/v1/authorization/ghl/init",
                                "/api/v1/authorization/redirect",
                                "/api/v1/authorization/ghl/ping"
                        ).permitAll()
                        // Paddle webhook — unauthenticated (trust is via HMAC signature)
                        .requestMatchers(HttpMethod.POST, "/api/v1/payments/webhooks/paddle").permitAll()
                        // Paddle success redirect — browser navigation after checkout, no JWT
                        .requestMatchers(HttpMethod.GET, "/api/v1/subscriptions/tenant-subscriptions/success").permitAll()
                        // Public plan browsing — no login required
                        .requestMatchers(HttpMethod.GET, "/api/v1/subscriptions/plans/public").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/v1/subscriptions/plans/*").permitAll()
                        .requestMatchers("/actuator/**").permitAll()
                        .requestMatchers("/api/v1/auth/**").permitAll()
                        .requestMatchers("/api/v1/ghl/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/v1/subscription-plans", "/api/v1/subscription-plans/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/v1/currencies", "/api/v1/currencies/**").permitAll()
                        .requestMatchers("/api/v1/admins/**").hasRole("ADMIN")
                        .requestMatchers("/api/v1/users/**").hasRole("USER")
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}

