package grails.plugin.awssdk.ses

import groovy.transform.CompileStatic

@CompileStatic
class TransactionalEmailTemplate {
    String destinationEmail
    String subjectCode
    String defaultSubject
    List subjectVariables = []
    Map model
    String templateName
    Locale locale = Locale.ENGLISH
    int timeZoneGmt = 0
    String replyToEmail = ''


    void to(String str) {
        this.destinationEmail = str
    }

    void defaultSubject(String str) {
        this.defaultSubject = str
    }

    void destinationEmail(String str) {
        this.destinationEmail = str
    }

    void subjectCode(String str) {
        this.subjectCode = str
    }

    void subjectVariables(List subjectVariables) {
        this.subjectVariables = subjectVariables
    }

    void model(Map model) {
        this.model = model
    }

    void templateName(String templateName) {
        this.templateName = templateName
    }

    void locale(Locale locale) {
        this.locale = locale
    }

    void timeZoneGmt(int timeZoneGmt) {
        this.timeZoneGmt = timeZoneGmt
    }

    void replyToEmail(String replyToEmail) {
        this.replyToEmail = replyToEmail
    }
}
