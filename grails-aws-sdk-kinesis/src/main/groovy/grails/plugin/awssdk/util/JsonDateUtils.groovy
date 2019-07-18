package grails.plugin.awssdk.util

import java.time.Instant
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeFormatterBuilder

class JsonDateUtils {

    public static final DateTimeFormatter ISO_FORMAT_MILLIS = new DateTimeFormatterBuilder().appendInstant(3).toFormatter();

    static Date parseJsonDate(String date) {
        Date.from(Instant.parse(date))
    }

    static String formatDate(Date date) {
        ISO_FORMAT_MILLIS.format(date.toInstant())
    }

}
