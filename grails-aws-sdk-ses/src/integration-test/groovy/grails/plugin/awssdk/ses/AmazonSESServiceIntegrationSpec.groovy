package grails.plugin.awssdk.ses

import com.agorapulse.awssdk.ses.UnsupportedAttachmentTypeException
import grails.test.mixin.integration.Integration
import grails.util.Environment
import org.springframework.beans.factory.annotation.Autowired
import spock.lang.IgnoreIf
import spock.lang.Specification

@Integration
class AmazonSESServiceIntegrationSpec extends Specification {

    String email = System.getenv('TEST_INBOX_EMAIL')
    ReadMail readEmail = ReadMail.withEnvironmentVariables()

    @Autowired
    AmazonSESService amazonSESService

    @IgnoreIf({ !System.getProperty('TEST_INBOX_EMAIL') ||
                !System.getProperty('TEST_INBOX_PASSWORD') ||
                !System.getProperty('TEST_INBOX_HOST') ||
                !System.getProperty('TEST_INBOX_FOLDER') ||
                !System.getProperty('TEST_INBOX_PROVIDER') })
    void "test AmazonSESService.mail method actually delivers an email"() {
        when:
        def subjectStr = 'GRAILS AWS SDK SES Subject'
        int deliveryIndicator = amazonSESService.mail {
            to email
            subject subjectStr
            from email
        }
        then:
        deliveryIndicator == 1

        when: " Fetch emails from the server to test the email have been delivered "
        sleep(10_000) // sleep for ten senconds to ensure the email has reached the server
        def currentEnv = Environment.current
        def emailSubject = "[${currentEnv}] ${subjectStr}" as String
        boolean emailFound = readEmail.fetchFolderMessageSubjects().any { it == emailSubject}

        then:
        emailFound

        when:
        readEmail.deleteMessagesAtInboxWithSubject(emailSubject)
        emailFound = readEmail.fetchFolderMessageSubjects().any { it == emailSubject}

        then:
        !emailFound
    }

    @IgnoreIf({ !System.getProperty('TEST_INBOX_EMAIL') ||
            !System.getProperty('TEST_INBOX_PASSWORD') ||
            !System.getProperty('TEST_INBOX_HOST') ||
            !System.getProperty('TEST_INBOX_FOLDER') ||
            !System.getProperty('TEST_INBOX_PROVIDER') })
    void "test send attachment"() {
        when:
        def f = new File('src/integration-test/groovy/grails/plugin/awssdk/ses/groovylogo.png')

        then:
        f.exists()

        when:
        def subjectStr = 'GRAILS AWS SDK SES with Attachment'
        int deliveryIndicator = amazonSESService.mailWithAttachment {
            subject subjectStr
            htmlBody '<p>This is an example body</p>'
            to email
            from email
            attachment {
                filepath f.absolutePath
            }
        }

        then:
        deliveryIndicator == 1

        when: " Fetch emails from the server to test the email have been delivered "
        sleep(10_000) // sleep for ten senconds to ensure the email has reached the server
        def currentEnv = Environment.current
        def emailSubject = "[${currentEnv}] ${subjectStr}" as String
        boolean emailFound = readEmail.fetchFolderMessageSubjects().any { it == emailSubject}
        List<Map> messages = readEmail.messagesWithSubjectAtInbox(emailSubject)

        then:
        emailFound
        messages
        messages.size() >= 1

        when:
        boolean haveAttachments = readEmail.doMessagesWithSubjectAtInboxHaveAttachments(emailSubject)

        then:
        haveAttachments

        when:
        readEmail.deleteMessagesAtInboxWithSubject(emailSubject)
        emailFound = readEmail.fetchFolderMessageSubjects().any { it == emailSubject}

        then:
        !emailFound
    }

    void "test that if you try to send an unsupported attachment an exception is thrown "() {
        when:
        def subjectStr = 'GRAILS AWS SDK SES with Attachment'
        amazonSESService.mailWithAttachment {
            subject subjectStr
            htmlBody '<p>This is an example body</p>'
            to email
            from email
            attachment {
                filepath '/temp/virus.exe'
                filename 'virus.exe'
                mimeType 'application/octet-stream'
            }
        }

        then:
        thrown UnsupportedAttachmentTypeException
    }
}