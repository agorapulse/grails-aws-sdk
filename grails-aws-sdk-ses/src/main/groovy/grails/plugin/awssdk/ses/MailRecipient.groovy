package grails.plugin.awssdk.ses

interface MailRecipient {

    String getEmail()
    int getEmailBlacklistedCount()
    boolean getEmailValidated()
    Locale getLocale()
    int getTimeZoneGmt()

}