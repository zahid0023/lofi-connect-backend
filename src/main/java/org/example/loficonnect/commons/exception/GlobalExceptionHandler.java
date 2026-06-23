package org.example.loficonnect.commons.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import feign.FeignException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.example.loficonnect.commons.dto.response.ApiErrorResponse;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(FeignException.class)
    public ResponseEntity<Map<String, Object>> handleFeignException(FeignException ex) {

        int status = ex.status();
        String message = "Feign client error";

        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("timestamp", Instant.now().toString());

        try {
            String body = ex.contentUTF8(); // ✅ FIX

            if (body != null && !body.isBlank()) {

                ObjectMapper mapper = new ObjectMapper();
                Map<String, Object> map = mapper.readValue(body, Map.class);

                message = String.valueOf(map.getOrDefault("message", message));

                Object statusCodeObj = map.get("statusCode");
                if (statusCodeObj instanceof Number) {
                    status = ((Number) statusCodeObj).intValue();
                }

                responseBody.putAll(map);
            } else {
                responseBody.put("message", ex.getMessage());
            }

        } catch (Exception e) {
            log.warn("Failed to parse Feign exception body: {}", e.getMessage());
            responseBody.put("message", ex.getMessage());
        }

        responseBody.put("statusCode", status);
        responseBody.put("message", message);

        return ResponseEntity.status(status).body(responseBody);
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

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<@NonNull ApiErrorResponse> handleDataIntegrityViolation(
            DataIntegrityViolationException ex,
            HttpServletRequest request
    ) {

        String rootMessage = extractRootCauseMessage(ex);

        log.warn("Data integrity violation: {}", rootMessage);

        ApiErrorResponse response = new ApiErrorResponse(
                request.getHeader("X-Request-Id"),
                HttpStatus.CONFLICT.value(),
                "DATA_INTEGRITY_VIOLATION",
                rootMessage
        );

        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<@NonNull ApiErrorResponse> handleConstraintViolation(
            ConstraintViolationException ex,
            HttpServletRequest request
    ) {
        String message = ex.getConstraintViolations().stream()
                .map(v -> v.getPropertyPath() + ": " + v.getMessage())
                .collect(Collectors.joining(", "));

        log.warn("Constraint violation: {}", message);

        ApiErrorResponse response = new ApiErrorResponse(
                request.getHeader("X-Request-Id"),
                HttpStatus.BAD_REQUEST.value(),
                "VALIDATION_ERROR",
                message
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(AppKeyInvalidException.class)
    public ResponseEntity<@NonNull ApiErrorResponse> handleAppKeyInvalid(
            AppKeyInvalidException ex,
            HttpServletRequest request
    ) {
        return build(ex, HttpStatus.UNAUTHORIZED, "INVALID_APP_KEY", request);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<@NonNull ApiErrorResponse> handleIllegalArgument(
            IllegalArgumentException ex,
            HttpServletRequest request
    ) {
        return build(
                ex,
                HttpStatus.BAD_REQUEST,
                "INVALID_ARGUMENT",
                request
        );
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<@NonNull ApiErrorResponse> handleEntityNotFound(
            EntityNotFoundException ex,
            HttpServletRequest request
    ) {

        log.warn("Entity not found: {}", ex.getMessage());

        ApiErrorResponse response = new ApiErrorResponse(
                request.getHeader("X-Request-Id"),
                HttpStatus.NOT_FOUND.value(),
                "ENTITY_NOT_FOUND",
                ex.getMessage()
        );

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(AuthorizationDeniedException.class)
    public ResponseEntity<@NonNull ApiErrorResponse> handleUnauthorized(
            AuthorizationDeniedException ex,
            HttpServletRequest request
    ) {
        log.error("Unauthorized access attempt: {}", ex.getMessage());
        log.error("Unauthorized access attempt: {}", ex.getAuthorizationResult());

        return build(
                ex,
                HttpStatus.FORBIDDEN,
                "UNAUTHORIZED",
                request
        );
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<@NonNull ApiErrorResponse> handleUnexpected(
            Exception ex,
            HttpServletRequest request
    ) {

        log.error("Unexpected error", ex);

        return build(
                ex,
                HttpStatus.INTERNAL_SERVER_ERROR,
                "INTERNAL_SERVER_ERROR",
                request
        );
    }

    private ResponseEntity<@NonNull ApiErrorResponse> build(
            Exception ex,
            HttpStatus status,
            String code,
            HttpServletRequest request
    ) {
        ApiErrorResponse response = new ApiErrorResponse(
                request.getHeader("X-Request-Id"),
                status.value(),
                code,
                ex.getMessage()
        );
        return ResponseEntity.status(status).body(response);
    }

    private String extractRootCauseMessage(Throwable ex) {
        Throwable root = ex;

        while (root.getCause() != null) {
            root = root.getCause();
        }

        return root.getMessage();
    }
}
