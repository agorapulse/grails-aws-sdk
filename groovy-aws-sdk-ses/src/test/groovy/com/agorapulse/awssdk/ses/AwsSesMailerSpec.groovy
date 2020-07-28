package com.agorapulse.awssdk.ses

import spock.lang.IgnoreIf
import spock.lang.Specification
import spock.lang.Subject

@SuppressWarnings(['AbcMetric', 'JavaIoPackageAccess'])
class AwsSesMailerSpec extends Specification {

    @Subject
    AwsSesMailer awsSesMailer = new AwsSesMailer()

    void "test transactionalEmailWithClosure"() {
        when:
        TransactionalEmail transactionalEmail = AwsSesMailer.transactionalEmailWithClosure {
            subject 'Hi Paul'
            htmlBody '<p>This is an example body</p>'
            to 'me@sergiodelamo.com'
            from 'subscribe@groovycalamari.com'
            attachment {
                filename 'test.pdf'
                filepath '/tmp/test.pdf'
                mimeType 'application/pdf'
                description 'An example pdf'
            }
        }

        then:
        transactionalEmail
        transactionalEmail.subject == 'Hi Paul'
        transactionalEmail.htmlBody == '<p>This is an example body</p>'
        transactionalEmail.sourceEmail == 'subscribe@groovycalamari.com'
        transactionalEmail.recipients == ['me@sergiodelamo.com']
        transactionalEmail.destinationEmail == 'me@sergiodelamo.com'
        transactionalEmail.attachments.size() == 1
        transactionalEmail.attachments.first().filename == 'test.pdf'
        transactionalEmail.attachments.first().filepath == '/tmp/test.pdf'
        transactionalEmail.attachments.first().mimeType == 'application/pdf'
        transactionalEmail.attachments.first().description == 'An example pdf'

        when:
         
        def f = new File(AwsSesMailerSpec.class.getResource("groovylogo.png").getFile())

        then:
        f.exists()

        when:
        transactionalEmail = AwsSesMailer.transactionalEmailWithClosure {
            subject 'Hi Paul'
            htmlBody '<p>This is an example body</p>'
            to 'me@sergiodelamo.com'
            from 'subscribe@groovycalamari.com'
            attachment {
                filepath f.absolutePath
            }
        }

        then:
        transactionalEmail
        transactionalEmail.subject == 'Hi Paul'
        transactionalEmail.htmlBody == '<p>This is an example body</p>'
        transactionalEmail.sourceEmail == 'subscribe@groovycalamari.com'
        transactionalEmail.recipients == ['me@sergiodelamo.com']
        transactionalEmail.destinationEmail == 'me@sergiodelamo.com'
        transactionalEmail.attachments.size() == 1
        transactionalEmail.attachments.first().filename == 'groovylogo.png'
        transactionalEmail.attachments.first().filepath == f.absolutePath
        transactionalEmail.attachments.first().mimeType == 'image/png'
        transactionalEmail.attachments.first().description == ''
    }

    def "test that if you try to send an unsupported attachment an exception is thrown "() {
        when:
        def subjectStr = 'GROOVY AWS SDK SES with Attachment'

        awsSesMailer.mailWithAttachment {
            subject subjectStr
            htmlBody '<p>This is an example body</p>'
            from System.getProperty('TEST_FROM_EMAIL')
            attachment {
                filepath '/temp/virus.exe'
                filename 'virus.exe'
                mimeType 'application/octet-stream'
            }
        }

        then:
        thrown UnsupportedAttachmentTypeException
    }

    @IgnoreIf({  !System.getProperty('TEST_FROM_EMAIL') ||
            !System.getProperty('TEST_INBOX_EMAIL') ||
            !System.getProperty('TEST_INBOX_PASSWORD') ||
            !System.getProperty('TEST_INBOX_HOST') ||
            !System.getProperty('TEST_INBOX_FOLDER') ||
            !System.getProperty('TEST_INBOX_PROVIDER') ||
            !System.getProperty('AWS_ACCESS_KEY') ||
            !System.getProperty('AWS_SECRET_KEY') ||
            !System.getProperty('AWS_REGION_NAME')})
    void "test AmazonSESService.mail method actually delivers an email"() {
        given:
            String accessKey = System.getProperty('AWS_ACCESS_KEY')
            String secretKey = System.getProperty('AWS_SECRET_KEY')
            String regionName = System.getProperty('AWS_REGION_NAME')
            awsSesMailer.initClient(accessKey, secretKey, regionName)

        when:
            def subjectStr = 'Groovy AWS SDK SES Subject'
            int deliveryIndicator = awsSesMailer.mail {
                to System.getProperty('TEST_INBOX_EMAIL')
                subject subjectStr
                from System.getProperty('TEST_FROM_EMAIL')
            }
        then:
            deliveryIndicator == 1

        when: " Fetch emails from the server to test the email have been delivered "
            sleep(10_000) // sleep for ten senconds to ensure the email has reached the server
            String folder = System.getProperty('TEST_INBOX_FOLDER')
            String provider = System.getProperty('TEST_INBOX_PROVIDER')
            String host = System.getProperty('TEST_INBOX_HOST')
            String email = System.getProperty('TEST_INBOX_EMAIL')
            String password = System.getProperty('TEST_INBOX_PASSWORD')
            int deleted = ReadMailService.deleteMessagesAtInboxWithSubject(subjectStr, folder, provider, host, email, password)

        then:
            deleted >= 1
    }

    @IgnoreIf({  !System.getProperty('TEST_FROM_EMAIL') ||
            !System.getProperty('TEST_INBOX_EMAIL') ||
            !System.getProperty('TEST_INBOX_HOST') ||
            !System.getProperty('TEST_INBOX_FOLDER') ||
            !System.getProperty('TEST_INBOX_PROVIDER') ||
            !System.getProperty('AWS_ACCESS_KEY') ||
            !System.getProperty('AWS_SECRET_KEY') ||
            !System.getProperty('AWS_REGION_NAME')})
    def "test send attachment"() {
        given:
            String accessKey = System.getProperty('AWS_ACCESS_KEY')
            String secretKey = System.getProperty('AWS_SECRET_KEY')
            String regionName = System.getProperty('AWS_REGION_NAME')
            awsSesMailer.initClient(accessKey, secretKey, regionName)

        when:
            def f = new File(AwsSesMailerSpec.class.getResource("groovylogo.png").getFile())

        then:
            f.exists()

        when:
            def subjectStr = 'GRAILS AWS SDK SES with Attachment'
            int deliveryIndicator = awsSesMailer.mailWithAttachment {
                subject subjectStr
                htmlBody '<p>This is an example body</p>'
                to System.getProperty('TEST_INBOX_EMAIL')
                from System.getProperty('TEST_FROM_EMAIL')
                attachment {
                    filepath f.absolutePath
                }
            }
        then:
            deliveryIndicator == 1

        when: " Fetch emails from the server to test the email have been delivered "
            sleep(10_000) // sleep for ten senconds to ensure the email has reached the server
            String folder = System.getProperty('TEST_INBOX_FOLDER')
            String provider = System.getProperty('TEST_INBOX_PROVIDER')
            String host = System.getProperty('TEST_INBOX_HOST')
            String email = System.getProperty('TEST_INBOX_EMAIL')
            String password = System.getProperty('TEST_INBOX_PASSWORD')
            int deleted = ReadMailService.deleteMessagesAtInboxWithSubject(subjectStr, folder, provider, host, email, password)

        then:
            deleted >= 1
    }
}
