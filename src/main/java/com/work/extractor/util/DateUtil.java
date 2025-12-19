package com.work.extractor.util;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.Year;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public final class DateUtil {

    private DateUtil() {

    }

    public static LocalDateTime toLocalDateTime(Instant instant) {
        return LocalDateTime.ofInstant(
                instant,
                ZoneId.systemDefault()
        );
    }

    public static String format(long time) {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")
                .withZone(ZoneId.systemDefault());
        return fmt.format(Instant.ofEpochMilli(time));
    }

    public static int getYear() {
        return Year.now().getValue();
    }

}
