package grails.plugin.awssdk.kinesis

import com.amazonaws.ClientConfiguration
import com.amazonaws.auth.AWSCredentialsProvider
import com.amazonaws.auth.DefaultAWSCredentialsProviderChain
import com.amazonaws.regions.Region
import com.amazonaws.regions.ServiceAbbreviations
import com.amazonaws.services.kinesis.AmazonKinesisClient
import com.amazonaws.services.kinesis.clientlibrary.interfaces.IRecordProcessorFactory
import com.amazonaws.services.kinesis.clientlibrary.lib.worker.InitialPositionInStream
import com.amazonaws.services.kinesis.clientlibrary.lib.worker.KinesisClientLibConfiguration
import com.amazonaws.services.kinesis.clientlibrary.lib.worker.Worker
import grails.core.GrailsApplication
import grails.plugin.awssdk.AwsClientUtil
import grails.util.Environment

import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

abstract class AbstractClientService {

    static SERVICE_NAME = ServiceAbbreviations.Kinesis

    GrailsApplication grailsApplication

    KinesisClientLibConfiguration kclConfig
    String workerId

    private ExecutorService executor
    private AmazonKinesisClient kinesis

    /**
     * @param streamName
     * @param recordProcessorFactory
     * @param idleTimeBetweenReadsInMillis
     * @return
     */
    def init(String streamName,
             IRecordProcessorFactory recordProcessorFactory,
             long idleTimeBetweenReadsInMillis = KinesisClientLibConfiguration.DEFAULT_IDLETIME_BETWEEN_READS_MILLIS) {
        if (!(Environment.current in [Environment.PRODUCTION, Environment.DEVELOPMENT])) {
            // Only bootstrap the client in PROD and DEV env
            log.info "Ignoring stream client bootstraping in env ${Environment.current} for stream $streamName"
            return
        }
        if (config.kinesis?.containsKey('enabled') && !config.kinesis.enabled) {
            log.info "Ignoring stream client bootstraping (disabled in config)"
            return
        }

        if (config.accessKey && config.secretKey) {
            // For local testing (on prod/beta EC2 role is used by AWS Credentials provider chain)
            // Because KCL client does not support BasicCredentials
            Properties props = System.getProperties()
            props.setProperty('aws.accessKeyId', config.accessKey)
            props.setProperty('aws.secretKey', config.secretKey)
        }

        // Set region
        Region region = AwsClientUtil.buildRegion(config, config[SERVICE_NAME])

        // Create client
        AWSCredentialsProvider credentials = new DefaultAWSCredentialsProviderChain()
        ClientConfiguration configuration = AwsClientUtil.buildClientConfiguration(config, config[SERVICE_NAME])
        kinesis = new AmazonKinesisClient(credentials, configuration)
                .withRegion(region)

        workerId = "${InetAddress.localHost.canonicalHostName}:${UUID.randomUUID()}"
        log.info "Using Kinesis worker id: ${workerId}"

        // Configure a Kinesis client for each stream
        String clientAppName = streamName // Define DynamoDB table for checkpoints (in the future, we might have several app consuming the stream)
        String clientStreamName = streamName
        if (config?.consumerFilterKey) {
            // Local dev environment with stream prefix
            clientAppName = "${config?.consumerFilterKey}_${clientAppName}"
            List existingStreamNames = listStreamNames()
            if (!(clientStreamName in existingStreamNames)) {
                createStream(clientStreamName)
            }
        }
        kclConfig = new KinesisClientLibConfiguration(clientAppName, clientStreamName, credentials, workerId)
                .withCommonClientConfig(configuration)
                .withRegionName(region.name)
                .withInitialPositionInStream(InitialPositionInStream.LATEST)
                .withIdleTimeBetweenReadsInMillis(idleTimeBetweenReadsInMillis)

        // Create a new worker for each stream
        Worker worker = new Worker(recordProcessorFactory, kclConfig)
        try {
            log.info "Starting Kinesis worker for ${clientStreamName}"
            executor = Executors.newSingleThreadExecutor()
            executor.execute(worker)
        } catch (Throwable t) {
            log.error "Caught throwable while processing Kinesis data.", t
        }
    }

    /**
     *
     * @return
     */
    def shutdown() {
        executor.shutdown()
        try {
            executor.awaitTermination(2L, TimeUnit.SECONDS)
        }
        catch (InterruptedException e) {
        }
        executor.shutdownNow()
    }

    // PRIVATE

    private void createStream(String streamName, int shardCount = 1) {
        kinesis.createStream(streamName, shardCount)
    }

    protected def getConfig() {
        grailsApplication.config.grails?.plugin?.awssdk ?: grailsApplication.config.grails?.plugins?.awssdk
    }

    private List<String> listStreamNames() {
        kinesis.listStreams().streamNames
    }


}
