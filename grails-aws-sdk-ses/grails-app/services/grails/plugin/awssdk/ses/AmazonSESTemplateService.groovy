package grails.plugin.awssdk.ses

import grails.gsp.PageRenderer
import groovy.transform.CompileStatic
import groovy.transform.TypeCheckingMode
import groovy.util.logging.Slf4j
import org.joda.time.LocalDateTime
import org.springframework.context.MessageSource
import org.springframework.context.NoSuchMessageException

@Slf4j
@CompileStatic
class AmazonSESTemplateService extends AmazonSESService {

    MessageSource messageSource

    /**
     * Use the variable name groovyPageRenderer to enable
     * automatic Spring bean binding. Notice the variable
     * starts with groovy..., could be cause of confusing because
     * the type is PageRenderer without prefix Groovy....
     */
    PageRenderer groovyPageRenderer

    /**
     * @return 1 if successful, 0 if not sent, -1 if blacklisted
     */
    int mailTemplate(@DelegatesTo(TransactionalEmailTemplate) Closure composer) throws Exception {

        Closure cl = composer.clone() as Closure
        TransactionalEmailTemplate transactionalEmailTemplate = new TransactionalEmailTemplate()
        cl.delegate = transactionalEmailTemplate
        cl.resolveStrategy = Closure.DELEGATE_FIRST
        cl()

        sendTemplate(
                transactionalEmailTemplate.destinationEmail,
                transactionalEmailTemplate.subjectCode,
                transactionalEmailTemplate.subjectVariables,
                transactionalEmailTemplate.model,
                transactionalEmailTemplate.templateName,
                transactionalEmailTemplate.locale,
                transactionalEmailTemplate.timeZoneGmt,
                transactionalEmailTemplate.replyToEmail,
                transactionalEmailTemplate.defaultSubject)
    }

    /**
     * Global method to send email to anybody
     *
     * @param destinationEmail
     * @param subjectKey
     * @param subjectVariables
     * @param model
     * @param templateName
     * @param locale
     * @param timeZoneGmt
     * @param replyToEmail
     * @return 1 if successful, 0 if not sent, -1 if blacklisted
     */
    int sendTemplate(List<String> destinationEmails,
                     String subjectKey,
                     List subjectVariables,
                     Map model,
                     String templateName,
                     Locale locale = Locale.ENGLISH,
                     int timeZoneGmt = 0,
                     String replyToEmail = '',
                     String defaultSubject = '') {
        int statusId = 0
        if (destinationEmails.findAll()) {
            String htmlBody = renderHtmlForTemplate(locale, model, destinationEmails, templateName, timeZoneGmt)
            String subject = subjectWithSubjectKey(subjectKey, subjectVariables, locale, defaultSubject)
            statusId = send(destinationEmails, subject, htmlBody, '', replyToEmail)
        }
        statusId
    }

    /**
     * Global method to send email to anybody
     *
     * @param destinationEmail
     * @param subjectKey
     * @param subjectVariables
     * @param model
     * @param templateName
     * @param locale
     * @param timeZoneGmt
     * @param replyToEmail
     * @return 1 if successful, 0 if not sent, -1 if blacklisted
     */
    int sendTemplate(String destinationEmail,
                     String subjectKey,
                     List subjectVariables,
                     Map model,
                     String templateName,
                     Locale locale = Locale.ENGLISH,
                     int timeZoneGmt = 0,
                     String replyToEmail = '',
                     String defaultSubject = '') {
        return sendTemplate(Collections.singletonList(destinationEmail), subjectKey, subjectVariables, model, templateName, locale, timeZoneGmt, replyToEmail, defaultSubject)
    }

    private String subjectWithSubjectKey(String subjectKey, List subjectVariables, Locale locale = Locale.ENGLISH, String defaultSubject = '') {
        try {
            return messageSource.getMessage(subjectKey, subjectVariables as Object[], locale)

        } catch(NoSuchMessageException e) {
            log.error(e.message)
            return defaultSubject
        }
    }

    String renderHtmlForTemplate(Locale locale, Map model, List<String> destinationEmails, String templateName, int timeZoneGmt = 0) {
        def t = "${templatePath}/${templateName}" as String
        groovyPageRenderer.render(
                model: model + [
                        locale  : locale,
                        notificationEmail: destinationEmails.first(),
                        destinationEmails: destinationEmails,
                        sentDate: new LocalDateTime().plusMinutes((timeZoneGmt * 60).toInteger()).toDate()
                ],
                template: t
        )
    }

    /**
     * Global method to send email to a recipient (must implement MailRecipient interface)
     *
     * @param recipient
     * @param subjectKey
     * @param subjectVariables
     * @param model
     * @param templateName
     * @param locale
     * @param destinationEmail
     * @param replyToEmail
     * @return 1 if successful, 0 if not sent, -1 if blacklisted
     */
    @CompileStatic(TypeCheckingMode.SKIP) // Skip static compiliation since emailValidated Getter and save are no in the interface
    int sendTemplateToRecipient(MailRecipient recipient,
                                String subjectKey,
                                List subjectVariables,
                                Map model,
                                String templateName,
                                String destinationEmail = '',
                                String replyToEmail = '',
                                String defaultSubject = '') {
        int statusId = 0
        if (!destinationEmail && recipient.emailValidated) {
            destinationEmail = recipient.email
        }
        if (destinationEmail != '') {
            statusId = sendTemplate(
                    destinationEmail,
                    subjectKey,
                    subjectVariables,
                    model,
                    templateName,
                    recipient.locale,
                    recipient.timeZoneGmt,
                    replyToEmail,
                    defaultSubject
            )
            if (statusId == -1 && recipient.email == destinationEmail) {
                // Email has been blacklisted
                recipient.emailBlacklistedCount++
                recipient.emailValidated = false
                recipient.save()
            }
        }
        statusId
    }

}

