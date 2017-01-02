package com.agorapulse.awssdk

import com.amazonaws.ClientConfiguration
import com.amazonaws.Protocol
import groovy.transform.CompileStatic

@CompileStatic
class AwsSdkUtils {
    static final String DEFAULT_REGION = 'us-east-1'

    public static final String CONNECTION_TIMEOUT = 'connectionTimeout'
    public static final String MAX_CONNECTIONS = 'maxConnections'
    public static final String MAX_ERROR_RETRY = 'maxErrorRetry'
    public static final String PROTOCOL = 'protocol'
    public static final String SOCKET_TIMEOUT = 'socketTimeout'
    public static final String USER_AGENT = 'userAgent'
    public static final String PROXY_DOMAIN = 'proxyDomain'
    public static final String PROXY_HOST = 'proxyHost'
    public static final String PROXY_PASSWORD = 'proxyPassword'
    public static final String PROXY_PORT = 'proxyPort'
    public static final String PROXY_USERNAME = 'proxyUsername'
    public static final String PROXY_WORKSTATION = 'proxyWorkstation'

    static int intValueForConfigName(Map co, String propertykey, int defaultValue = 0) {
        if ( co.containsKey(propertykey) ) {
            try {
                return co.get(propertykey) as Integer
            } catch ( NumberFormatException e ) {
                return defaultValue
            }
        }
        defaultValue
    }

    static String stringValueForConfig(Map co,  String propertykey, String defaultValue = '') {
        if ( co.containsKey(propertykey) ) {
            try {
                return co.get(propertykey) as String

            } catch ( NumberFormatException e ) {
                return defaultValue
            }
        }
        defaultValue
    }

    static ClientConfiguration clientConfigurationWithMap(Map co) {
        int connectionTimeout = intValueForConfigName(co, CONNECTION_TIMEOUT)
        int maxConnections = intValueForConfigName(co, MAX_CONNECTIONS)
        int maxErrorRetry = intValueForConfigName(co, MAX_ERROR_RETRY)
        String protocol = stringValueForConfig(co, PROTOCOL)
        int socketTimeout = intValueForConfigName(co, SOCKET_TIMEOUT)
        String userAgent = stringValueForConfig(co, USER_AGENT)
        String proxyDomain = stringValueForConfig(co, PROXY_DOMAIN)
        String proxyHost = stringValueForConfig(co, PROXY_HOST)
        String proxyPassword = stringValueForConfig(co, PROXY_PASSWORD)
        int proxyPort = intValueForConfigName(co, PROXY_PORT)
        String proxyUsername = stringValueForConfig(co, PROXY_USERNAME)
        String proxyWorkstation = stringValueForConfig(co, PROXY_WORKSTATION)

        ClientConfiguration clientConfiguration = new ClientConfiguration()
        if ( connectionTimeout ) {
            clientConfiguration.connectionTimeout = connectionTimeout
        }
        if ( maxConnections ) {
            clientConfiguration.maxConnections = maxConnections
        }
        if ( maxErrorRetry ) {
            clientConfiguration.maxErrorRetry = maxErrorRetry
        }
        if ( protocol ) {
            if ( protocol.toUpperCase() == 'HTTP') {
                clientConfiguration.protocol = Protocol.HTTP
            } else {
                clientConfiguration.protocol = Protocol.HTTPS
            }
        }
        if ( socketTimeout ) {
            clientConfiguration.socketTimeout = socketTimeout
        }
        if ( userAgent ) {
            clientConfiguration.userAgent = userAgent
        }
        if ( proxyDomain ) {
            clientConfiguration.proxyDomain = proxyDomain
        }
        if ( proxyHost ) {
            clientConfiguration.proxyHost = proxyHost
        }
        if ( proxyPassword ) {
            clientConfiguration.proxyPassword = proxyPassword
        }
        if ( proxyPort ) {
            clientConfiguration.proxyPort = proxyPort
        }
        if ( proxyUsername ) {
            clientConfiguration.proxyUsername = proxyUsername
        }
        if ( proxyWorkstation ) {
            clientConfiguration.proxyWorkstation = proxyWorkstation
        }
        clientConfiguration
    }
}
