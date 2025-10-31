package org.example.loficonnect.util;

public class LocationContext {
    private static final ThreadLocal<String> locationIdHolder = new ThreadLocal<>();

    public static void setLocationId(String locationId) {
        locationIdHolder.set(locationId);
    }

    public static String getLocationId() {
        return locationIdHolder.get();
    }

    public static void clearLocationIdHolder() {
        locationIdHolder.remove();
    }
}
