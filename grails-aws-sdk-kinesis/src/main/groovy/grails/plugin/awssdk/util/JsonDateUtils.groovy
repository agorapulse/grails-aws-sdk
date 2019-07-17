package grails.plugin.awssdk.util

import java.time.Instant
import java.time.format.DateTimeFormatter

/**
 * @deprecated use <code>java.time</code> classes directly
 */
@Deprecated
class JsonDateUtils {

    static Date parseJsonDate(String date) {
        Date.from(Instant.parse(date))
    }

    static String formatDate(Date date) {
        DateTimeFormatter.ISO_INSTANT.format(date.toInstant())
    }

}
