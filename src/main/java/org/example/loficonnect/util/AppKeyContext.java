package org.example.loficonnect.util;

public class AppKeyContext {
    private static final ThreadLocal<String> appKeyHolder = new ThreadLocal<>();

    public static void setAppKey(String appKey) {
        appKeyHolder.set(appKey);
    }

    public static String getAppKey() {
        return appKeyHolder.get();
    }

    public static void clearAppKeyHolder() {
        appKeyHolder.remove();
    }
}
