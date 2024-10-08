package org.fms.training.util;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

public final class DateTimeUtils {

    private DateTimeUtils() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public static Date convertToUtilDate(LocalDateTime localDateTime) {
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }
}
