package grails.plugin.awssdk

import com.amazonaws.AmazonWebServiceClient
import com.amazonaws.ClientConfiguration
import com.amazonaws.Protocol
import com.amazonaws.auth.BasicAWSCredentials
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
import com.amazonaws.services.dynamodb.AmazonDynamoDBAsyncClient
import com.amazonaws.services.dynamodb.AmazonDynamoDBClient
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
import com.amazonaws.services.rds.AmazonRDSAsyncClient
import com.amazonaws.services.rds.AmazonRDSClient
import com.amazonaws.services.route53.AmazonRoute53AsyncClient
import com.amazonaws.services.route53.AmazonRoute53Client
import com.amazonaws.services.s3.AmazonS3Client
import com.amazonaws.services.s3.transfer.TransferManager
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

    def grailsApplication

    private Map asyncClients = [:]
    private Map clients = [:]
    private Map transferManagers = [:]

    AmazonAutoScalingAsyncClient getAutoScalingAsync(region = '') {
        getServiceClient('autoScaling', region, true) as AmazonAutoScalingAsyncClient
    }

    AmazonAutoScalingClient getAutoScaling(region = '') {
        getServiceClient('autoScaling', region) as AmazonAutoScalingClient
    }

    AmazonCloudFormationAsyncClient getCloudFormationAsync(region = '') {
        getServiceClient('cloudFormation', region, true) as AmazonCloudFormationAsyncClient
    }

    AmazonCloudFormationClient getCloudFormation(region = '') {
        getServiceClient('cloudFormation', region) as AmazonCloudFormationClient
    }

    AmazonCloudFrontAsyncClient getCloudFrontAsync() {
        getServiceClient('cloudFront', '', true) as AmazonCloudFrontAsyncClient
    }

    AmazonCloudFrontClient getCloudFront() {
        getServiceClient('cloudFront') as AmazonCloudFrontClient
    }

    AmazonCloudSearchAsyncClient getCloudSearchAsync(region = '') {
        getServiceClient('cloudSearch', region, true) as AmazonCloudSearchAsyncClient
    }

    AmazonCloudSearchClient getCloudSearch(region = '') {
        getServiceClient('cloudSearch', region) as AmazonCloudSearchClient
    }

    AmazonCloudWatchAsyncClient getCloudWatchAsync(region = '') {
        getServiceClient('cloudWatch', region, true) as AmazonCloudWatchAsyncClient
    }

    AmazonCloudWatchClient getCloudWatch(region = '') {
        getServiceClient('cloudWatch', region) as AmazonCloudWatchClient
    }

    AmazonDynamoDBAsyncClient getDynamoDBAsync(region = '') {
        getServiceClient('dynamoDB', region, true) as AmazonDynamoDBAsyncClient
    }

    AmazonDynamoDBClient getDynamoDB(region = '') {
        getServiceClient('dynamoDB', region) as AmazonDynamoDBClient
    }

    AmazonEC2AsyncClient getEc2Async(region = '') {
        getServiceClient('ec2', region, true) as AmazonEC2AsyncClient
    }

    AmazonEC2Client getEc2(region = '') {
        getServiceClient('ec2', region) as AmazonEC2Client
    }

    AWSElasticBeanstalkAsyncClient getElasticBeanstalkAsync(region = '') {
        getServiceClient('elasticBeanstalk', region, true) as AWSElasticBeanstalkAsyncClient
    }

    AWSElasticBeanstalkClient getElasticBeanstalk(region = '') {
        getServiceClient('elasticBeanstalk', region) as AWSElasticBeanstalkClient
    }

    AmazonElastiCacheAsyncClient getElastiCacheAsync(region = '') {
        getServiceClient('elastiCache', region, true) as AmazonElastiCacheAsyncClient
    }

    AmazonElastiCacheClient getElastiCache(region = '') {
        getServiceClient('elastiCache', region) as AmazonElastiCacheClient
    }

    AmazonElasticLoadBalancingAsyncClient getElbAsync(region = '') {
        getServiceClient('elasticLoadBalancing', region, true) as AmazonElasticLoadBalancingAsyncClient
    }

    AmazonElasticLoadBalancingClient getElb(region = '') {
        getServiceClient('elasticLoadBalancing', region) as AmazonElasticLoadBalancingClient
    }

    AmazonElasticMapReduceAsyncClient getElasticMapReduceAsync(region = '') {
        getServiceClient('elasticMapReduce', region, true) as AmazonElasticMapReduceAsyncClient
    }

    AmazonElasticMapReduceClient getElasticMapReduce(region = '') {
        getServiceClient('elasticMapReduce', region) as AmazonElasticMapReduceClient
    }

    AmazonElasticTranscoderClient getElasticTranscoder(region = '') {
        getServiceClient('elasticTranscoder', region) as AmazonElasticTranscoderClient
    }

    AmazonGlacierClient getGlacier(region = '') {
        getServiceClient('glacier', region) as AmazonGlacierClient
    }

    AmazonIdentityManagementAsyncClient getIamAsync() {
        getServiceClient('iam', '', true) as AmazonIdentityManagementAsyncClient
    }

    AmazonIdentityManagementClient getIam(region = '') {
        getServiceClient('iam') as AmazonIdentityManagementClient
    }

    AmazonImportExportAsyncClient getImportExportAsync() {
        getServiceClient('importExport', '', true) as AmazonImportExportAsyncClient
    }

    AmazonImportExportClient getImportExport(region = '') {
        getServiceClient('importExport') as AmazonImportExportClient
    }

    AmazonRDSAsyncClient getRdsAsync(region = '') {
        getServiceClient('rds', region, true) as AmazonRDSAsyncClient
    }

    AmazonRDSClient getRds(region = '') {
        getServiceClient('rds', region) as AmazonRDSClient
    }

    AmazonRoute53AsyncClient getRoute53Async() {
        getServiceClient('route53', '', true) as AmazonRoute53AsyncClient
    }

    AmazonRoute53Client getRoute53() {
        getServiceClient('route53') as AmazonRoute53Client
    }

    AmazonS3Client getS3(String region = '') {
        getServiceClient('s3', region) as AmazonS3Client
    }

    AmazonSimpleDBAsyncClient getSdbAsync(String region = '') {
        getServiceClient('sdb', region, true) as AmazonSimpleDBAsyncClient
    }

    AmazonSimpleDBClient getSdb(String region = '') {
        getServiceClient('sdb', region) as AmazonSimpleDBClient
    }

    AmazonSimpleEmailServiceAsyncClient getSesAsync(String region = '') {
        getServiceClient('ses', region) as AmazonSimpleEmailServiceAsyncClient
    }

    AmazonSimpleEmailServiceClient getSes(String region = '') {
        getServiceClient('ses', region) as AmazonSimpleEmailServiceClient
    }

    AmazonSNSAsyncClient getSnsAsync(String region = '') {
        getServiceClient('sns', region, true) as AmazonSNSAsyncClient
    }

    AmazonSNSClient getSns(String region = '') {
        getServiceClient('sns', region) as AmazonSNSClient
    }

    AmazonSQSAsyncClient getSqsAsync(String region = '') {
        getServiceClient('sqs', region, true) as AmazonSQSAsyncClient
    }

    AmazonSQSClient getSqs(String region = '') {
        getServiceClient('sqs', region) as AmazonSQSClient
    }

    AWSStorageGatewayAsyncClient getStorageGatewayAsync(String region = '') {
        getServiceClient('storageGateway', region, true) as AWSStorageGatewayAsyncClient
    }

    AWSStorageGatewayClient getStorageGateway(String region = '') {
        getServiceClient('storageGateway', region) as AWSStorageGatewayClient
    }

    AmazonSimpleWorkflowAsyncClient getSwfAsync(String region = '') {
        getServiceClient('swf', region, true) as AmazonSimpleWorkflowAsyncClient
    }

    AmazonSimpleWorkflowClient getSwf(String region = '') {
        getServiceClient('swf', region) as AmazonSimpleWorkflowClient
    }

    TransferManager getTransferManager(String region = '') {
        if (!region) {
            if (awsConfig['s3']?.region) region = awsConfig['s3'].region
            else if (awsConfig?.region) region = awsConfig.region
            else region = DEFAULT_REGION
        }

        if (!transferManagers[region]) {
            transferManagers[region] = new TransferManager(getS3(region))
        }
        transferManagers[region]
    }

    // PRIVATE

    private def getAwsConfig() {
        grailsApplication.config.grails?.plugin?.awssdk
    }

    private BasicAWSCredentials buildCredentials(defaultConfig, serviceConfig) {
        Map config = [
                accessKey: defaultConfig.accessKey ?: '',
                secretKey: defaultConfig.secretKey ?: ''
        ]
        if (serviceConfig) {
            if (serviceConfig.accessKey) config.accessKey = serviceConfig.accessKey
            if (serviceConfig.secretKey) config.secretKey = serviceConfig.secretKey
        }
        new BasicAWSCredentials(config.accessKey, config.secretKey)
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

    private AmazonWebServiceClient getServiceClient(String service, String region = '', Boolean async = false) {
        if (!region) {
            if (awsConfig[service]?.region) region = awsConfig[service].region
            else if (awsConfig?.region) region = awsConfig.region
            else region = DEFAULT_REGION
        }

        if (async && !asyncClients[service]) asyncClients[service] = [:]
        else if (!async && !clients[service]) clients[service] = [:]

        if ((async && !asyncClients[service].hasProperty(region))
                || (!async && !clients[service].hasProperty(region))) {
            AmazonWebServiceClient client
            BasicAWSCredentials credentials = buildCredentials(awsConfig, awsConfig[service])
            ClientConfiguration configuration = buildClientConfiguration(awsConfig, awsConfig[service])
            switch (service) {
                case 'autoScaling':
                    if (async) {
                        if (credentials.AWSAccessKeyId && credentials.AWSSecretKey) client = new AmazonAutoScalingAsyncClient(credentials)
                        else client = new AmazonAutoScalingAsyncClient()
                    } else {
                        if (credentials.AWSAccessKeyId && credentials.AWSSecretKey) client = new AmazonAutoScalingClient(credentials)
                        else client = new AmazonAutoScalingClient()
                    }
                    client.configuration = configuration
                    client.endpoint = "autoscaling.${region}.amazonaws.com"
                    break
                case 'cloudFormation':
                    if (async) {
                        if (credentials.AWSAccessKeyId && credentials.AWSSecretKey) client = new AmazonCloudFormationAsyncClient(credentials)
                        else client = new AmazonCloudFormationAsyncClient()
                    } else {
                        if (credentials.AWSAccessKeyId && credentials.AWSSecretKey) client = new AmazonCloudFormationClient(credentials)
                        else client = new AmazonCloudFormationClient()
                    }
                    client.configuration = configuration
                    client.endpoint = "cloudformation.${region}.amazonaws.com"
                    break
                case 'cloudFront':
                    if (async) {
                        if (credentials.AWSAccessKeyId && credentials.AWSSecretKey) client = new AmazonCloudFrontAsyncClient(credentials)
                        else client = new AmazonCloudFrontAsyncClient()
                    } else {
                        if (credentials.AWSAccessKeyId && credentials.AWSSecretKey) client = new AmazonCloudFrontClient(credentials)
                        else client = new AmazonCloudFrontClient()
                    }
                    client.configuration = configuration
                    client.endpoint = "cloudfront.amazonaws.com"
                    break
                case 'cloudSearch':
                    if (async) {
                        if (credentials.AWSAccessKeyId && credentials.AWSSecretKey) client = new AmazonCloudSearchAsyncClient(credentials)
                        else client = new AmazonCloudSearchAsyncClient()
                    } else {
                        if (credentials.AWSAccessKeyId && credentials.AWSSecretKey) client = new AmazonCloudSearchClient(credentials)
                        else client = new AmazonCloudSearchClient()
                    }
                    client.configuration = configuration
                    client.endpoint = "cloudsearch.${region}.amazonaws.com"
                    break
                case 'cloudWatch':
                    if (async) {
                        if (credentials.AWSAccessKeyId && credentials.AWSSecretKey) client = new AmazonCloudWatchAsyncClient(credentials)
                        else client = new AmazonCloudWatchAsyncClient()
                    } else {
                        if (credentials.AWSAccessKeyId && credentials.AWSSecretKey) client = new AmazonCloudWatchClient(credentials)
                        else client = new AmazonCloudWatchClient()
                    }
                    client.configuration = configuration
                    client.endpoint = "monitoring.${region}.amazonaws.com"
                    break
                case 'dynamoDB':
                    if (async) {
                        if (credentials.AWSAccessKeyId && credentials.AWSSecretKey) client = new AmazonDynamoDBAsyncClient(credentials)
                        else client = new AmazonDynamoDBAsyncClient()
                    } else {
                        if (credentials.AWSAccessKeyId && credentials.AWSSecretKey) client = new AmazonDynamoDBClient(credentials)
                        else client = new AmazonDynamoDBClient()
                    }
                    client.configuration = configuration
                    client.endpoint = "dynamodb.${region}.amazonaws.com"
                    break
                case 'ec2':
                    if (async) {
                        if (credentials.AWSAccessKeyId && credentials.AWSSecretKey) client = new AmazonEC2AsyncClient(credentials)
                        else client = new AmazonEC2AsyncClient()
                    } else {
                        if (credentials.AWSAccessKeyId && credentials.AWSSecretKey) client = new AmazonEC2Client(credentials)
                        else client = new AmazonEC2Client()
                    }
                    client.configuration = configuration
                    client.endpoint = "ec2.${region}.amazonaws.com"
                    break
                case 'elasticBeanstalk':
                    if (async) {
                        if (credentials.AWSAccessKeyId && credentials.AWSSecretKey) client = new AWSElasticBeanstalkAsyncClient(credentials)
                        else client = new AWSElasticBeanstalkAsyncClient()
                    } else {
                        if (credentials.AWSAccessKeyId && credentials.AWSSecretKey) client = new AWSElasticBeanstalkClient(credentials)
                        else client = new AWSElasticBeanstalkClient()
                    }
                    client.configuration = configuration
                    client.endpoint = "elasticbeanstalk.${region}.amazonaws.com"
                    break
                case 'elastiCache':
                    if (async) {
                        if (credentials.AWSAccessKeyId && credentials.AWSSecretKey) client = new AmazonElastiCacheAsyncClient(credentials)
                        else client = new AmazonElastiCacheAsyncClient()
                    } else {
                        if (credentials.AWSAccessKeyId && credentials.AWSSecretKey) client = new AmazonElastiCacheClient(credentials)
                        else client = new AmazonElastiCacheClient()
                    }
                    client.configuration = configuration
                    client.endpoint = "elasticache.${region}.amazonaws.com"
                    break
                case 'elasticLoadBalancing':
                    if (async) {
                        if (credentials.AWSAccessKeyId && credentials.AWSSecretKey) client = new AmazonElasticLoadBalancingAsyncClient(credentials)
                        else client = new AmazonElasticLoadBalancingAsyncClient()
                    } else {
                        if (credentials.AWSAccessKeyId && credentials.AWSSecretKey) client = new AmazonElasticLoadBalancingClient(credentials)
                        else client = new AmazonElasticLoadBalancingClient()
                    }
                    client.configuration = configuration
                    client.endpoint = "elasticloadbalancing.${region}.amazonaws.com"
                    break
                case 'elasticMapReduce':
                    if (async) {
                        if (credentials.AWSAccessKeyId && credentials.AWSSecretKey) client = new AmazonElasticMapReduceAsyncClient(credentials)
                        else client = new AmazonElasticMapReduceAsyncClient()
                    } else {
                        if (credentials.AWSAccessKeyId && credentials.AWSSecretKey) client = new AmazonElasticMapReduceClient(credentials)
                        else client = new AmazonElasticMapReduceClient()
                    }
                    client.configuration = configuration
                    client.endpoint = "elasticmapreduce.${region}.amazonaws.com"
                    break
                case 'elasticTranscoder':
                    if (async) {
                        if (credentials.AWSAccessKeyId && credentials.AWSSecretKey) client = new AmazonElasticTranscoderAsyncClient(credentials)
                        else client = new AmazonElasticTranscoderAsyncClient()
                    } else {
                        if (credentials.AWSAccessKeyId && credentials.AWSSecretKey) client = new AmazonElasticTranscoderClient(credentials)
                        else client = new AmazonElasticTranscoderClient()
                    }
                    client.configuration = configuration
                    client.endpoint = "elastictranscoder.${region}.amazonaws.com"
                    break
                case 'glacier':
                    if (async) {
                        if (credentials.AWSAccessKeyId && credentials.AWSSecretKey) client = new AmazonGlacierAsyncClient(credentials)
                        else client = new AmazonGlacierAsyncClient()
                    } else {
                        if (credentials.AWSAccessKeyId && credentials.AWSSecretKey) client = new AmazonGlacierClient(credentials)
                        else client = new AmazonGlacierClient()
                    }
                    client.configuration = configuration
                    client.endpoint = "glacier.${region}.amazonaws.com"
                    break
                case 'iam':
                    if (async) {
                        if (credentials.AWSAccessKeyId && credentials.AWSSecretKey) client = new AmazonIdentityManagementAsyncClient(credentials)
                        else client = new AmazonIdentityManagementAsyncClient()
                    } else {
                        if (credentials.AWSAccessKeyId && credentials.AWSSecretKey) client = new AmazonIdentityManagementClient(credentials)
                        else client = new AmazonIdentityManagementClient()
                    }
                    client.configuration = configuration
                    client.endpoint = "iam.amazonaws.com"
                    break
                case 'importExport':
                    if (async) {
                        if (credentials.AWSAccessKeyId && credentials.AWSSecretKey) client = new AmazonImportExportAsyncClient(credentials)
                        else client = new AmazonImportExportAsyncClient()
                    } else {
                        if (credentials.AWSAccessKeyId && credentials.AWSSecretKey) client = new AmazonImportExportClient(credentials)
                        else client = new AmazonImportExportClient()
                    }
                    client.configuration = configuration
                    client.endpoint = "importexport.amazonaws.com"
                    break
                case 'rds':
                    if (async) {
                        if (credentials.AWSAccessKeyId && credentials.AWSSecretKey) client = new AmazonRDSAsyncClient(credentials)
                        else client = new AmazonRDSAsyncClient()
                    } else {
                        if (credentials.AWSAccessKeyId && credentials.AWSSecretKey) client = new AmazonRDSClient(credentials)
                        else client = new AmazonRDSClient()
                    }
                    client.configuration = configuration
                    client.endpoint = "rds.${region}.amazonaws.com"
                    break
                case 'route53':
                    if (async) {
                        if (credentials.AWSAccessKeyId && credentials.AWSSecretKey) client = new AmazonRoute53AsyncClient(credentials)
                        else client = new AmazonRoute53AsyncClient()
                    } else {
                        if (credentials.AWSAccessKeyId && credentials.AWSSecretKey) client = new AmazonRoute53Client(credentials)
                        else client = new AmazonRoute53Client()
                    }
                    client.configuration = configuration
                    client.endpoint = "route53.amazonaws.com"
                    break
                case 's3':
                    if (async) throw new Exception("Sorry, there is no async client for AmazonS3")
                    if (credentials.AWSAccessKeyId && credentials.AWSSecretKey) client = new AmazonS3Client(credentials)
                    else client = new AmazonS3Client()
                    client.configuration = configuration
                    if (region == 'us' || region == DEFAULT_REGION) {
                        client.endpoint = "s3.amazonaws.com"
                    } else {
                        client.endpoint = "s3-${region}.amazonaws.com"
                    }
                    break
                case 'sdb':
                    if (async) {
                        if (credentials.AWSAccessKeyId && credentials.AWSSecretKey) client = new AmazonSimpleDBAsyncClient(credentials)
                        else client = new AmazonSimpleDBAsyncClient()
                    } else {
                        if (credentials.AWSAccessKeyId && credentials.AWSSecretKey) client = new AmazonSimpleDBClient(credentials)
                        else client = new AmazonSimpleDBClient()
                    }
                    client.configuration = configuration
                    if (region == 'us-east-1') {
                        client.endpoint = "sdb.amazonaws.com"
                    } else {
                        client.endpoint = "sdb.${region}.amazonaws.com"
                    }
                    break
                case 'ses':
                    if (async) {
                        if (credentials.AWSAccessKeyId && credentials.AWSSecretKey) client = new AmazonSimpleEmailServiceAsyncClient(credentials)
                        else client = new AmazonSimpleEmailServiceAsyncClient()
                    } else {
                        if (credentials.AWSAccessKeyId && credentials.AWSSecretKey) client = new AmazonSimpleEmailServiceClient(credentials)
                        else client = new AmazonSimpleEmailServiceClient()
                    }
                    client.configuration = configuration
                    client.endpoint = "email.${region}.amazonaws.com"
                    break
                case 'sns':
                    if (async) {
                        if (credentials.AWSAccessKeyId && credentials.AWSSecretKey) client = new AmazonSNSAsyncClient(credentials)
                        else client = new AmazonSNSAsyncClient()
                    } else {
                        if (credentials.AWSAccessKeyId && credentials.AWSSecretKey) client = new AmazonSNSClient(credentials)
                        else client = new AmazonSNSClient()
                    }
                    client.configuration = configuration
                    client.endpoint = "sns.${region}.amazonaws.com"
                    break
                case 'sqs':
                    if (async) {
                        if (credentials.AWSAccessKeyId && credentials.AWSSecretKey) client = new AmazonSQSAsyncClient(credentials)
                        else client = new AmazonSQSAsyncClient()
                    } else {
                        if (credentials.AWSAccessKeyId && credentials.AWSSecretKey) client = new AmazonSQSClient(credentials)
                        else client = new AmazonSQSClient()
                    }
                    client.configuration = configuration
                    client.endpoint = "sqs.${region}.amazonaws.com"
                    break
                case 'storageGateway':
                    if (async) {
                        if (credentials.AWSAccessKeyId && credentials.AWSSecretKey) client = new AWSStorageGatewayAsyncClient(credentials)
                        else client = new AWSStorageGatewayAsyncClient()
                    } else {
                        if (credentials.AWSAccessKeyId && credentials.AWSSecretKey) client = new AWSStorageGatewayClient(credentials)
                        else client = new AWSStorageGatewayClient()
                    }
                    client.configuration = configuration
                    client.endpoint = "storagegateway.${region}.amazonaws.com"
                    break
                case 'swf':
                    if (async) {
                        if (credentials.AWSAccessKeyId && credentials.AWSSecretKey) client = new AmazonSimpleWorkflowAsyncClient(credentials)
                        else client = new AmazonSimpleWorkflowAsyncClient()
                    } else {
                        if (credentials.AWSAccessKeyId && credentials.AWSSecretKey) client = new AmazonSimpleWorkflowClient(credentials)
                        else client = new AmazonSimpleWorkflowClient()
                    }
                    client.configuration = configuration
                    client.endpoint = "swf.${region}.amazonaws.com"
                    break
            }
            if (async) {
                asyncClients[service][region] = client
            } else {
                clients[service][region] = client
            }
        }
    }

}
