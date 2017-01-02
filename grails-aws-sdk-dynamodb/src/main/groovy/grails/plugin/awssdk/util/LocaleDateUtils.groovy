package grails.plugin.awssdk.util

import org.joda.time.DateTimeZone
import org.joda.time.LocalDate

class LocaleDateUtils {

    static final DateTimeZone jodaTzUTC = DateTimeZone.forID("UTC")

    // from Date to LocalDate:
    static LocalDate dateToLocalDate(Date date) {
        if(!date) return null
        new LocalDate(date.time, jodaTzUTC)
    }

    // from LocalDate to Date:
    static Date localDateToDate(LocalDate localDate) {
        if(!localDate) return null
        new Date(localDate.toDateTimeAtStartOfDay(jodaTzUTC).millis)
    }

}
