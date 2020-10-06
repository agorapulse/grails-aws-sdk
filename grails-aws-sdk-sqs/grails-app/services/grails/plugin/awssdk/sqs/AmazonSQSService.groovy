package grails.plugin.awssdk.sqs


import com.amazonaws.AmazonClientException
import com.amazonaws.AmazonServiceException
import com.amazonaws.services.sqs.AmazonSQS
import com.amazonaws.services.sqs.AmazonSQSClientBuilder
import com.amazonaws.services.sqs.model.*
import grails.core.GrailsApplication
import groovy.transform.Synchronized
import groovy.util.logging.Slf4j
import org.springframework.beans.factory.InitializingBean

import static agorapulse.libs.awssdk.util.AwsClientUtil.configure

@Slf4j
class AmazonSQSService implements InitializingBean  {

    static SERVICE_NAME = AmazonSQS.ENDPOINT_PREFIX

    GrailsApplication grailsApplication
    AmazonSQS client
    private String defaultQueueName = ''

    private Map queueUrlByNames = [:]
    static private String getQueueNameFromUrl(String queueUrl) {
        queueUrl.tokenize('/').last()
    }

    void afterPropertiesSet() throws Exception {
        // Create client
        client = configure(AmazonSQSClientBuilder.standard(), SERVICE_NAME, config, serviceConfig).build()

        defaultQueueName = serviceConfig?.queue ?: ''
    }

    protected void init(String queueName) {
        defaultQueueName = queueName
    }

    /**
     *
     * @param queueName
     * @return
     */
    String createQueue(String queueName, Map<String, String> attributesOverrides = [:]) {
        CreateQueueRequest createQueueRequest = new CreateQueueRequest(queueName)
        if (serviceConfig?.delaySeconds) {
            createQueueRequest.attributes['DelaySeconds'] = serviceConfig.delaySeconds.toString()
        }
        if (serviceConfig?.messageRetentionPeriod) {
            createQueueRequest.attributes['MessageRetentionPeriod'] = serviceConfig.messageRetentionPeriod.toString()
        }
        if (serviceConfig?.maximumMessageSize) {
            createQueueRequest.attributes['MaximumMessageSize'] = serviceConfig.maximumMessageSize.toString()
        }
        if (serviceConfig?.visibilityTimeout) {
            createQueueRequest.attributes['VisibilityTimeout'] = serviceConfig.visibilityTimeout.toString()
        }
        if (serviceConfig?.fifo) {
            createQueueRequest.attributes['FifoQueue'] = serviceConfig.fifo.toString()
        }

        createQueueRequest.attributes.putAll(attributesOverrides)

        String queueUrl = client.createQueue(createQueueRequest).queueUrl
        log.debug "Queue created (queueUrl=$queueUrl)"
        addQueue(queueUrl)
        queueUrl
    }

    /**
     *
     * @param queueUrl
     * @param receiptHandle
     */
    void deleteMessage(String queueName,
                       String receiptHandle) {
        String queueUrl = getQueueUrl(queueName)
        client.deleteMessage(new DeleteMessageRequest(queueUrl, receiptHandle))
        log.debug "Message deleted (queueUrl=$queueUrl)"
    }

    /**
     *
     * @param receiptHandle
     */

    void deleteMessage(String receiptHandle) {
        assertDefaultQueueName()
        deleteMessage(defaultQueueName, receiptHandle)
    }

    /**
     *
     * @param queueName
     */
    void deleteQueue(String queueName) {
        String queueUrl = getQueueUrl(queueName)
        client.deleteQueue(queueUrl)
        removeQueue(queueUrl)
    }

    /**
     *
     * @param queueName
     * @return
     */
    Map getQueueAttributes(String queueName) {
        String queueUrl = getQueueUrl(queueName)

        GetQueueAttributesRequest getQueueAttributesRequest = new GetQueueAttributesRequest(queueUrl).withAttributeNames(['All'])
        Map attributes = [:]
        try {
            attributes = client.getQueueAttributes(getQueueAttributesRequest).attributes
        } catch(AmazonServiceException exception) {
            if (exception.errorCode == 'AWS.SimpleQueueService.NonExistentQueue') {
                removeQueue(queueUrl)
            }
            log.warn 'An amazon service exception was catched while getting queue attributes', exception
        } catch (AmazonClientException exception) {
            log.warn 'An amazon client exception was catched while getting queue attributes', exception
        }
        attributes
    }

    /**
     *
     * @return
     */
    Map getQueueAttributes() {
        assertDefaultQueueName()
        getQueueAttributes(defaultQueueName)
    }

    /**
     *
     * @param jobName
     * @param autoCreate
     * @return
     */
    String getQueueUrl(String queueName) {
        if (queueName.startsWith('http://') || queueName.startsWith('https://')) {
            return queueName
        }
        if (serviceConfig?.queueNamePrefix) {
            queueName = serviceConfig.queueNamePrefix + queueName
        }
        if (!queueUrlByNames) {
            loadQueues()
        }

        String queueUrl = queueUrlByNames[queueName]
        if (!queueUrl && (serviceConfig?.autoCreateQueue)) {
            queueUrl = createQueue(queueName)
        }
        if (!queueUrl) {
            throw new AmazonSQSException("Queue ${queueName} not found")
        }
        queueUrl
    }

