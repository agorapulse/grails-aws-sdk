package grails.plugin.awssdk.sns

import agorapulse.libs.awssdk.util.AwsClientUtil
import com.amazonaws.ClientConfiguration
import com.amazonaws.regions.Region
import com.amazonaws.services.sns.AmazonSNS
import com.amazonaws.services.sns.AmazonSNSClient
import com.amazonaws.services.sns.model.*
import grails.core.GrailsApplication
import groovy.json.JsonOutput
import groovy.util.logging.Slf4j
import org.springframework.beans.factory.InitializingBean

import java.util.regex.Matcher
import java.util.regex.Pattern

@Slf4j
class AmazonSNSService implements InitializingBean  {

    static SERVICE_NAME = AmazonSNS.ENDPOINT_PREFIX
    static MOBILE_PLATFORM_ANDROID = 'android'
    static MOBILE_PLATFORM_IOS = 'ios'

    GrailsApplication grailsApplication
    AmazonSNSClient client
    
    void afterPropertiesSet() throws Exception {
        // Set region
        Region region = AwsClientUtil.buildRegion(config, serviceConfig)
        assert region?.isServiceSupported(SERVICE_NAME)

        // Create client
        def credentials = AwsClientUtil.buildCredentials(config, serviceConfig)
        ClientConfiguration configuration = AwsClientUtil.buildClientConfiguration(config, serviceConfig)
        client = new AmazonSNSClient(credentials, configuration)
                .withRegion(region)
    }

    /**
     *
     * @param deviceToken
     * @param customUserData
     * @return
     */
    String registerAndroidDevice(String deviceToken,
                                 String customUserData = '') {
        String platformApplicationArn = serviceConfig?.android?.applicationArn
        assert platformApplicationArn, 'Android application arn must be defined in config'
        createPlatformEndpoint(
                platformApplicationArn,
                deviceToken,
                customUserData
        )
    }

    /**
     *
     * @param deviceToken
     * @param customUserData
     * @return
     */
    String registerIosDevice(String deviceToken,
                             String customUserData = '') {
        String platformApplicationArn = serviceConfig?.ios?.applicationArn
        assert platformApplicationArn, 'Ios application arn must be defined in config'
        createPlatformEndpoint(
                platformApplicationArn,
                deviceToken,
                customUserData
        )
    }

    /**
     *
     * @param platformType
     * @param deviceToken
     * @param customUserData
     * @return
     */
    String registerDevice(String platformType,
                          String deviceToken,
                          String customUserData = '') {
        switch(platformType) {
            case MOBILE_PLATFORM_ANDROID:
                registerAndroidDevice(deviceToken, customUserData)
                break
            case MOBILE_PLATFORM_IOS:
                registerIosDevice(deviceToken, customUserData)
                break
        }
    }

    /**
     *
     * @param endpointArn
     * @param notification
     * @param collapseKey
     * @param delayWhileIdle
     * @param timeToLive
     * @param dryRun
     * @return
     */
    String sendAndroidAppNotification(String endpointArn,
                                      Map notification,
                                      String collapseKey,
                                      boolean delayWhileIdle = true,
                                      int timeToLive = 125,
                                      boolean dryRun = false) {

        String message = buildAndroidMessage(
                notification,
                collapseKey,
                delayWhileIdle,
                timeToLive,
                dryRun
        )
        publish(
                endpointArn,
                'GCM',
                message
        )
    }

    /**
     *
     * @param endpointArn
     * @param notification
     * @param sandbox
     * @return
     */
    String sendIosAppNotification(String endpointArn,
                                  Map notification,
                                  boolean sandbox = false) {
        String message = buildIosMessage(notification)
        publish(
                endpointArn,
                sandbox ? 'APNS_SANDBOX' : 'APNS',
                message
        )
    }

    /**
     *
     * @param endpointArn
     * @param deviceToken
     * @param customUserData
     * @return
     */
    String validateAndroidDevice(String endpointArn,
                                 String deviceToken,
                                 String customUserData = '') {
        String platformApplicationArn = serviceConfig?.android?.applicationArn
        assert platformApplicationArn, 'Android application arn must be defined in config'
        validatePlatformDeviceToken(
                platformApplicationArn,
                MOBILE_PLATFORM_ANDROID,
                endpointArn,
                deviceToken,
                customUserData
        )
    }

