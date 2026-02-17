package org.example.loficonnect.auth.util;

import java.util.UUID;

public class AppKeyGenerator {
    public static String generateAppKey() {
        return UUID.randomUUID().toString();
    }
}
