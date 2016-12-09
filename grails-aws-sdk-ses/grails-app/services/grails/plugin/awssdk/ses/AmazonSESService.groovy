package grails.plugin.awssdk.ses

import com.agorapulse.awssdk.ses.AwsSesMailer
import com.agorapulse.awssdk.ses.TransactionalEmail
import com.agorapulse.awssdk.ses.UnsupportedAttachmentTypeException
import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.regions.Region
import com.amazonaws.regions.RegionUtils
import com.amazonaws.services.simpleemail.AmazonSimpleEmailService
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClient
import grails.config.Config
import grails.core.support.GrailsConfigurationAware
import grails.plugin.awssdk.AwsClientUtil
import grails.util.Environment
import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j

@CompileStatic
@Slf4j
class AmazonSESService implements GrailsConfigurationAware {

    static String SERVICE_NAME = AmazonSimpleEmailService.ENDPOINT_PREFIX
    String sourceEmail
    String subjectPrefix
    String templatePath
    AwsSesMailer mailer = new AwsSesMailer()

    @Override
    void setConfiguration(Config co) {
        final String defaultConfig = 'grails.plugin.awssdk'
        final String serviceConfig = "${defaultConfig}.ses"

        sourceEmail = co.getProperty('grails.plugin.awssdk.ses.sourceEmail', String)
        subjectPrefix = co.getProperty('grails.plugin.awssdk.ses.subjectPrefix', String)
        templatePath = co.getProperty('grails.plugin.awssdk.ses.templatePath', String, '/templates/email')

        String accessKey = AwsClientUtil.stringValueForConfig(co, 'accessKey', serviceConfig, defaultConfig, null)
        String secretKey = AwsClientUtil.stringValueForConfig(co, 'secretKey', serviceConfig, defaultConfig, null)
        String regionName = AwsClientUtil.stringValueForConfig(co, 'region', serviceConfig, defaultConfig, AwsClientUtil.DEFAULT_REGION)

        if ( !accessKey || !secretKey || !regionName ) {
            log.error('you must define at least AWS accessKey, secretKey and region to use this plugin')
            return
        }

        Region region = RegionUtils.getRegion(regionName)
        if ( !region?.isServiceSupported(SERVICE_NAME) ) {
            log.error("${SERVICE_NAME} is not supported in region $regionName")
            return
        }

        def credentials = new BasicAWSCredentials(accessKey, secretKey)
        def clientConfiguration = AwsClientUtil.clientConfigurationWithConfig(co, defaultConfig, serviceConfig)
        mailer.client = new AmazonSimpleEmailServiceClient(credentials, clientConfiguration)
                .withRegion(region)
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
}
