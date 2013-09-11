package grails.plugin.awssdk

import com.amazonaws.AmazonWebServiceClient
import com.amazonaws.ClientConfiguration
import com.amazonaws.Protocol
import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.auth.DefaultAWSCredentialsProviderChain
import com.amazonaws.regions.Region
import com.amazonaws.regions.RegionUtils
import com.amazonaws.regions.ServiceAbbreviations
import com.amazonaws.services.autoscaling.AmazonAutoScalingAsyncClient
import com.amazonaws.services.autoscaling.AmazonAutoScalingClient
import com.amazonaws.services.cloudformation.AmazonCloudFormationAsyncClient
import com.amazonaws.services.cloudformation.AmazonCloudFormationClient
import com.amazonaws.services.cloudfront.AmazonCloudFrontAsyncClient
import com.amazonaws.services.cloudfront.AmazonCloudFrontClient
import com.amazonaws.services.cloudsearch.AmazonCloudSearchAsyncClient
import com.amazonaws.services.cloudsearch.AmazonCloudSearchClient
import com.amazonaws.services.cloudwatch.AmazonCloudWatchAsyncClient
import com.amazonaws.services.cloudwatch.AmazonCloudWatchClient
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBAsyncClient
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient
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
import com.amazonaws.services.opsworks.AWSOpsWorksAsyncClient
import com.amazonaws.services.opsworks.AWSOpsWorksClient
import com.amazonaws.services.rds.AmazonRDSAsyncClient
import com.amazonaws.services.rds.AmazonRDSClient
import com.amazonaws.services.redshift.AmazonRedshiftAsyncClient
import com.amazonaws.services.redshift.AmazonRedshiftClient
import com.amazonaws.services.route53.AmazonRoute53AsyncClient
import com.amazonaws.services.route53.AmazonRoute53Client
import com.amazonaws.services.s3.AmazonS3Client
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
import com.amazonaws.services.storagegateway.AWSStorageGatewayAsyncClient
import com.amazonaws.services.storagegateway.AWSStorageGatewayClient

class AmazonWebService {

    static final String DEFAULT_REGION = 'us-east-1'
    
    static transactional = false

    def grailsApplication

    private Map asyncClients = [:]
    private Map clients = [:]
    private Map transferManagers = [:]

    AmazonAutoScalingAsyncClient getAutoScalingAsync(regionName = '') {
        getServiceClient('autoScaling', regionName, true) as AmazonAutoScalingAsyncClient
    }

