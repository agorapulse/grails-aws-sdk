package grails.plugin.awssdk.ses

import com.amazonaws.AmazonClientException
import com.amazonaws.AmazonServiceException
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClient
import grails.test.mixin.TestFor
import spock.lang.Specification
import static com.agorapulse.awssdk.ses.AwsSdkSesEmailDeliveryStatus.*


@TestFor(AmazonSESService)
class AmazonSESServiceSpec extends Specification {

    static doWithConfig(conf) {
        conf.grails.plugin.awssdk.ses.sourceEmail = 'foo@domain.com'
    }

    void setup() {
        // Mock collaborator
        service.mailer.client = Mock(AmazonSimpleEmailServiceClient)
    }

    void setUpSesInteractions(Map parameters = [:]) {
        if (parameters.serviceException) {
            1 * service.mailer.client.sendEmail(_) >> {
                throw new AmazonServiceException(parameters.serviceException)
            }
        } else if (parameters.clientException) {
            1 * service.mailer.client.sendEmail(_) >> {
                throw new AmazonClientException(parameters.clientException)
            }
        } else {
            1 * service.mailer.client.sendEmail(_) >> [messageId: 'someMessageId']
        }
    }

    /**
     * Tests for sendEmail(String destinationEmail, String subject, String htmlBody, String sourceEmail = '', String replyToEmail = '')
     */
    void "Send email"() {
        setup:
        setUpSesInteractions()

        when:
        int statusId = service.send('destination@email.com', 'Some subject', 'Some html body', 'source@email.com')

        then:
        statusId == STATUS_DELIVERED
    }

    void "Send email with source email"() {
        setup:
        setUpSesInteractions()

        when:
        int statusId = service.send('destination@email.com', 'Some subject', 'Some html body', 'source@email.com')

        then:
        statusId == STATUS_DELIVERED
    }

    void "Send email with reply email"() {
        setup:
        setUpSesInteractions()

        when:
        int statusId = service.send('destination@email.com', 'Some subject', 'Some html body', 'source@email.com', 'reply@email.com')

        then:
        statusId == STATUS_DELIVERED
    }

    void "Send email with blacklisted service exception"() {
        setup:
        setUpSesInteractions(serviceException: "Address blacklisted")

        when:
        int statusId = service.send('destination@email.com', 'Some subject', 'Some html body', 'source@email.com')

        then:
        statusId == STATUS_BLACKLISTED
    }

    void "Send email with unverified email service exception"() {
        setup:
        setUpSesInteractions(serviceException: "Email address is not verified")

        when:
        int statusId = service.send('destination@email.com', 'Some subject', 'Some html body', 'source@email.com')

        then:
        statusId == STATUS_NOT_DELIVERED
    }

    void "Send email with unknown service exception"() {
        setup:
        setUpSesInteractions(serviceException: "Unknown")

        when:
        int statusId = service.send('destination@email.com', 'Some subject', 'Some html body', 'source@email.com')

        then:
        statusId == STATUS_NOT_DELIVERED
    }

    void "Send email with unknown client exception"() {
        setup:
        setUpSesInteractions(clientException: "Unknown")

        when:
        int statusId = service.send('destination@email.com', 'Some subject', 'Some html body', 'source@email.com')

        then:
        statusId == STATUS_NOT_DELIVERED
    }



}
