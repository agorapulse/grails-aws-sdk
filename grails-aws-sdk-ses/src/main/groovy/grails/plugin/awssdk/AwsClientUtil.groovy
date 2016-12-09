package grails.plugin.awssdk

import com.amazonaws.ClientConfiguration
import com.amazonaws.Protocol
import grails.config.Config
import groovy.transform.CompileStatic

@CompileStatic
class AwsClientUtil {

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

    static int intValueForConfigName(Config co, String configName, final String serviceConfig,  final String defaultConfig, int defaultValue = 0) {
        def propertykey = "${serviceConfig}.${configName}" as String
        if ( co.getProperty(propertykey, Integer, null) ) {
            return co.getProperty(propertykey, Integer, null).intValue()
        }
        propertykey = "${defaultConfig}.${configName}" as String
        if (co.getProperty(propertykey, Integer, null) ) {
            return  co.getProperty(propertykey, Integer, null).intValue()
        }
        defaultValue
    }

    static String stringValueForConfig(Config co,  String configName,  final String serviceConfig, final String defaultConfig, String defaultValue = '') {
        def propertyKey = "${serviceConfig}.${configName}" as String
        if ( co.getProperty(propertyKey, String, null) ) {
            return co.getProperty(propertyKey, String, null)
        }
        propertyKey = "${defaultConfig}.${configName}" as String
        if (co.getProperty(propertyKey, String, null) ) {
            return co.getProperty(propertyKey, String, null)
        }
        defaultValue
    }

    static ClientConfiguration clientConfigurationWithConfig(Config co, final String defaultConfig, final String serviceConfig) {
        int connectionTimeout = intValueForConfigName(co, CONNECTION_TIMEOUT, serviceConfig, defaultConfig)
        int maxConnections = intValueForConfigName(co, MAX_CONNECTIONS, serviceConfig,  defaultConfig)
        int maxErrorRetry = intValueForConfigName(co, MAX_ERROR_RETRY, serviceConfig, defaultConfig)
        String protocol = stringValueForConfig(co, PROTOCOL, serviceConfig, defaultConfig)
        int socketTimeout = intValueForConfigName(co, SOCKET_TIMEOUT, serviceConfig, defaultConfig)
        String userAgent = stringValueForConfig(co, USER_AGENT, serviceConfig, defaultConfig)
        String proxyDomain = stringValueForConfig(co, PROXY_DOMAIN, serviceConfig, defaultConfig)
        String proxyHost = stringValueForConfig(co, PROXY_HOST, serviceConfig, defaultConfig)
        String proxyPassword = stringValueForConfig(co, PROXY_PASSWORD, serviceConfig, defaultConfig)
        int proxyPort = intValueForConfigName(co, PROXY_PORT, serviceConfig, defaultConfig)
        String proxyUsername = stringValueForConfig(co, PROXY_USERNAME, serviceConfig, defaultConfig)
        String proxyWorkstation = stringValueForConfig(co, PROXY_WORKSTATION, serviceConfig, defaultConfig)

        ClientConfiguration clientConfiguration = new ClientConfiguration()
        if (connectionTimeout){
            clientConfiguration.connectionTimeout = connectionTimeout
        }
        if (maxConnections) {
            clientConfiguration.maxConnections = maxConnections
        }
        if (maxErrorRetry) {
            clientConfiguration.maxErrorRetry = maxErrorRetry
        }
        if (protocol) {
            if ( protocol.toUpperCase() == 'HTTP') {
                clientConfiguration.protocol = Protocol.HTTP
            } else {
                clientConfiguration.protocol = Protocol.HTTPS
            }
        }
        if (socketTimeout) clientConfiguration.socketTimeout = socketTimeout
        if (userAgent) clientConfiguration.userAgent = userAgent
        if (proxyDomain) clientConfiguration.proxyDomain = proxyDomain
        if (proxyHost) clientConfiguration.proxyHost = proxyHost
        if (proxyPassword) clientConfiguration.proxyPassword = proxyPassword
        if (proxyPort) clientConfiguration.proxyPort = proxyPort
        if (proxyUsername) clientConfiguration.proxyUsername = proxyUsername
        if (proxyWorkstation) clientConfiguration.proxyWorkstation = proxyWorkstation
        clientConfiguration
    }



}