    AmazonAutoScalingClient getAutoScaling(regionName = '') {
        getServiceClient('autoScaling', regionName) as AmazonAutoScalingClient
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

    AmazonCloudWatchAsyncClient getCloudWatchAsync(regionName = '') {
        getServiceClient('cloudWatch', regionName, true) as AmazonCloudWatchAsyncClient
    }

    AmazonCloudWatchClient getCloudWatch(regionName = '') {
        getServiceClient('cloudWatch', regionName) as AmazonCloudWatchClient
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

    private def getAwsConfig() {
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

        BasicAWSCredentials credentials = new BasicAWSCredentials(config.accessKey, config.secretKey)

        if(!credentials.AWSAccessKeyId || !credentials.AWSSecretKey) {
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
                userAgent: defaultConfig.userAgent ?: ''
        ]
        if (serviceConfig) {
            if (serviceConfig.connectionTimeout) config.connectionTimeout = serviceConfig.connectionTimeout
            if (serviceConfig.maxConnections) config.maxConnections = serviceConfig.maxConnections
            if (serviceConfig.maxErrorRetry) config.maxErrorRetry = serviceConfig.maxErrorRetry
            if (serviceConfig.protocol) config.protocol = serviceConfig.protocol
            if (serviceConfig.socketTimeout) config.socketTimeout = serviceConfig.socketTimeout
            if (serviceConfig.userAgent) config.connectionTimeout = serviceConfig.userAgent
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
        clientConfiguration
    }

    private AmazonWebServiceClient getServiceClient(String service, String regionName = '', Boolean async = false) {
        if (!regionName) {
            if (awsConfig[service]?.region) regionName = awsConfig[service].region
            else if (awsConfig?.region) regionName = awsConfig.region
            else regionName = DEFAULT_REGION
        }

        Region region = RegionUtils.getRegion(regionName)
        if (!region || !region.isServiceSupported(getServiceAbbreviation(service))) {
            if (!region) log.warn "Region ${regionName} not found"
            else if (!region.isServiceSupported(getServiceAbbreviation(service))) log.warn "Service ${service} is not supported in region ${regionName}"
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

            switch (service) {
                case 'autoScaling':
                    client = async ? new AmazonAutoScalingAsyncClient(credentials) : new AmazonAutoScalingClient(credentials)
                    break
                case 'cloudFormation':
                    client = async ? new AmazonCloudFormationAsyncClient(credentials) : new AmazonCloudFormationClient(credentials)
                    break
                case 'cloudFront':
                    client = async ? new AmazonCloudFrontAsyncClient(credentials) : new AmazonCloudFrontClient(credentials)
                    break
                case 'cloudSearch':
                    client = async ? new AmazonCloudSearchAsyncClient(credentials) : new AmazonCloudSearchClient(credentials)
                    break
                case 'cloudWatch':
                    client = async ? new AmazonCloudWatchAsyncClient(credentials) : new AmazonCloudWatchClient(credentials)
                    break
                case 'dynamoDB':
                    client = async ? new AmazonDynamoDBAsyncClient(credentials) : new AmazonDynamoDBClient(credentials)
                    break
                case 'ec2':
                    client = async ? new AmazonEC2AsyncClient(credentials) : new AmazonEC2Client(credentials)
                    break
                case 'elasticBeanstalk':
                    client = async ? new AWSElasticBeanstalkAsyncClient(credentials) : new AWSElasticBeanstalkClient(credentials)
                    break
                case 'elastiCache':
                    client = async ? new AmazonElastiCacheAsyncClient(credentials) : new AmazonElastiCacheClient(credentials)
                    break
                case 'elasticLoadBalancing':
                    client = async ? new AmazonElasticLoadBalancingAsyncClient(credentials) : new AmazonElasticLoadBalancingClient(credentials)
                    break
                case 'elasticMapReduce':
                    client = async ? new AmazonElasticMapReduceAsyncClient(credentials) : new AmazonElasticMapReduceClient(credentials)
                    break
                case 'elasticTranscoder':
                    client = async ? new AmazonElasticTranscoderAsyncClient(credentials) : new AmazonElasticTranscoderClient(credentials)
                    break
                case 'glacier':
                    client = async ? new AmazonGlacierAsyncClient(credentials) : new AmazonGlacierClient(credentials)
                    break
                case 'iam':
                    client = async ? new AmazonIdentityManagementAsyncClient(credentials) : new AmazonIdentityManagementClient(credentials)
                    break
                case 'importExport':
                    client = async ? new AmazonImportExportAsyncClient(credentials) : new AmazonImportExportClient(credentials)
                    break
                case 'opsWorks':
                    client = async ? new AWSOpsWorksAsyncClient(credentials) : new AWSOpsWorksClient(credentials)
                    break
                case 'rds':
                    client = async ? new AmazonRDSAsyncClient(credentials) : new AmazonRDSClient(credentials)
                    break
                case 'redshift':
                    client = async ? new AmazonRedshiftAsyncClient(credentials) : new AmazonRedshiftClient(credentials)
                    break
                case 'route53':
                    client = async ? new AmazonRoute53AsyncClient(credentials) : new AmazonRoute53Client(credentials)
                    break
                case 's3':
                    if (async) throw new Exception("Sorry, there is no async client for AmazonS3")
                    client = new AmazonS3Client(credentials)
                    break
                case 'sdb':
                    client = async ? new AmazonSimpleDBAsyncClient(credentials) : new AmazonSimpleDBClient(credentials)
                    break
                case 'ses':
                    client = async ? new AmazonSimpleEmailServiceAsyncClient(credentials) : new AmazonSimpleEmailServiceClient(credentials)
                    break
                case 'sns':
                    client = async ? new AmazonSNSAsyncClient(credentials) : new AmazonSNSClient(credentials)
                    break
                case 'sqs':
                    client = async ? new AmazonSQSAsyncClient(credentials) : new AmazonSQSClient(credentials)
                    break
                case 'sts':
                    client = async ? new AWSSecurityTokenServiceAsyncClient(credentials) : new AWSSecurityTokenServiceClient(credentials)
                    break
                case 'storageGateway':
                    client = async ? new AWSStorageGatewayAsyncClient(credentials) : new AWSStorageGatewayClient(credentials)
                    break
                case 'swf':
                    client = async ? new AmazonSimpleWorkflowAsyncClient(credentials) : new AmazonSimpleWorkflowClient(credentials)
                    break
                default:
                    throw new Exception("Sorry, no client found for service ${service}")
            }

            client.configuration = configuration
            client.region = region

            clientsCache[service][regionName] = client
        } else {
            clientsCache[service][regionName]
        }
    }

    // Service abbreviation exceptions (name is different for CloudWatch and SES)
    private String getServiceAbbreviation(String service) {
        switch(service) {
            case 'cloudWatch':
                ServiceAbbreviations.CloudWatch // 'monitoring'
                break
            case 'ses':
                ServiceAbbreviations.Email // 'email'
                break
            default:
                service.toLowerCase()
        }
    }

}
