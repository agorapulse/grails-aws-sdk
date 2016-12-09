package com.agorapulse.awssdk.ses

import groovy.transform.CompileStatic

@CompileStatic
class TransactionalEmail {
    String sourceEmail
    String subject
    String htmlBody = '<html><body></body></html>'

    String replyToEmail

    List<String> recipients = []

    List<TransactionalEmailAttachment> attachments = []

    void attachment(Closure attachment) {
        Closure cl = (Closure) attachment.clone()
        TransactionalEmailAttachment att = new TransactionalEmailAttachment()
        cl.delegate = att
        cl.resolveStrategy = Closure.DELEGATE_FIRST
        cl()
        attachments << att
    }

    @SuppressWarnings('ConfusingMethodName')
    void recipients(List<String> recipients) {
        this.recipients = recipients
    }

    @SuppressWarnings('ConfusingMethodName')
    void to(String str) {
        this.recipients = [str]
    }

    @SuppressWarnings('ConfusingMethodName')
    void from(String str) {
        this.sourceEmail = str
    }

    @SuppressWarnings('ConfusingMethodName')
    void sourceEmail(String str) {
        this.sourceEmail = str
    }

    @SuppressWarnings('ConfusingMethodName')
    void destinationEmail(String str) {
        this.recipients = [str]
    }

    String getDestinationEmail() {
        (this.recipients.isEmpty()) ? null : this.recipients.first()
    }

    @SuppressWarnings('ConfusingMethodName')
    void subject(String str) {
        this.subject = str
    }

    @SuppressWarnings('ConfusingMethodName')
    void htmlBody(String str) {
        this.htmlBody = str
    }

    @SuppressWarnings('ConfusingMethodName')
    void replyToEmail(String str) {
        this.replyToEmail = str
    }
}
