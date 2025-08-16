package org.example.loficonnect.util;

import java.util.Map;

public class MapUtil {

    /**
     * Puts the key-value pair into the map if the value is not null.
     * If the value is a String, it also checks that it is not empty.
     */
    public static void putIfNotNull(Map<String, Object> map, String key, Object value) {
        if (value != null) {
            if (value instanceof String) {
                if (!((String) value).isEmpty()) {
                    map.put(key, value);
                }
            } else {
                map.put(key, value);
            }
        }
    }
}
