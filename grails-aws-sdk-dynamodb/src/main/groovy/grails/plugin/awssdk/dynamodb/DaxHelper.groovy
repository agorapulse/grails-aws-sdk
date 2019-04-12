package grails.plugin.awssdk.dynamodb

import agorapulse.libs.awssdk.util.AwsClientUtil
import com.amazon.dax.client.dynamodbv2.AmazonDaxClientBuilder
import com.amazon.dax.client.dynamodbv2.ClientConfig
import com.amazonaws.regions.Region
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB
import groovy.transform.CompileDynamic
import groovy.transform.CompileStatic

import java.util.concurrent.TimeUnit

@CompileStatic
class DaxHelper {

    static AmazonDynamoDB buildDaxClient(String daxEndpoint, Region region, Object config, Object serviceConfig) {
        ClientConfig clientConfig = buildClientConfiguration(config, serviceConfig)
                .withRegion(region)
                .withEndpoints(daxEndpoint)

        return  AmazonDaxClientBuilder
                .standard()
                .withEndpointConfiguration(daxEndpoint)
                .withRegion(region.name)
                .withCredentials(AwsClientUtil.buildCredentials(config, serviceConfig))
                .withClientConfiguration(clientConfig)
                .build()
    }

    @CompileDynamic
    private static ClientConfig buildClientConfiguration(defaultConfig, serviceConfig) {
        Map config = [
                connectionTimeout: defaultConfig.connectionTimeout ?: 0,
                maxConnections: defaultConfig.maxConnections ?: 0,
                maxErrorRetry: defaultConfig.maxErrorRetry ?: 0,
                socketTimeout: defaultConfig.socketTimeout ?: 0,
        ]
        if (serviceConfig) {
            if (serviceConfig.connectionTimeout) config.connectionTimeout = serviceConfig.connectionTimeout
            if (serviceConfig.maxConnections) config.maxConnections = serviceConfig.maxConnections
            if (serviceConfig.maxErrorRetry) config.maxErrorRetry = serviceConfig.maxErrorRetry
            if (serviceConfig.socketTimeout) config.socketTimeout = serviceConfig.socketTimeout
        }

        ClientConfig clientConfiguration = new ClientConfig()
        if (config.connectionTimeout) clientConfiguration.withConnectTimeout(config.connectionTimeout, TimeUnit.MILLISECONDS)
        if (config.maxConnections) clientConfiguration.withMaxPendingConnectsPerHost(config.maxConnections)
        if (config.maxErrorRetry) clientConfiguration.withReadRetries(config.maxErrorRetry)
        if (config.maxErrorRetry) clientConfiguration.withReadRetries(config.maxErrorRetry)
        if (config.socketTimeout) clientConfiguration.withRequestTimeout(config.socketTimeout, TimeUnit.MILLISECONDS)
        clientConfiguration
    }

}
