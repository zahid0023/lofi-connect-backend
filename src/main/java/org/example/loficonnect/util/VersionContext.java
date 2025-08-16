package org.example.loficonnect.util;

public class VersionContext {
    private static final ThreadLocal<String> versionHolder = new ThreadLocal<>();

    public static void setVersion(String version) {
        versionHolder.set(version);
    }

    public static String getVersion() {
        return versionHolder.get();
    }

    public static void clearVersionHolder() {
        versionHolder.remove();
    }
}