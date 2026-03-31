package org.example.loficonnect.auth.config;

import org.example.loficonnect.auth.exception.CustomAccessDeniedHandler;
import org.example.loficonnect.auth.exception.CustomAuthenticationEntryPoint;
import org.example.loficonnect.auth.filter.JwtAuthenticationFilter;
import org.example.loficonnect.commons.filter.AppKeyUsageFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
    private final AppKeyUsageFilter appKeyUsageFilter;

    public SecurityConfig(JwtAuthenticationFilter jwtFilter, AppKeyUsageFilter appKeyUsageFilter) {
        this.jwtFilter = jwtFilter;
        this.appKeyUsageFilter = appKeyUsageFilter;
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
                        .requestMatchers("/actuator/**").permitAll()
                        .requestMatchers("/api/v1/auth/**").permitAll()
                        .requestMatchers("/api/v1/ghl/**").permitAll()
                        .requestMatchers("/api/v1/admins/**").hasRole("ADMIN")
                        .requestMatchers("/api/v1/users/**").hasRole("USER")
                        .anyRequest().authenticated()
                )
                .addFilterBefore(appKeyUsageFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}

