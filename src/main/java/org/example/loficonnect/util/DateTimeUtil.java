package org.example.loficonnect.util;

import java.time.*;

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

    public static ZonedDateTime toZonedDateTime(LocalDateTime localDateTime, String timezone) {
        return localDateTime.atZone(ZoneId.of(timezone));
    }

    public static ZonedDateTime toZonedDateTime(LocalDate localDate, LocalTime localTime, String timezone) {
        return localDate.atTime(localTime).atZone(ZoneId.of(timezone));
    }
}
