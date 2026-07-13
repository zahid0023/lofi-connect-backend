package org.example.loficonnect.util;

/**
 * ThreadLocal store for the validated app key, its ID, and rate-limit state.
 * Populated by {@code AppKeyInterceptor.preHandle} on every {@code @AppKey}-protected request.
 * Cleared in {@code AppKeyInterceptor.afterCompletion}.
 */
public class AppKeyContext {

    private static final ThreadLocal<String>  appKeyHolder            = new ThreadLocal<>();
    private static final ThreadLocal<Long>    appKeyIdHolder          = new ThreadLocal<>();
    /** Remaining calls in the current billing period (null = unlimited plan). */
    private static final ThreadLocal<Integer> rateLimitRemainingHolder = new ThreadLocal<>();
    /** Total allowed calls per billing period (null = unlimited plan). */
    private static final ThreadLocal<Long>    rateLimitTotalHolder     = new ThreadLocal<>();

    public static void setAppKey(String appKey) { appKeyHolder.set(appKey); }
    public static String getAppKey()            { return appKeyHolder.get(); }

    public static void setAppKeyId(Long id) { appKeyIdHolder.set(id); }
    public static Long  getAppKeyId()       { return appKeyIdHolder.get(); }

    public static void setRateLimitRemaining(int remaining) { rateLimitRemainingHolder.set(remaining); }
    public static Integer getRateLimitRemaining()           { return rateLimitRemainingHolder.get(); }

    public static void setRateLimitTotal(long total) { rateLimitTotalHolder.set(total); }
    public static Long  getRateLimitTotal()          { return rateLimitTotalHolder.get(); }

    public static void clearAppKeyHolder() {
        appKeyHolder.remove();
        appKeyIdHolder.remove();
        rateLimitRemainingHolder.remove();
        rateLimitTotalHolder.remove();
    }
}