    /**
     *
     * @param endpointArn
     * @param deviceToken
     * @param customUserData
     * @return
     */
    String validateIosDevice(String endpointArn,
                             String deviceToken,
                             String customUserData = '') {
        String platformApplicationArn = serviceConfig?.ios?.applicationArn
        assert platformApplicationArn, 'Ios application arn must be defined in config'
        validatePlatformDeviceToken(
                platformApplicationArn,
                MOBILE_PLATFORM_IOS,
                endpointArn,
                deviceToken,
                customUserData
        )
    }

    /**
     *
     * @param platformType
     * @param endpointArn
     * @param deviceToken
     * @param customUserData
     * @return
     */
    String validateDevice(String platformType,
                          String endpointArn,
                          String deviceToken,
                          String customUserData = '') {
        switch(platformType) {
            case MOBILE_PLATFORM_ANDROID:
                validateAndroidDevice(endpointArn, deviceToken, customUserData)
                break
            case MOBILE_PLATFORM_IOS:
                validateIosDevice(endpointArn, deviceToken, customUserData)
                break
        }
    }

    /**
     *
     * @param endpointArn
     */
    void unregisterDevice(String endpointArn) {
        deleteEndpoint(endpointArn)
    }

    // PRIVATE

    def getConfig() {
        grailsApplication.config.grails?.plugin?.awssdk ?: grailsApplication.config.grails?.plugins?.awssdk
    }

    def getServiceConfig() {
        config[SERVICE_NAME]
    }

    private String buildAndroidMessage(Map data,
                                       String collapseKey,
                                       boolean delayWhileIdle = true,
                                       int timeToLive = 125,
                                       boolean dryRun = false) {
        // Note: we don't use JSON converters here because there required grails-plugin-converters, which required servlet classes
        JsonOutput.toJson([
                collapse_key: collapseKey,
                data: data,
                delay_while_idle: delayWhileIdle,
                time_to_live: timeToLive,
                dry_run: dryRun
        ])
    }

    private String buildIosMessage(Map data) {
        // Note: we don't use JSON converters here because there required grails-plugin-converters, which required servlet classes
        JsonOutput.toJson([
                aps: data
        ])
    }

    private String createPlatformEndpoint(String platformApplicationArn,
                                          String deviceToken,
                                          String customUserData = '') {
        String endpointArn
        try {
            log.debug("Creating platform endpoint with token " + deviceToken)
            CreatePlatformEndpointRequest request = new CreatePlatformEndpointRequest()
                    .withPlatformApplicationArn(platformApplicationArn)
                    .withToken(deviceToken)
            if (customUserData) {
                request.customUserData = customUserData
            }
            CreatePlatformEndpointResult result = client.createPlatformEndpoint(request)
            endpointArn = result.endpointArn
        } catch (InvalidParameterException ipe) {
            String message = ipe.errorMessage
            log.debug("Exception message: " + message)
            Pattern p = Pattern.compile(".*Endpoint (arn:aws:sns[^ ]+) already exists with the same Token.*")
            Matcher m = p.matcher(message)
            if (m.matches()) {
                // The platform endpoint already exists for this token, but with additional custom data that
                // createEndpoint doesn't want to overwrite. Just use the existing platform endpoint.
                endpointArn = m.group(1)
            } else {
                // Rethrow the exception, the input is actually bad.
                throw ipe
            }
        }
        endpointArn
    }

    private DeleteEndpointResult deleteEndpoint(String endpointArn) {
        DeleteEndpointRequest depReq = new DeleteEndpointRequest()
                .withEndpointArn(endpointArn)
        client.deleteEndpoint(depReq)
    }

    private GetEndpointAttributesResult getEndpointAttributes(String endpointArn) {
        GetEndpointAttributesRequest geaReq = new GetEndpointAttributesRequest().withEndpointArn(endpointArn)
        client.getEndpointAttributes(geaReq)
    }

    private SetEndpointAttributesResult setEndpointAttributes(String endpointArn,
                                                              Map attributes) {
        SetEndpointAttributesRequest saeReq = new SetEndpointAttributesRequest()
                .withEndpointArn(endpointArn)
                .withAttributes(attributes)
        client.setEndpointAttributes(saeReq)
    }

    private String publish(String endpointArn,
                           String platform,
                           String message) {
        // Note: we don't use JSON converters here because there required grails-plugin-converters, which required servlet classes
        PublishRequest request = new PublishRequest(
                message: JsonOutput.toJson([(platform): message]),
                messageStructure: 'json',
                targetArn: endpointArn, // For direct publish to mobile end points, topicArn is not relevant.
        )

        PublishResult result = null
        try {
            result = client.publish(request)
        } catch (Exception e) {
            log.error 'An exception was catched while publishing',  e
        }
        result ? result.messageId : ''
    }

