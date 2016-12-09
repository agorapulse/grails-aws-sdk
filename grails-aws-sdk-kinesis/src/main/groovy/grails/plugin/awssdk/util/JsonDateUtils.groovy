package grails.plugin.awssdk.util

import java.text.SimpleDateFormat

class JsonDateUtils {

    static JSON_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss'Z'"

    static SimpleDateFormat jsonDateFormatter

    static Date parseJsonDate(String date) {
        if (!jsonDateFormatter) {
            jsonDateFormatter = new SimpleDateFormat(JSON_DATE_FORMAT)
            jsonDateFormatter.timeZone = TimeZone.getTimeZone('GMT')
        }
        jsonDateFormatter.parse(date)
    }

    static String formatDate(Date date) {
        if (!jsonDateFormatter) {
            jsonDateFormatter = new SimpleDateFormat(JSON_DATE_FORMAT)
            jsonDateFormatter.timeZone = TimeZone.getTimeZone('GMT')
        }
        jsonDateFormatter.format(date)
    }

}
