package grails.plugin.awssdk.sts

import com.amazonaws.services.securitytoken.AWSSecurityTokenService
import com.amazonaws.services.securitytoken.model.AssumeRoleRequest
import spock.lang.Specification

import java.util.concurrent.atomic.AtomicReference

class AmazonSTSServiceSpec extends Specification {

    public static final String SESSION_NAME = 'session'
    public static final String ROLE_ARN = 'arn:::my-role'
    public static final int DURATION_SECONDS = 360
    public static final String POLICY_STRING = '{ "policy" : "string" }'

    AWSSecurityTokenService client = Mock()

    AmazonSTSService service = new AmazonSTSService(client: client)

    void 'assume role'() {
        given:
            AtomicReference<AssumeRoleRequest> requestReference = new AtomicReference<>()
        when:
            service.assumeRole(SESSION_NAME, ROLE_ARN, DURATION_SECONDS) {
                policy = POLICY_STRING
            }
        then:
            requestReference.get()
            requestReference.get().policy == POLICY_STRING
            requestReference.get().roleSessionName == SESSION_NAME
            requestReference.get().roleArn == ROLE_ARN
            requestReference.get().durationSeconds == DURATION_SECONDS

            1 * client.assumeRole(_) >> { AssumeRoleRequest request -> requestReference.set(request) }
    }

}