    private String validatePlatformDeviceToken(String platformApplicationArn,
                                               String platformType,
                                               String endpointArn,
                                               String deviceToken,
                                               String customUserData = '') {
        boolean updateRequired = false

        log.debug "Retrieving platform endpoint data..."
        // Look up the platform endpoint and make sure the data in it is current, even if it was just created.
        try {
            GetEndpointAttributesResult result = getEndpointAttributes(endpointArn)
            updateRequired = !result.attributes.get("Token").equals(deviceToken) || !result.attributes.get("Enabled").equalsIgnoreCase("true")
        } catch (NotFoundException nfe) {
            // We had a stored ARN, but the platform endpoint associated with it disappeared. Recreate it.
            endpointArn = createPlatformEndpoint(platformApplicationArn, deviceToken, customUserData)
        }

        if (updateRequired) {
            log.debug "Platform endpoint update required..."
            if ((platformType == MOBILE_PLATFORM_IOS && endpointArn.contains('GCM')) ||
                    (platformType == MOBILE_PLATFORM_ANDROID && endpointArn.contains('APNS'))) {
                log.debug "Switching between IOS and ANDROID platforms..."
                // Manager switched device between and android and an IOS device
                deleteEndpoint(endpointArn)
                endpointArn = createPlatformEndpoint(platformApplicationArn, deviceToken, customUserData)
            } else {
                // The platform endpoint is out of sync with the current data, update the token and enable it.
                log.debug("Updating platform endpoint " + endpointArn)
                try {
                    setEndpointAttributes(endpointArn, [
                            Token: deviceToken,
                            Enabled: 'true'
                    ])
                } catch (InvalidParameterException ipe) {
                    deleteEndpoint(endpointArn)
                    endpointArn = createPlatformEndpoint(platformApplicationArn, deviceToken, customUserData)
                }
            }
        }
        endpointArn
    }

    /**
     * @param topicName
     * @return
     */
    String createTopic(String topicName){

        log.debug("Creating topic sns with name " + topicName)
        CreateTopicRequest createTopicRequest = new CreateTopicRequest(topicName);

        try {
            CreateTopicResult createTopicResult = client.createTopic(createTopicRequest);
            createTopicResult.topicArn
        }catch (Exception e){
            log.error 'An exception was catched while creating a topic',  e
        }

    }

    /**
     * @param topicArn
     */
    void deleteTopic(String topicArn){
        log.debug("Deleting topic sns with arn " + topicArn)
        DeleteTopicRequest deleteTopicRequest = new DeleteTopicRequest(topicArn);

        try {
            client.deleteTopic(deleteTopicRequest)
        }catch (Exception e){
            log.error 'An exception was catched while creating a topic',  e
        }
    }

    /**
     * @param topic
     * @param protocol
     * @param endpoint
     * @return
     */
    String subscribeTopic(String topic,String protocol,String endpoint){

        log.debug("Creating a topic subscription to endpoit " + endpoint)
        SubscribeRequest subRequest = new SubscribeRequest(topic, protocol, endpoint);

        try{
            SubscribeResult result = client.subscribe(subRequest);
            result.subscriptionArn
        }catch (Exception e){
            log.error 'An exception was catched while subscribe a topic',  e
        }

    }

    /**
     * @param arn
     */
    void unsubscribeTopic(String arn){

        log.debug("Deleting a topic subscription to number " + arn)
        UnsubscribeRequest unSubRequest = new UnsubscribeRequest(arn);

        try{
            client.unsubscribe(unSubRequest);
        }catch (Exception e){
            log.error 'An exception was catched while unsubscribe a topic',  e
        }
    }

    /**
     * @param topicArn
     * @param number
     * @return
     */
     String subscribeTopicWithSMS(String topicArn,String number){
        try{
            subscribeTopic(topicArn,'sms',number)
        }catch (Exception e){
            log.error 'An exception was catched while subscribe a topic with sms protocol',  e
        }
    }

    /**
     * @param topicArn
     * @param subject
     * @param message
     * @return
     */
     String publishTopic(String topicArn,String subject,String message){
        PublishRequest publishRequest = new PublishRequest(topicArn, message,subject);
        try{
            PublishResult publishResult = client.publish(publishRequest);
            publishResult.messageId
        }catch (Exception e){
            log.error 'An exception was catched while publishing',  e
        }
    }





}
