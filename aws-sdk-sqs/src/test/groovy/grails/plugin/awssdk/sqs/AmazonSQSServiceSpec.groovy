package grails.plugin.awssdk.sqs

import com.amazonaws.AmazonClientException
import com.amazonaws.AmazonServiceException
import com.amazonaws.services.sqs.AmazonSQSClient
import com.amazonaws.services.sqs.model.GetQueueAttributesResult
import grails.test.mixin.TestFor
import spock.lang.Specification

@TestFor(AmazonSQSService)
class AmazonSQSServiceSpec extends Specification {

    void setup() {
        // Mock collaborator
        service.client = Mock(AmazonSQSClient)
    }

    /*
     * Tests for createQueue(String queueName)
     */

    def "Create queue"() {
        when:
        String queueUrl = service.createQueue('someQueue')

        then:
        1 * service.client.createQueue(_) >> ['queueUrl': 'somepath/queueName']
        queueUrl
        queueUrl == 'somepath/queueName'
    }

    /*
     * Tests for deleteMessage(String queueUrl, String receiptHandle)
     */

    def "Delete message"() {
        given:
        service.queueUrlByNames['queueName'] = 'somepath/queueName'

        when:
        service.deleteMessage('queueName', 'receiptHandle')

        then:
        1 * service.client.deleteMessage(_)
    }

    /*
     * Tests for listQueueNames()
     */

    def "List queue names"() {
        when:
        List queueNames = service.listQueueNames()

        then:
        1 * service.client.listQueues(_) >> ['queueUrls': ['somepath/queueName1', 'somepath/queueName2', 'somepath/queueName3']]
        queueNames
        queueNames.size() == 3
        queueNames == ['queueName1', 'queueName2', 'queueName3']

    }

    /*
     * Tests for listQueueUrls()
     */

    def "List queue urls"() {
        when:
        List queueUrls = service.listQueueUrls()

        then:
        1 * service.client.listQueues(_) >> ['queueUrls': ['somepath/queueName1', 'somepath/queueName2', 'somepath/queueName3']]
        queueUrls
        queueUrls.size() == 3
        queueUrls == ['somepath/queueName1', 'somepath/queueName2', 'somepath/queueName3']
    }

    /*
     * Tests for getQueueAttributes(String queueUrl)
     */

    def "Get queue attributes"() {
        given:
        service.queueUrlByNames['queueName'] = 'somepath/queueName'

        when:
        Map attributes = service.getQueueAttributes('queueName')

        then:
        1 * service.client.getQueueAttributes(_) >> {
            GetQueueAttributesResult attrResult = new GetQueueAttributesResult()
            attrResult.setAttributes(['attribute1': 'value1', 'attribute2': 'value2'])
            attrResult
        }
        attributes
        attributes.size() == 2
    }

    def "Get queue attributes service exception"() {
        given:
        service.queueUrlByNames['queueName'] = 'somepath/queueName'

        when:
        Map attributes = service.getQueueAttributes('queueName')

        then:
        1 * service.client.getQueueAttributes(_) >> {
            AmazonServiceException exception = new AmazonServiceException('Error')
            exception.setErrorCode('AWS.SimpleQueueService.NonExistentQueue')
            throw exception
        }
        !attributes
        old(service.queueUrlByNames.size() == 1)
        service.queueUrlByNames.size() == 0
    }

    def "Get queue attributes client exception"() {
        given:
        service.queueUrlByNames['queueName'] = 'somepath/queueName'

        when:
        Map attributes = service.getQueueAttributes('queueName')

        then:
        1 * service.client.getQueueAttributes(_) >> { throw new AmazonClientException('Error') }
        !attributes
    }

    /*
     * Tests for getQueueUrl()
     */

    def "Get queue url with autocreate"() {
        when:
        String queueUrl = service.getQueueUrl('queueName', true)

        then:
        service.client.listQueues(_) >> ['queueUrls': ['somepath/queueName1', 'somepath/queueName2', 'somepath/queueName3']]
        service.client.createQueue(_) >> ['queueUrl': 'somepath/queueName']
        service.client
        queueUrl == 'somepath/queueName'
        old(service.queueUrlByNames.size() == 0)
        service.queueUrlByNames.size() == 4
        service.queueUrlByNames['queueName'] == 'somepath/queueName'
    }

    /*
     * Tests for receiveMessages(String queueUrl, int maxNumberOfMessages = 0, int visibilityTimeout = 0, int waitTimeSeconds = 0)
     */

    def "Receive messages"() {
        given:
        service.queueUrlByNames['queueName'] = 'somepath/queueName'

        when:
        List messages = service.receiveMessages('queueName', 1, 1, 1)

        then:
        1 * service.client.receiveMessage(_) >> ['messages': ['message1', 'message2']]
        messages
        messages.size() == 2
    }

    /*
     * Tests for sendMessage(String queueUrl, String messageBody)
     */

    def "Send messages"() {
        given:
        service.queueUrlByNames['queueName'] = 'somepath/queueName'

        when:
        String messageId = service.sendMessage('queueName', 'messageBody')

        then:
        1 * service.client.sendMessage(_) >> ['messageId': 'msg_id']
        messageId == 'msg_id'
    }

}
