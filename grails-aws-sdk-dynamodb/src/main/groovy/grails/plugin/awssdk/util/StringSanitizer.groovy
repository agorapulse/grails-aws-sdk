package grails.plugin.awssdk.util;

public class StringSanitizer {

    public static String sanitize(String str) {
        // removes all 4-byte utf8 chars (emoji...), instead of using utf8mb4 charset in MySQL, and replace vertical tab with carriage return
        return str.replaceAll("[^\\u0000-\\uFFFF]", "").replaceAll("\\u000B", "\\\n");
    }

}