    /**
     *
     * @return
     */
    String getQueueUrl() {
        assertDefaultQueueName()
        getQueueUrl(defaultQueueName)
    }

    /**
     *
     * @param reload
     * @return
     */
    List<String> listQueueNames(boolean reload = false) {
        if (!queueUrlByNames || reload) {
            loadQueues()
        }
        queueUrlByNames?.keySet().sort()
    }

    /**
     *
     * @param reload
     * @return
     */
    List<String> listQueueUrls(boolean reload = false) {
        if (!queueUrlByNames || reload) {
            loadQueues()
        }
        queueUrlByNames?.values().sort()
    }

    /**
     *
     * @param queueName
     * @param maxNumberOfMessages
     * @param visibilityTimeout
     * @param waitTimeSeconds
     * @return
     */
    List receiveMessages(String queueName,
                         int maxNumberOfMessages = 1,
                         int visibilityTimeout = 0,
                         int waitTimeSeconds = 0) {
        String queueUrl = getQueueUrl(queueName)

        ReceiveMessageRequest receiveMessageRequest = new ReceiveMessageRequest(queueUrl)
        if (maxNumberOfMessages) {
            receiveMessageRequest.maxNumberOfMessages = maxNumberOfMessages
        }
        if (visibilityTimeout) {
            receiveMessageRequest.visibilityTimeout = visibilityTimeout
        }
        if (waitTimeSeconds) {
            receiveMessageRequest.waitTimeSeconds = waitTimeSeconds
        }
        List messages = client.receiveMessage(receiveMessageRequest).messages
        log.debug "Messages received (count=${messages.size()})"
        messages
    }

    /**
     *
     * @param maxNumberOfMessages
     * @param visibilityTimeout
     * @param waitTimeSeconds
     * @return
     */
    List receiveMessages(int maxNumberOfMessages = 1,
                         int visibilityTimeout = 0,
                         int waitTimeSeconds = 0) {
        assertDefaultQueueName()
        receiveMessages(defaultQueueName, maxNumberOfMessages, visibilityTimeout, waitTimeSeconds)
    }

    /**
     * 
     * @param queueName
     * @param messageBody
     * @param delaySeconds
     * @return
     */
    String sendMessage(String queueName,
                       String messageBody,
                       Integer delaySeconds = 0) {
        String queueUrl = getQueueUrl(queueName)

        SendMessageRequest request = new SendMessageRequest(queueUrl, messageBody)
        if (delaySeconds) {
            request.delaySeconds = delaySeconds
        }
        String messageId = client.sendMessage(request).messageId
        log.debug "Message sent (messageId=$messageId)"
        messageId
    }

    /**
     *
     * @param messageBody
     * @return
     */
    String sendMessage(String messageBody,
                       @DelegatesTo(value = SendMessageRequest, strategy = Closure.DELEGATE_FIRST)
                       Closure messageConfiguration
    ) {
        assertDefaultQueueName()
        sendMessage(defaultQueueName, messageBody, messageConfiguration)
    }

    /**
     *
     * @param queueName
     * @param messageBody
     * @param delaySeconds
     * @return
     */
    String sendMessage(String queueName,
                       String messageBody,
                       @DelegatesTo(value = SendMessageRequest, strategy = Closure.DELEGATE_FIRST)
                       Closure messageConfiguration
    ) {
        String queueUrl = getQueueUrl(queueName)

        SendMessageRequest request = new SendMessageRequest(queueUrl, messageBody)

        request.with messageConfiguration

        String messageId = client.sendMessage(request).messageId
        log.debug "Message sent (messageId=$messageId)"
        messageId
    }

    /**
     *
     * @param messageBody
     * @return
     */
    String sendMessage(String messageBody) {
        assertDefaultQueueName()
        sendMessage(defaultQueueName, messageBody)
    }

    // PRIVATE

    boolean assertDefaultQueueName() {
        assert defaultQueueName, "Default queue must be defined"
    }

    def getConfig() {
        grailsApplication.config.grails?.plugin?.awssdk ?: grailsApplication.config.grails?.plugins?.awssdk
    }

    def getServiceConfig() {
        config[SERVICE_NAME]
    }

    @Synchronized
    private void addQueue(String queueUrl) {
        assert queueUrl, "Invalid queueUrl"
        queueUrlByNames[getQueueNameFromUrl(queueUrl)] = queueUrl
    }

    @Synchronized
    private void loadQueues() {
        ListQueuesRequest listQueuesRequest = new ListQueuesRequest()
        if (serviceConfig?.queueNamePrefix) {
            listQueuesRequest.queueNamePrefix = serviceConfig.queueNamePrefix
        }
        List queueUrls = client.listQueues(listQueuesRequest).queueUrls
        queueUrlByNames = queueUrls?.collectEntries { queueUrl ->
            [getQueueNameFromUrl(queueUrl), queueUrl]
        }

    }

    @Synchronized
    private void removeQueue(String queueUrl) {
        assert queueUrl, "Invalid queueUrl"
        queueUrlByNames.remove(getQueueNameFromUrl(queueUrl))
    }

}
