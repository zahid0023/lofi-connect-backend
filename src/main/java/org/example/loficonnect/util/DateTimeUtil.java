package org.example.loficonnect.util;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public class DateTimeUtil {

    /**
     * Convert LocalDateTime and timezone to epoch milliseconds.
     *
     * @param localDateTime LocalDateTime to convert
     * @param timezone      Timezone ID, e.g. "America/New_York"
     * @return epoch milliseconds
     */
    public static long toEpochMillis(LocalDateTime localDateTime, String timezone) {
        ZonedDateTime zonedDateTime = localDateTime.atZone(ZoneId.of(timezone));
        return zonedDateTime.toInstant().toEpochMilli();
    }
}
