package org.example.loficonnect.commons.exception;

public class QuotaExceededException extends RuntimeException {
    public QuotaExceededException(String message) {
        super(message);
    }
}
