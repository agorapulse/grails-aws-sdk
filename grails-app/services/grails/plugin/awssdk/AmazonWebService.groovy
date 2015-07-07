package grails.plugin.awssdk

import com.amazonaws.AmazonWebServiceClient
import com.amazonaws.ClientConfiguration
import com.amazonaws.Protocol
import com.amazonaws.auth.AWSCredentials
import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.auth.DefaultAWSCredentialsProviderChain
import com.amazonaws.regions.Region
import com.amazonaws.regions.RegionUtils
import com.amazonaws.regions.Regions
import com.amazonaws.regions.ServiceAbbreviations
import com.amazonaws.services.autoscaling.AmazonAutoScalingAsyncClient
import com.amazonaws.services.autoscaling.AmazonAutoScalingClient
import com.amazonaws.services.cloudformation.AmazonCloudFormationAsyncClient
import com.amazonaws.services.cloudformation.AmazonCloudFormationClient
import com.amazonaws.services.cloudfront.AmazonCloudFrontAsyncClient
import com.amazonaws.services.cloudfront.AmazonCloudFrontClient
import com.amazonaws.services.cloudsearchdomain.AmazonCloudSearchDomainAsyncClient
import com.amazonaws.services.cloudsearchdomain.AmazonCloudSearchDomainClient
import com.amazonaws.services.cloudsearchv2.AmazonCloudSearchAsyncClient
import com.amazonaws.services.cloudsearchv2.AmazonCloudSearchClient
import com.amazonaws.services.cloudtrail.AWSCloudTrailAsyncClient
import com.amazonaws.services.cloudtrail.AWSCloudTrailClient
import com.amazonaws.services.cloudwatch.AmazonCloudWatchAsyncClient
import com.amazonaws.services.cloudwatch.AmazonCloudWatchClient
import com.amazonaws.services.codedeploy.AmazonCodeDeployAsyncClient
import com.amazonaws.services.codedeploy.AmazonCodeDeployClient
import com.amazonaws.services.cognitoidentity.AmazonCognitoIdentityAsyncClient
import com.amazonaws.services.cognitoidentity.AmazonCognitoIdentityClient
import com.amazonaws.services.cognitosync.AmazonCognitoSyncAsyncClient
import com.amazonaws.services.cognitosync.AmazonCognitoSyncClient
import com.amazonaws.services.config.AmazonConfigAsyncClient
import com.amazonaws.services.config.AmazonConfigClient
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBAsyncClient
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper
import com.amazonaws.services.ec2.AmazonEC2AsyncClient
import com.amazonaws.services.ec2.AmazonEC2Client
import com.amazonaws.services.elasticache.AmazonElastiCacheAsyncClient
import com.amazonaws.services.elasticache.AmazonElastiCacheClient
import com.amazonaws.services.elasticbeanstalk.AWSElasticBeanstalkAsyncClient
import com.amazonaws.services.elasticbeanstalk.AWSElasticBeanstalkClient
import com.amazonaws.services.elasticloadbalancing.AmazonElasticLoadBalancingAsyncClient
import com.amazonaws.services.elasticloadbalancing.AmazonElasticLoadBalancingClient
import com.amazonaws.services.elasticmapreduce.AmazonElasticMapReduceAsyncClient
import com.amazonaws.services.elasticmapreduce.AmazonElasticMapReduceClient
import com.amazonaws.services.elastictranscoder.AmazonElasticTranscoderAsyncClient
import com.amazonaws.services.elastictranscoder.AmazonElasticTranscoderClient
import com.amazonaws.services.glacier.AmazonGlacierAsyncClient
import com.amazonaws.services.glacier.AmazonGlacierClient
import com.amazonaws.services.identitymanagement.AmazonIdentityManagementAsyncClient
import com.amazonaws.services.identitymanagement.AmazonIdentityManagementClient
import com.amazonaws.services.importexport.AmazonImportExportAsyncClient
import com.amazonaws.services.importexport.AmazonImportExportClient
import com.amazonaws.services.kinesis.AmazonKinesisAsyncClient
import com.amazonaws.services.kinesis.AmazonKinesisClient
import com.amazonaws.services.kms.AWSKMSAsyncClient
import com.amazonaws.services.kms.AWSKMSClient
import com.amazonaws.services.opsworks.AWSOpsWorksAsyncClient
import com.amazonaws.services.opsworks.AWSOpsWorksClient
import com.amazonaws.services.rds.AmazonRDSAsyncClient
import com.amazonaws.services.rds.AmazonRDSClient
import com.amazonaws.services.redshift.AmazonRedshiftAsyncClient
import com.amazonaws.services.redshift.AmazonRedshiftClient
import com.amazonaws.services.route53.AmazonRoute53AsyncClient
import com.amazonaws.services.route53.AmazonRoute53Client
import com.amazonaws.services.s3.AmazonS3Client
import com.amazonaws.services.s3.AmazonS3EncryptionClient
import com.amazonaws.services.s3.model.EncryptionMaterials
import com.amazonaws.services.s3.transfer.TransferManager
import com.amazonaws.services.securitytoken.AWSSecurityTokenServiceAsyncClient
import com.amazonaws.services.securitytoken.AWSSecurityTokenServiceClient
import com.amazonaws.services.simpledb.AmazonSimpleDBAsyncClient
import com.amazonaws.services.simpledb.AmazonSimpleDBClient
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceAsyncClient
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClient
import com.amazonaws.services.simpleworkflow.AmazonSimpleWorkflowAsyncClient
import com.amazonaws.services.simpleworkflow.AmazonSimpleWorkflowClient
import com.amazonaws.services.sns.AmazonSNSAsyncClient
import com.amazonaws.services.sns.AmazonSNSClient
import com.amazonaws.services.sqs.AmazonSQSAsyncClient
import com.amazonaws.services.sqs.AmazonSQSClient
import com.amazonaws.services.sqs.buffered.AmazonSQSBufferedAsyncClient
import com.amazonaws.services.storagegateway.AWSStorageGatewayAsyncClient
import com.amazonaws.services.storagegateway.AWSStorageGatewayClient

