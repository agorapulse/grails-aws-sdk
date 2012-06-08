package grails.plugins.awssdk

import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.ClientConfiguration
import com.amazonaws.Protocol

class AmazonBaseService {

    String defaultRegion = 'us-east-1'

    protected BasicAWSCredentials basicAWSCredentials
    protected ClientConfiguration clientConfiguration
    protected Map clients = [:]

    public AmazonBaseService(String accessKey = '', String secretKey = '', Map config = [:]) {
        setCredentials(accessKey, secretKey)
        setClientConfiguration(config)
    }

    void setCredentials(String accessKey = '', String secretKey = '') {
        basicAWSCredentials = new BasicAWSCredentials(accessKey, secretKey)
    }

    void setCredentials(Map credentials) {
        basicAWSCredentials = new BasicAWSCredentials(credentials.accessKey, credentials.secretKey)
    }

    void setClientConfiguration(Map config) {
        clientConfiguration = new ClientConfiguration()
        if (config.connectionTimeout) clientConfiguration.connectionTimeout = config.connectionTimeout
        if (config.maxConnections) clientConfiguration.maxConnections = config.maxConnections
        if (config.maxErrorRetry) clientConfiguration.maxErrorRetry = config.maxErrorRetry
        if (config.protocol) {
            if (config.protocol.toUpperCase() == 'HTTP' ) clientConfiguration.protocol = Protocol.HTTP
            else clientConfiguration.protocol = Protocol.HTTPS
        }
        if (config.socketTimeout) clientConfiguration.socketTimeout = config.socketTimeout
        if (config.userAgent) clientConfiguration.userAgent = config.userAgent
    }

}
