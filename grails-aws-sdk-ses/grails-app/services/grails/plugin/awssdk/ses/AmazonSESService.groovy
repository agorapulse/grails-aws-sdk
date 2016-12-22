package grails.plugin.awssdk.ses

import com.agorapulse.awssdk.ses.AwsSesMailer
import com.agorapulse.awssdk.ses.TransactionalEmail
import com.agorapulse.awssdk.ses.UnsupportedAttachmentTypeException
import com.amazonaws.ClientConfiguration
import com.amazonaws.regions.Region
import com.amazonaws.services.simpleemail.AmazonSimpleEmailService
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClient
import grails.core.GrailsApplication
import grails.plugin.awssdk.AwsClientUtil
import grails.util.Environment
import groovy.util.logging.Slf4j
import org.springframework.beans.factory.InitializingBean

@Slf4j
class AmazonSESService implements InitializingBean {

    static String SERVICE_NAME = AmazonSimpleEmailService.ENDPOINT_PREFIX

    GrailsApplication grailsApplication
    String sourceEmail
    String subjectPrefix
    String templatePath
    AwsSesMailer mailer = new AwsSesMailer()

    void afterPropertiesSet() throws Exception {
        // Set region
        Region region = AwsClientUtil.buildRegion(config, serviceConfig)
        assert region?.isServiceSupported(SERVICE_NAME)

        // Create client
        def credentials = AwsClientUtil.buildCredentials(config, serviceConfig)
        ClientConfiguration configuration = AwsClientUtil.buildClientConfiguration(config, serviceConfig)
        mailer.client = new AmazonSimpleEmailServiceClient(credentials, configuration)
                .withRegion(region)

        sourceEmail = serviceConfig?.sourceEmail ?: ''
        subjectPrefix = serviceConfig?.subjectPrefix ?: ''
        templatePath = serviceConfig?.templatePath ?: '/templates/email'
    }

    private String preffixSubject(String subject) {
        // Prefix email subject for DEV and BETA environment
        if (Environment.current != Environment.PRODUCTION) {
            subject = "[${Environment.current}] $subject"

        } else if ( subjectPrefix ) {
            subject = "${subjectPrefix} $subject"
        }
        subject
    }

    /**
     * @return 1 if successful, 0 if not sent, -1 if blacklisted
     */
    int mail(@DelegatesTo(TransactionalEmail) Closure composer) throws Exception {
        def transactionalEmail = AwsSesMailer.transactionalEmailWithClosure(composer)
        transactionalEmail.subject = preffixSubject(transactionalEmail.subject)
        mailer.send(transactionalEmail.destinationEmail,
                transactionalEmail.subject,
                transactionalEmail.htmlBody,
                transactionalEmail.sourceEmail,
                transactionalEmail.replyToEmail)
    }

    /**
     *
     * @param destinationEmail
     * @param subject
     * @param htmlBody
     * @param sourceEmail
     * @param replyToEmail
     * @return 1 if successful, 0 if not sent, -1 if blacklisted
     */
    int send(String destinationEmail,
             String subject,
             String htmlBody,
             String sourceEmail = '',
             String replyToEmail = '') {
        if (!sourceEmail) {
            assert this.sourceEmail, "Default sourceEmail must be set in config"
            sourceEmail = this.sourceEmail
        }
        subject = preffixSubject(subject)
        mailer.send(destinationEmail, subject, htmlBody, sourceEmail, replyToEmail)
    }


    int mailWithAttachment(@DelegatesTo(TransactionalEmail) Closure composer) throws UnsupportedAttachmentTypeException {
        def transactionalEmail = AwsSesMailer.transactionalEmailWithClosure(composer)
        transactionalEmail.subject = preffixSubject(transactionalEmail.subject)
        mailer.sendEmailWithAttachment(transactionalEmail)
    }

    int sendEmailWithAttachment(TransactionalEmail transactionalEmail) throws UnsupportedAttachmentTypeException {
        transactionalEmail.subject = preffixSubject(transactionalEmail.subject)
        mailer.sendEmailWithAttachment(transactionalEmail)
    }

    // PRIVATE

    def getConfig() {
        grailsApplication.config.grails?.plugin?.awssdk ?: grailsApplication.config.grails?.plugins?.awssdk
    }

    def getServiceConfig() {
        config['ses']
    }

}
