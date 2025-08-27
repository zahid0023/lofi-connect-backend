package org.example.loficonnect.exception;

import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(FeignException.class)
    public ResponseEntity<Map<String, Object>> handleFeignException(FeignException ex) {
        int status = ex.status();
        String message = "Feign client error";

        try {
            if (ex.content() != null && ex.content().length > 0) {
                String body = new String(ex.content(), StandardCharsets.UTF_8);
                Map<String, Object> map = new com.fasterxml.jackson.databind.ObjectMapper().readValue(body, Map.class);

                // Check for GoHighLevel specific error
                String error = (String) map.get("error");
                String errorDescription = (String) map.get("error_description");

                if ("invalid_grant".equals(error) && errorDescription != null) {
                    message = "Authentication failed: Refresh token is invalid or expired. "
                              + "Please re-authorize the app to generate a new token.";
                    status = HttpStatus.UNAUTHORIZED.value();
                } else {
                    message = map.getOrDefault("message", message).toString();
                    status = (int) map.getOrDefault("statusCode", status);
                }
            }
        } catch (Exception e) {
            // fallback if parsing fails
            log.warn("Failed to parse Feign exception body: {}", e.getMessage());
        }

        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", Instant.now().toString());
        body.put("statusCode", status);
        body.put("message", message);

        return ResponseEntity.status(status).body(body);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Map<String, Object>> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        String paramName = ex.getName();
        String message;

        if (ex.getRequiredType() != null) {
            Class<?> requiredType = ex.getRequiredType();

            if (LocalDate.class.isAssignableFrom(requiredType)) {
                message = String.format("Invalid value for parameter '%s'. Expected Format: yyyy-MM-dd", paramName);
            } else if (LocalTime.class.isAssignableFrom(requiredType)) {
                message = String.format("Invalid value for parameter '%s'. Expected Format: HH:mm:ss", paramName);
            } else {
                message = String.format("Invalid value for parameter '%s'. Expected type: %s", paramName, requiredType.getSimpleName());
            }
        } else {
            message = String.format("Invalid value for parameter '%s'.", paramName);
        }

        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", Instant.now().toString());
        body.put("statusCode", HttpStatus.BAD_REQUEST.value());
        body.put("message", message);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    // Optional: handle other exceptions globally
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGenericException(Exception ex) {
        Map<String, Object> body = Map.of(
                "timestamp", Instant.now().toString(),
                "statusCode", HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "message", ex.getMessage()
        );

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
    }
}