import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class AmazonWebService {

    static final String DEFAULT_REGION = 'us-east-1'
    static final int DEFAULT_THREAD_POOL_SIZE = 50

    static transactional = false

    def grailsApplication

    private Map asyncClients = [:]
    private Map dynamoDBMappers = [:]
    private Map clients = [:]
    private Map transferManagers = [:]

    AmazonAutoScalingAsyncClient getAutoScalingAsync(regionName = '') {
        getServiceClient('autoScaling', regionName, true) as AmazonAutoScalingAsyncClient
    }

    AmazonAutoScalingClient getAutoScaling(regionName = '') {
        getServiceClient('autoScaling', regionName) as AmazonAutoScalingClient
    }

    AmazonCodeDeployAsyncClient getCodeDeployAsync(regionName = '') {
        getServiceClient('codeDeploy', regionName, true) as AmazonCodeDeployAsyncClient
    }

    AmazonCloudFormationAsyncClient getCloudFormationAsync(regionName = '') {
        getServiceClient('cloudFormation', regionName, true) as AmazonCloudFormationAsyncClient
    }

    AmazonCloudFormationClient getCloudFormation(regionName = '') {
        getServiceClient('cloudFormation', regionName) as AmazonCloudFormationClient
    }

    AmazonCloudFrontAsyncClient getCloudFrontAsync() {
        getServiceClient('cloudFront', '', true) as AmazonCloudFrontAsyncClient
    }

    AmazonCloudFrontClient getCloudFront() {
        getServiceClient('cloudFront') as AmazonCloudFrontClient
    }

    AmazonCloudSearchAsyncClient getCloudSearchAsync(regionName = '') {
        getServiceClient('cloudSearch', regionName, true) as AmazonCloudSearchAsyncClient
    }

    AmazonCloudSearchClient getCloudSearch(regionName = '') {
        getServiceClient('cloudSearch', regionName) as AmazonCloudSearchClient
    }

    AmazonCloudSearchDomainAsyncClient getCloudSearchDomainAsync(regionName = '') {
        getServiceClient('cloudSearchDomain', regionName, true) as AmazonCloudSearchDomainAsyncClient
    }

    AmazonCloudSearchDomainClient getCloudSearchDomain(regionName = '') {
        getServiceClient('cloudSearchDomain', regionName) as AmazonCloudSearchDomainClient
    }

    AWSCloudTrailAsyncClient getCloudTrailAsync(regionName = '') {
        getServiceClient('cloudTrail', regionName, true) as AWSCloudTrailAsyncClient
    }

    AWSCloudTrailClient getCloudTrail(regionName = '') {
        getServiceClient('cloudTrail', regionName) as AWSCloudTrailClient
    }

    AmazonCloudWatchAsyncClient getCloudWatchAsync(regionName = '') {
        getServiceClient('cloudWatch', regionName, true) as AmazonCloudWatchAsyncClient
    }

    AmazonCloudWatchClient getCloudWatch(regionName = '') {
        getServiceClient('cloudWatch', regionName) as AmazonCloudWatchClient
    }

    AmazonCodeDeployClient getCodeDeploy(regionName = '') {
        getServiceClient('codeDeploy', regionName) as AmazonCodeDeployClient
    }

    AmazonCognitoIdentityAsyncClient getCognitoIdentityAsync(regionName = '') {
        getServiceClient('cognitoIdentity', regionName, true) as AmazonCognitoIdentityAsyncClient
    }

    AmazonCognitoIdentityClient getCognitoIdentity(regionName = '') {
        getServiceClient('cognitoIdentity', regionName) as AmazonCognitoIdentityClient
    }

    AmazonCognitoSyncAsyncClient getCognitoSyncAsync(regionName = '') {
        getServiceClient('cognitoSync', regionName, true) as AmazonCognitoSyncAsyncClient
    }

    AmazonCognitoSyncClient getCognitoSync(regionName = '') {
        getServiceClient('cognitoSync', regionName) as AmazonCognitoSyncClient
    }

    AmazonConfigAsyncClient getConfigAsync(regionName = '') {
        getServiceClient('config', regionName, true) as AmazonConfigAsyncClient
    }

    AmazonConfigClient getConfig(regionName = '') {
        getServiceClient('config', regionName) as AmazonConfigClient
    }

    AmazonDynamoDBAsyncClient getDynamoDBAsync(regionName = '') {
        getServiceClient('dynamoDB', regionName, true) as AmazonDynamoDBAsyncClient
    }

    AmazonDynamoDBClient getDynamoDB(regionName = '') {
        getServiceClient('dynamoDB', regionName) as AmazonDynamoDBClient
    }

    AmazonEC2AsyncClient getEc2Async(regionName = '') {
        getServiceClient('ec2', regionName, true) as AmazonEC2AsyncClient
    }

    AmazonEC2Client getEc2(regionName = '') {
        getServiceClient('ec2', regionName) as AmazonEC2Client
    }

    AWSElasticBeanstalkAsyncClient getElasticBeanstalkAsync(regionName = '') {
        getServiceClient('elasticBeanstalk', regionName, true) as AWSElasticBeanstalkAsyncClient
    }

    AWSElasticBeanstalkClient getElasticBeanstalk(regionName = '') {
        getServiceClient('elasticBeanstalk', regionName) as AWSElasticBeanstalkClient
    }

    AmazonElastiCacheAsyncClient getElastiCacheAsync(regionName = '') {
        getServiceClient('elastiCache', regionName, true) as AmazonElastiCacheAsyncClient
    }

    AmazonElastiCacheClient getElastiCache(regionName = '') {
        getServiceClient('elastiCache', regionName) as AmazonElastiCacheClient
    }

    AmazonElasticLoadBalancingAsyncClient getElasticLoadBalancingAsync(regionName = '') {
        getServiceClient('elasticLoadBalancing', regionName, true) as AmazonElasticLoadBalancingAsyncClient
    }

    AmazonElasticLoadBalancingClient getElasticLoadBalancing(regionName = '') {
        getServiceClient('elasticLoadBalancing', regionName) as AmazonElasticLoadBalancingClient
    }

    AmazonElasticMapReduceAsyncClient getElasticMapReduceAsync(regionName = '') {
        getServiceClient('elasticMapReduce', regionName, true) as AmazonElasticMapReduceAsyncClient
    }

    AmazonElasticMapReduceClient getElasticMapReduce(region = '') {
        getServiceClient('elasticMapReduce', region) as AmazonElasticMapReduceClient
    }

    AmazonElasticTranscoderClient getElasticTranscoderAsync(regionName = '') {
        getServiceClient('elasticTranscoder', regionName, true) as AmazonElasticTranscoderAsyncClient
    }

    AmazonElasticTranscoderClient getElasticTranscoder(regionName = '') {
        getServiceClient('elasticTranscoder', regionName) as AmazonElasticTranscoderClient
    }

    AmazonGlacierClient getGlacierAsync(regionName = '') {
        getServiceClient('glacier', regionName, true) as AmazonGlacierAsyncClient
    }

    AmazonGlacierClient getGlacier(regionName = '') {
        getServiceClient('glacier', regionName) as AmazonGlacierClient
    }

    AmazonIdentityManagementAsyncClient getIamAsync() {
        getServiceClient('iam', '', true) as AmazonIdentityManagementAsyncClient
    }

    AmazonIdentityManagementClient getIam() {
        getServiceClient('iam') as AmazonIdentityManagementClient
    }

    AmazonImportExportAsyncClient getImportExportAsync() {
        getServiceClient('importExport', '', true) as AmazonImportExportAsyncClient
    }

    AmazonImportExportClient getImportExport() {
        getServiceClient('importExport') as AmazonImportExportClient
    }

    AmazonKinesisAsyncClient getKinesisAsync(regionName = '') {
        getServiceClient('kinesis', regionName, true) as AmazonKinesisAsyncClient
    }

    AmazonKinesisClient getKinesis(regionName = '') {
        getServiceClient('kinesis', regionName) as AmazonKinesisClient
    }

    AWSKMSAsyncClient getKMSAsync(regionName = '') {
        getServiceClient('kms', regionName, true) as AWSKMSAsyncClient
    }

    AWSKMSClient getKMS(regionName = '') {
        getServiceClient('kms', regionName) as AWSKMSClient
    }

    AWSOpsWorksAsyncClient getOpsWorksAsync() {
        getServiceClient('opsWorks', '', true) as AWSOpsWorksAsyncClient
    }

    AWSOpsWorksClient getOpsWorks() {
        getServiceClient('opsWorks', '') as AWSOpsWorksClient
    }

    AmazonRDSAsyncClient getRdsAsync(regionName = '') {
        getServiceClient('rds', regionName, true) as AmazonRDSAsyncClient
    }

    AmazonRDSClient getRds(regionName = '') {
        getServiceClient('rds', regionName) as AmazonRDSClient
    }

    AmazonRedshiftAsyncClient getRedshiftAsync() {
        getServiceClient('redshift', '', true) as AmazonRedshiftAsyncClient
    }

    AmazonRedshiftClient getRedshift() {
        getServiceClient('redshift') as AmazonRedshiftClient
    }

    AmazonRoute53AsyncClient getRoute53Async() {
        getServiceClient('route53', '', true) as AmazonRoute53AsyncClient
    }

    AmazonRoute53Client getRoute53() {
        getServiceClient('route53') as AmazonRoute53Client
    }

    AmazonS3Client getS3(String regionName = '') {
        getServiceClient('s3', regionName) as AmazonS3Client
    }

    AmazonS3EncryptionClient getS3Encryption(String regionName = '') {
        getServiceClient('s3Encryption', regionName) as AmazonS3EncryptionClient
    }

    AWSSecurityTokenServiceClient getSts(String regionName = '') {
        getServiceClient('sts', regionName) as AWSSecurityTokenServiceClient
    }

    AWSSecurityTokenServiceAsyncClient getStsAsync(String regionName = '') {
        getServiceClient('sts', regionName, true) as AWSSecurityTokenServiceAsyncClient
    }

    AmazonSimpleDBAsyncClient getSdbAsync(String regionName = '') {
        getServiceClient('sdb', regionName, true) as AmazonSimpleDBAsyncClient
    }

    AmazonSimpleDBClient getSdb(String regionName = '') {
        getServiceClient('sdb', regionName) as AmazonSimpleDBClient
    }

    AmazonSimpleEmailServiceAsyncClient getSesAsync(String regionName = '') {
        getServiceClient('ses', regionName, true) as AmazonSimpleEmailServiceAsyncClient
    }

    AmazonSimpleEmailServiceClient getSes(String regionName = '') {
        getServiceClient('ses', regionName) as AmazonSimpleEmailServiceClient
    }

    AmazonSNSAsyncClient getSnsAsync(String regionName = '') {
        getServiceClient('sns', regionName, true) as AmazonSNSAsyncClient
    }

    AmazonSNSClient getSns(String regionName = '') {
        getServiceClient('sns', regionName) as AmazonSNSClient
    }

    AmazonSQSAsyncClient getSqsAsync(String regionName = '') {
        getServiceClient('sqs', regionName, true) as AmazonSQSAsyncClient
    }

    AmazonSQSBufferedAsyncClient getSqsBufferedAsync(String regionName = '') {
        AmazonSQSAsyncClient sqsAsyncClient = getSqsAsync(regionName)
        new AmazonSQSBufferedAsyncClient(sqsAsyncClient)
    }

    AmazonSQSClient getSqs(String regionName = '') {
        getServiceClient('sqs', regionName) as AmazonSQSClient
    }

    AWSStorageGatewayAsyncClient getStorageGatewayAsync(String regionName = '') {
        getServiceClient('storageGateway', regionName, true) as AWSStorageGatewayAsyncClient
    }

    AWSStorageGatewayClient getStorageGateway(String regionName = '') {
        getServiceClient('storageGateway', regionName) as AWSStorageGatewayClient
    }

    AmazonSimpleWorkflowAsyncClient getSwfAsync(String regionName = '') {
        getServiceClient('swf', regionName, true) as AmazonSimpleWorkflowAsyncClient
    }

    AmazonSimpleWorkflowClient getSwf(String regionName = '') {
        getServiceClient('swf', regionName) as AmazonSimpleWorkflowClient
    }

    // Utils

    DynamoDBMapper getDynamoDBMapper(String regionName = '') {
        if (!regionName) {
            if (awsConfig['dynamodb']?.region) regionName = awsConfig['dynamodb'].region
            else if (awsConfig?.region) regionName = awsConfig.region
            else regionName = DEFAULT_REGION
        }

        if (!dynamoDBMappers[regionName]) {
            dynamoDBMappers[regionName] = new DynamoDBMapper(getDynamoDB(regionName))
        }
        dynamoDBMappers[regionName]
    }

    TransferManager getTransferManager(String regionName = '') {
        if (!regionName) {
            if (awsConfig['s3']?.region) regionName = awsConfig['s3'].region
            else if (awsConfig?.region) regionName = awsConfig.region
            else regionName = DEFAULT_REGION
        }

        if (!transferManagers[regionName]) {
            transferManagers[regionName] = new TransferManager(getS3(regionName))
        }
        transferManagers[regionName]
    }

    // PRIVATE

    private getAwsConfig() {
        grailsApplication.config.grails?.plugin?.awssdk
    }

    private buildCredentials(defaultConfig, serviceConfig) {
        Map config = [
                accessKey: defaultConfig.accessKey ?: '',
                secretKey: defaultConfig.secretKey ?: ''
        ]
        if (serviceConfig) {
            if (serviceConfig.accessKey) config.accessKey = serviceConfig.accessKey
            if (serviceConfig.secretKey) config.secretKey = serviceConfig.secretKey
        }

        AWSCredentials credentials = new BasicAWSCredentials(config.accessKey, config.secretKey)

        if (!credentials.AWSAccessKeyId || !credentials.AWSSecretKey) {
            return new DefaultAWSCredentialsProviderChain()
        }

        credentials
    }

    private ClientConfiguration buildClientConfiguration(defaultConfig, serviceConfig) {
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

    private AmazonWebServiceClient getServiceClient(String service, String regionName = '', Boolean async = false) {
        if (!regionName) {
            if (awsConfig[service]?.region) regionName = awsConfig[service].region
            else if (awsConfig?.region) regionName = awsConfig.region
            else regionName = DEFAULT_REGION
        }

        Region region = RegionUtils.getRegion(regionName)
        String serviceName = getServiceAbbreviation(service)
        if (!region || !region.isServiceSupported(serviceName)) {
            if (!region) {
                log.warn "Region ${regionName} not found"
            } else if (!region.isServiceSupported(serviceName)) {
                log.warn "Service ${service} is not supported in region ${regionName}"
            }
            log.warn "Using default region ${DEFAULT_REGION}"
            regionName = DEFAULT_REGION
            region = RegionUtils.getRegion(regionName)
        }

        def clientsCache = async ? asyncClients : clients
        if (!clientsCache[service]) clientsCache[service] = [:]

        if (!clientsCache[service].containsKey(regionName)) {
            AmazonWebServiceClient client
            def credentials = buildCredentials(awsConfig, awsConfig[service])

            ClientConfiguration configuration = buildClientConfiguration(awsConfig, awsConfig[service])
            ExecutorService executorService = Executors.newFixedThreadPool(DEFAULT_THREAD_POOL_SIZE)

            switch (service) {
                case 'autoScaling':
                    client = async ? new AmazonAutoScalingAsyncClient(credentials, configuration, executorService) : new AmazonAutoScalingClient(credentials, configuration)
                    break
                case 'cognitoIdentity':
                    client = async ? new AmazonCognitoIdentityAsyncClient(credentials, configuration, executorService) : new AmazonCognitoIdentityClient(credentials, configuration)
                    break
                case 'cognitoSync':
                    client = async ? new AmazonCognitoSyncAsyncClient(credentials, configuration, executorService) : new AmazonCognitoSyncClient(credentials, configuration)
                    break
                case 'cloudFormation':
                    client = async ? new AmazonCloudFormationAsyncClient(credentials, configuration, executorService) : new AmazonCloudFormationClient(credentials, configuration)
                    break
                case 'cloudFront':
                    client = async ? new AmazonCloudFrontAsyncClient(credentials, configuration, executorService) : new AmazonCloudFrontClient(credentials, configuration)
                    break
                case 'cloudSearch':
                    client = async ? new AmazonCloudSearchAsyncClient(credentials, configuration, executorService) : new AmazonCloudSearchClient(credentials, configuration)
                    break
                case 'cloudSearchDomain':
                    client = async ? new AmazonCloudSearchDomainAsyncClient(credentials, configuration, executorService) : new AmazonCloudSearchDomainClient(credentials, configuration)
                    break
                case 'cloudTrail':
                    client = async ? new AWSCloudTrailAsyncClient(credentials, configuration, executorService) : new AWSCloudTrailClient(credentials, configuration)
                    break
                case 'cloudWatch':
                    client = async ? new AmazonCloudWatchAsyncClient(credentials, configuration, executorService) : new AmazonCloudWatchClient(credentials, configuration)
                    break
                case 'codeDeploy':
                    client = async ? new AmazonCodeDeployAsyncClient(credentials, configuration, executorService) : new AmazonCodeDeployClient(credentials, configuration)
                    break
                case 'config':
                    client = async ? new AmazonConfigAsyncClient(credentials, configuration, executorService) : new AmazonConfigClient(credentials, configuration)
                    break
                case 'dynamoDB':
                    client = async ? new AmazonDynamoDBAsyncClient(credentials, configuration, executorService) : new AmazonDynamoDBClient(credentials, configuration)
                    break
                case 'ec2':
                    client = async ? new AmazonEC2AsyncClient(credentials, configuration, executorService) : new AmazonEC2Client(credentials, configuration)
                    break
                case 'elasticBeanstalk':
                    client = async ? new AWSElasticBeanstalkAsyncClient(credentials, configuration, executorService) : new AWSElasticBeanstalkClient(credentials, configuration)
                    break
                case 'elastiCache':
                    client = async ? new AmazonElastiCacheAsyncClient(credentials, configuration, executorService) : new AmazonElastiCacheClient(credentials, configuration)
                    break
                case 'elasticLoadBalancing':
                    client = async ? new AmazonElasticLoadBalancingAsyncClient(credentials, configuration, executorService) : new AmazonElasticLoadBalancingClient(credentials, configuration)
                    break
                case 'elasticMapReduce':
                    client = async ? new AmazonElasticMapReduceAsyncClient(credentials, configuration, executorService) : new AmazonElasticMapReduceClient(credentials, configuration)
                    break
                case 'elasticTranscoder':
                    client = async ? new AmazonElasticTranscoderAsyncClient(credentials, configuration, executorService) : new AmazonElasticTranscoderClient(credentials, configuration)
                    break
                case 'glacier':
                    client = async ? new AmazonGlacierAsyncClient(credentials, configuration, executorService) : new AmazonGlacierClient(credentials, configuration)
                    break
                case 'iam':
                    client = async ? new AmazonIdentityManagementAsyncClient(credentials, configuration, executorService) : new AmazonIdentityManagementClient(credentials, configuration)
                    break
                case 'importExport':
                    client = async ? new AmazonImportExportAsyncClient(credentials, configuration, executorService) : new AmazonImportExportClient(credentials, configuration)
                    break
                case 'kinesis':
                    client = async ? new AmazonKinesisAsyncClient(credentials, configuration, executorService) : new AmazonKinesisClient(credentials, configuration)
                    break
                case 'kms':
                    client = async ? new AWSKMSAsyncClient(credentials, configuration, executorService) : new AWSKMSClient(credentials, configuration)
                    break
                case 'opsWorks':
                    client = async ? new AWSOpsWorksAsyncClient(credentials, configuration, executorService) : new AWSOpsWorksClient(credentials, configuration)
                    break
                case 'rds':
                    client = async ? new AmazonRDSAsyncClient(credentials, configuration, executorService) : new AmazonRDSClient(credentials, configuration)
                    break
                case 'redshift':
                    client = async ? new AmazonRedshiftAsyncClient(credentials, configuration, executorService) : new AmazonRedshiftClient(credentials, configuration)
                    break
                case 'route53':
                    client = async ? new AmazonRoute53AsyncClient(credentials, configuration, executorService) : new AmazonRoute53Client(credentials, configuration)
                    break
                case 's3':
                    if (async) throw new Exception("Sorry, there is no async client for AmazonS3")
                    client = new AmazonS3Client(credentials, configuration)
                    break
                case 's3Encryption':
                    if (async) throw new Exception("Sorry, there is no async client for AmazonS3EncryptionClient")
                    if (!awsConfig.encryptionMaterials) throw new Exception("Sorry, please provide entryptionMaterials in AWS config")
                    client = new AmazonS3EncryptionClient(credentials, awsConfig.encryptionMaterials as EncryptionMaterials)
                    break
                case 'sdb':
                    client = async ? new AmazonSimpleDBAsyncClient(credentials, configuration, executorService) : new AmazonSimpleDBClient(credentials, configuration)
                    break
                case 'ses':
                    client = async ? new AmazonSimpleEmailServiceAsyncClient(credentials, configuration, executorService) : new AmazonSimpleEmailServiceClient(credentials, configuration)
                    break
                case 'sns':
                    client = async ? new AmazonSNSAsyncClient(credentials, configuration, executorService) : new AmazonSNSClient(credentials, configuration)
                    break
                case 'sqs':
                    client = async ? new AmazonSQSAsyncClient(credentials, configuration, executorService) : new AmazonSQSClient(credentials, configuration)
                    break
                case 'sts':
                    client = async ? new AWSSecurityTokenServiceAsyncClient(credentials, configuration, executorService) : new AWSSecurityTokenServiceClient(credentials, configuration)
                    break
                case 'storageGateway':
                    client = async ? new AWSStorageGatewayAsyncClient(credentials, configuration, executorService) : new AWSStorageGatewayClient(credentials, configuration)
                    break
                case 'swf':
                    client = async ? new AmazonSimpleWorkflowAsyncClient(credentials, configuration, executorService) : new AmazonSimpleWorkflowClient(credentials, configuration)
                    break
                default:
                    throw new Exception("Sorry, no client found for service ${service}")
            }

            client.setRegion(region) // Workaround: do not use 'client.region = region', it generates an exception trying to cast Region to Regions (probably calling the wrong setter...)

            clientsCache[service][regionName] = client
        } else {
            clientsCache[service][regionName]
        }
    }

    // Service abbreviation exceptions (name is different for CloudWatch and SES)
    private String getServiceAbbreviation(String service) {
        switch(service) {
            case 'cloudSearchDomain':
                ServiceAbbreviations.CloudSearch
                break
            case 'cloudWatch':
                ServiceAbbreviations.CloudWatch
                break
            case 'cognitoIdentity':
                ServiceAbbreviations.CognitoIdentity
                break
            case 'cognitoSync':
                ServiceAbbreviations.CognitoSync
                break
            case 'ses':
                ServiceAbbreviations.Email
                break
            case 's3Encryption':
                ServiceAbbreviations.S3
                break
            default:
                service.toLowerCase()
        }
    }

}
