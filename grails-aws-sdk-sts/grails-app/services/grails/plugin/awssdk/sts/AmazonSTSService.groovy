package grails.plugin.awssdk.sts

import agorapulse.libs.awssdk.util.AwsClientUtil
import com.amazonaws.regions.Region
import com.amazonaws.services.securitytoken.AWSSecurityTokenService
import com.amazonaws.services.securitytoken.AWSSecurityTokenServiceClient
import com.amazonaws.services.securitytoken.AWSSecurityTokenServiceClientBuilder
import com.amazonaws.services.securitytoken.model.AssumeRoleRequest
import com.amazonaws.services.securitytoken.model.AssumeRoleResult
import grails.core.GrailsApplication
import groovy.transform.stc.ClosureParams
import groovy.transform.stc.SimpleType
import groovy.util.logging.Slf4j
import org.springframework.beans.factory.InitializingBean

@Slf4j
class AmazonSTSService implements InitializingBean {

    static SERVICE_NAME = 'sts'

    GrailsApplication grailsApplication
    AWSSecurityTokenService client

    void afterPropertiesSet() throws Exception {
        // Set region
        Region region = AwsClientUtil.buildRegion(config, serviceConfig) as Region
        assert region?.isServiceSupported(SERVICE_NAME)

        // Create client
        client = AWSSecurityTokenServiceClientBuilder.standard()
                .withRegion(region.name)
                .withEndpointConfiguration(AwsClientUtil.buildEndpointConfiguration(config, serviceConfig))
                .withCredentials(AwsClientUtil.buildCredentials(config, serviceConfig))
                .withClientConfiguration(AwsClientUtil.buildClientConfiguration(config, serviceConfig))
                .build() as AWSSecurityTokenServiceClient
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