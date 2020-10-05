package grails.plugin.awssdk.sts

import com.amazonaws.services.securitytoken.AWSSecurityTokenService
import com.amazonaws.services.securitytoken.AWSSecurityTokenServiceClientBuilder
import com.amazonaws.services.securitytoken.model.AssumeRoleRequest
import com.amazonaws.services.securitytoken.model.AssumeRoleResult
import grails.core.GrailsApplication
import groovy.transform.stc.ClosureParams
import groovy.transform.stc.SimpleType
import groovy.util.logging.Slf4j
import org.springframework.beans.factory.InitializingBean

import static agorapulse.libs.awssdk.util.AwsClientBuilder.configure

@Slf4j
class AmazonSTSService implements InitializingBean {

    static SERVICE_NAME = 'sts'

    GrailsApplication grailsApplication
    AWSSecurityTokenService client

    void afterPropertiesSet() throws Exception {
        client = configure(AWSSecurityTokenServiceClientBuilder.standard(), SERVICE_NAME, config, serviceConfig).build()
    }

    def getConfig() {
        grailsApplication.config.grails?.plugin?.awssdk ?: grailsApplication.config.grails?.plugins?.awssdk
    }

    def getServiceConfig() {
        config[SERVICE_NAME]
    }

    AssumeRoleResult assumeRole(
            String sessionName,
            String roleArn,
            int durationInSeconds,
            @DelegatesTo(value = AssumeRoleRequest, strategy = Closure.DELEGATE_FIRST)
            @ClosureParams(value = SimpleType, options = 'com.amazonaws.services.securitytoken.model.AssumeRoleRequest')
            Closure additionParameters = Closure.IDENTITY
    ) {
        AssumeRoleRequest request = new AssumeRoleRequest()
                .withRoleSessionName(sessionName)
                .withRoleArn(roleArn)
                .withDurationSeconds(durationInSeconds)

        request.with additionParameters

        client.assumeRole(request)
    }
}