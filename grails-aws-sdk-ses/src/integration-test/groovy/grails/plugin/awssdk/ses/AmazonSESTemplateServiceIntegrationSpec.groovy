package grails.plugin.awssdk.ses

import grails.test.mixin.integration.Integration
import grails.util.Environment
import org.springframework.beans.factory.annotation.Autowired
import spock.lang.IgnoreIf
import spock.lang.Specification
import static com.agorapulse.awssdk.ses.AwsSdkSesEmailDeliveryStatus.*

@Integration
class AmazonSESTemplateServiceIntegrationSpec extends Specification {
    String email = System.getenv('TEST_INBOX_EMAIL')
    ReadMail readEmail = ReadMail.withEnvironmentVariables()

    @Autowired
    AmazonSESTemplateService amazonSESTemplateService


    def "test renderHtmlForTemplate"() {
        when:
        String destinationEmail = 'me@email.com'
        String templateName = 'test'
        String htmlBody = amazonSESTemplateService.renderHtmlForTemplate(Locale.ENGLISH, [foo: 'bar'], destinationEmail, templateName)

        then:
        htmlBody
        htmlBody.contains('The template content with some foo=bar')
        htmlBody.contains('<html>')
        htmlBody.contains('</html>')

    }

    @IgnoreIf({ !System.getenv('TEST_INBOX_EMAIL') ||
            !System.getenv('TEST_INBOX_PASSWORD') ||
            !System.getenv('TEST_INBOX_HOST') ||
            !System.getenv('TEST_INBOX_FOLDER') ||
            !System.getenv('TEST_INBOX_PROVIDER') })
    void "test AmazonSESTemplateService.sendTemplate method actually delivers an email with a template rendered"() {
        when:
        def statusId = amazonSESTemplateService.sendTemplate(
                email,
                'email.test.subject', // defined in mail.properties
                [],             // Subject variables, if required
                [foo: 'bar'],
                'test'          // GSP located in '/views/template/emails/_test.gsp'
        )
        then:
        statusId == STATUS_DELIVERED

        when:
        sleep(10_000) // sleep for ten senconds to ensure the email has reached the server
        def expectedSubject = "[${Environment.current}] Test subject"
        List<Map> messages = readEmail.messagesWithSubjectAtInbox(expectedSubject)

        def expectedSubstring = "The template content with some foo=bar"
        then:
        messages
        messages.first().body.indexOf(expectedSubstring) != -1
        when:
        readEmail.deleteMessagesAtInboxWithSubject(expectedSubject)
        boolean emailFound = readEmail.fetchFolderMessageSubjects().any { it == expectedSubject}

        then:
        !emailFound
    }

    @IgnoreIf({ !System.getenv('TEST_INBOX_EMAIL') ||
                !System.getenv('TEST_INBOX_PASSWORD') ||
                !System.getenv('TEST_INBOX_HOST') ||
                !System.getenv('TEST_INBOX_FOLDER') ||
                !System.getenv('TEST_INBOX_PROVIDER') })
    void "test AmazonSESTemplateService.mailTemplate method actually delivers an email with a template rendered"() {
        when:
        def m = [foo: 'bar']
        def statusId = amazonSESTemplateService.mailTemplate {
            to email
            subjectCode 'email.test.subject' // defined in mail.properties
            model m
            templateName 'test' // GSP located in '/views/template/emails/_test.gsp'
        }
        then:
        statusId == STATUS_DELIVERED

        when:
        sleep(10_000) // sleep for ten senconds to ensure the email has reached the server
        def expectedSubject = "[${Environment.current}] Test subject"
        List<Map> messages = readEmail.messagesWithSubjectAtInbox(expectedSubject)

        def expectedSubstring = "The template content with some foo=bar"
        then:
        messages
        messages.first().body.indexOf(expectedSubstring) != -1
        when:
        readEmail.deleteMessagesAtInboxWithSubject(expectedSubject)
        boolean emailFound = readEmail.fetchFolderMessageSubjects().any { it == expectedSubject}

        then:
        !emailFound
    }

}