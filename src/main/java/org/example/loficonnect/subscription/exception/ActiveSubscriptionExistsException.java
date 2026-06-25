package org.example.loficonnect.subscription.exception;

public class ActiveSubscriptionExistsException extends RuntimeException {
    public ActiveSubscriptionExistsException(String message) {
        super(message);
    }
}
