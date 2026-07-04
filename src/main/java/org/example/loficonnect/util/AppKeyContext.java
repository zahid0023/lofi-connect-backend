package org.example.loficonnect.util;

/**
 * ThreadLocal store for the validated app key and its ID.
 * Populated by {@code AppKeyInterceptor.preHandle} on every {@code @AppKey}-protected request.
 * Cleared in {@code AppKeyInterceptor.afterCompletion}.
 */
public class AppKeyContext {

    private static final ThreadLocal<String> appKeyHolder   = new ThreadLocal<>();
    private static final ThreadLocal<Long>   appKeyIdHolder = new ThreadLocal<>();

    public static void setAppKey(String appKey) { appKeyHolder.set(appKey); }
    public static String getAppKey()            { return appKeyHolder.get(); }

    public static void setAppKeyId(Long id) { appKeyIdHolder.set(id); }
    public static Long  getAppKeyId()       { return appKeyIdHolder.get(); }

    public static void clearAppKeyHolder() {
        appKeyHolder.remove();
        appKeyIdHolder.remove();
    }
}
