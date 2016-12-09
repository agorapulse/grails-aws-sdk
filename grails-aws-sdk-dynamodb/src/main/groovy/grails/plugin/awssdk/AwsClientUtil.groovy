package grails.plugin.awssdk

import com.amazonaws.ClientConfiguration
import com.amazonaws.Protocol
import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.auth.DefaultAWSCredentialsProviderChain
import com.amazonaws.regions.Region
import com.amazonaws.regions.RegionUtils

class AwsClientUtil {

    static final String DEFAULT_REGION = 'us-east-1'

    /**
     *
     * @param defaultConfig
     * @param serviceConfig
     * @return
     */
    static ClientConfiguration buildClientConfiguration(defaultConfig, serviceConfig) {
        Map config = [
                connectionTimeout: defaultConfig.connectionTimeout ?: 0,
                maxConnections: defaultConfig.maxConnections ?: 0,
                maxErrorRetry: defaultConfig.maxErrorRetry ?: 0,
                protocol: defaultConfig.protocol ?: '',
                socketTimeout: defaultConfig.socketTimeout ?: 0,
                userAgent: defaultConfig.userAgent ?: '',
                proxyDomain: defaultConfig.proxyDomain ?: '',
                proxyHost: defaultConfig.proxyHost ?: '',
                proxyPassword: defaultConfig.proxyPassword ?: '',
                proxyPort: defaultConfig.proxyPort ?: 0,
                proxyUsername: defaultConfig.proxyUsername ?: '',
                proxyWorkstation: defaultConfig.proxyWorkstation ?: ''
        ]
        if (serviceConfig) {
            if (serviceConfig.connectionTimeout) config.connectionTimeout = serviceConfig.connectionTimeout
            if (serviceConfig.maxConnections) config.maxConnections = serviceConfig.maxConnections
            if (serviceConfig.maxErrorRetry) config.maxErrorRetry = serviceConfig.maxErrorRetry
            if (serviceConfig.protocol) config.protocol = serviceConfig.protocol
            if (serviceConfig.socketTimeout) config.socketTimeout = serviceConfig.socketTimeout
            if (serviceConfig.userAgent) config.userAgent = serviceConfig.userAgent
            if (serviceConfig.proxyDomain) config.proxyDomain = serviceConfig.proxyDomain
            if (serviceConfig.proxyHost) config.proxyHost = serviceConfig.proxyHost
            if (serviceConfig.proxyPassword) config.proxyPassword = serviceConfig.proxyPassword
            if (serviceConfig.proxyPort) config.proxyPort = serviceConfig.proxyPort
            if (serviceConfig.proxyUsername) config.proxyUsername = serviceConfig.proxyUsername
            if (serviceConfig.proxyWorkstation) config.proxyWorkstation = serviceConfig.proxyWorkstation
        }

        ClientConfiguration clientConfiguration = new ClientConfiguration()
        if (config.connectionTimeout) clientConfiguration.connectionTimeout = config.connectionTimeout
        if (config.maxConnections) clientConfiguration.maxConnections = config.maxConnections
        if (config.maxErrorRetry) clientConfiguration.maxErrorRetry = config.maxErrorRetry
        if (config.protocol) {
            if (config.protocol.toUpperCase() == 'HTTP') clientConfiguration.protocol = Protocol.HTTP
            else clientConfiguration.protocol = Protocol.HTTPS
        }
        if (config.socketTimeout) clientConfiguration.socketTimeout = config.socketTimeout
        if (config.userAgent) clientConfiguration.userAgent = config.userAgent
        if (config.proxyDomain) clientConfiguration.proxyDomain = config.proxyDomain
        if (config.proxyHost) clientConfiguration.proxyHost = config.proxyHost
        if (config.proxyPassword) clientConfiguration.proxyPassword = config.proxyPassword
        if (config.proxyPort) clientConfiguration.proxyPort = config.proxyPort
        if (config.proxyUsername) clientConfiguration.proxyUsername = config.proxyUsername
        if (config.proxyWorkstation) clientConfiguration.proxyWorkstation = config.proxyWorkstation
        clientConfiguration
    }

    /**
     *
     * @param defaultConfig
     * @param serviceConfig
     * @return
     */
    static buildCredentials(defaultConfig, serviceConfig) {
        Map config = [
                accessKey: defaultConfig.accessKey ?: '',
                secretKey: defaultConfig.secretKey ?: ''
        ]
        if (serviceConfig) {
            if (serviceConfig.accessKey) config.accessKey = serviceConfig.accessKey
            if (serviceConfig.secretKey) config.secretKey = serviceConfig.secretKey
        }

        if (!config.accessKey || !config.secretKey) {
            new DefaultAWSCredentialsProviderChain()
        } else {
            new BasicAWSCredentials(config.accessKey, config.secretKey)
        }
    }

    /**
     *
     * @param defaultConfig
     * @param serviceConfig
     */
    static buildRegion(defaultConfig, serviceConfig) {
        String regionName = DEFAULT_REGION
        if (serviceConfig?.region) {
            regionName = serviceConfig.region
        } else if (defaultConfig?.region) {
            regionName = defaultConfig.region
        }
        RegionUtils.getRegion(regionName)
    }


}
