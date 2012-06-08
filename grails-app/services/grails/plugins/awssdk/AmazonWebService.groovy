package grails.plugins.awssdk

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
import com.amazonaws.services.identitymanagement.AmazonIdentityManagementAsyncClient
import com.amazonaws.services.identitymanagement.AmazonIdentityManagementClient
import com.amazonaws.services.importexport.AmazonImportExportAsyncClient
import com.amazonaws.services.importexport.AmazonImportExportClient
import com.amazonaws.services.rds.AmazonRDSAsyncClient
import com.amazonaws.services.rds.AmazonRDSClient
import com.amazonaws.services.route53.AmazonRoute53AsyncClient
import com.amazonaws.services.route53.AmazonRoute53Client
import com.amazonaws.services.s3.AmazonS3Client
import com.amazonaws.services.simpledb.AmazonSimpleDBAsyncClient
import com.amazonaws.services.simpledb.AmazonSimpleDBClient
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceAsyncClient
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClient
import com.amazonaws.services.sns.AmazonSNSAsyncClient
import com.amazonaws.services.sns.AmazonSNSClient
import com.amazonaws.services.sqs.AmazonSQSAsyncClient
import com.amazonaws.services.sqs.AmazonSQSClient
import com.amazonaws.services.storagegateway.AWSStorageGatewayAsyncClient
import com.amazonaws.services.storagegateway.AWSStorageGatewayClient

class AmazonWebService {

    String defaultRegion = 'us-east-1'

    def grailsApplication

    private Map asyncClients = [:]
    private Map clients = [:]

    AmazonAutoScalingAsyncClient getAutoScalingAsync(region = '') {
        getServiceClient('autoscaling', region, true) as AmazonAutoScalingAsyncClient
    }

    AmazonAutoScalingClient getAutoScaling(region = '') {
        getServiceClient('autoscaling', region) as AmazonAutoScalingClient
    }

    AmazonCloudFormationAsyncClient getCloudFormationAsync(region = '') {
        getServiceClient('cloudformation', region, true) as AmazonCloudFormationAsyncClient
    }

    AmazonCloudFormationClient getCloudFormation(region = '') {
        getServiceClient('cloudformation', region) as AmazonCloudFormationClient
    }

    AmazonCloudFrontAsyncClient getCloudFrontAsync() {
        getServiceClient('cloudfront', '', true) as AmazonCloudFrontAsyncClient
    }

    AmazonCloudFrontClient getCloudFront() {
        getServiceClient('cloudfront') as AmazonCloudFrontClient
    }

    AmazonCloudSearchAsyncClient getCloudSearchAsync(region = '') {
        getServiceClient('cloudsearch', region, true) as AmazonCloudSearchAsyncClient
    }

    AmazonCloudSearchClient getCloudSearch(region = '') {
        getServiceClient('cloudsearch', region) as AmazonCloudSearchClient
    }

    AmazonCloudWatchAsyncClient getCloudWatchAsync(region = '') {
        getServiceClient('cloudwatch', region, true) as AmazonCloudWatchAsyncClient
    }

    AmazonCloudWatchClient getCloudWatch(region = '') {
        getServiceClient('cloudwatch', region) as AmazonCloudWatchClient
    }

    AmazonDynamoDBAsyncClient getDynamoDBAsync(region = '') {
        getServiceClient('dynamodb', region, true) as AmazonDynamoDBAsyncClient
    }

    AmazonDynamoDBClient getDynamoDB(region = '') {
        getServiceClient('dynamodb', region) as AmazonDynamoDBClient
    }

    AmazonEC2AsyncClient getEc2Async(region = '') {
        getServiceClient('ec2', region, true) as AmazonEC2AsyncClient
    }

    AmazonEC2Client getEc2(region = '') {
        getServiceClient('ec2', region) as AmazonEC2Client
    }

    AWSElasticBeanstalkAsyncClient getElasticBeanstalkAsync(region = '') {
        getServiceClient('elasticbeanstalk', region, true) as AWSElasticBeanstalkAsyncClient
    }

    AWSElasticBeanstalkClient getElasticBeanstalk(region = '') {
        getServiceClient('elasticbeanstalk', region) as AWSElasticBeanstalkClient
    }

    AmazonElastiCacheAsyncClient getElastiCacheAsync(region = '') {
        getServiceClient('elasticache', region, true) as AmazonElastiCacheAsyncClient
    }

    AmazonElastiCacheClient getElastiCache(region = '') {
        getServiceClient('elasticache', region) as AmazonElastiCacheClient
    }

    AmazonElasticLoadBalancingAsyncClient getElbAsync(region = '') {
        getServiceClient('elasticloadbalancing', region, true) as AmazonElasticLoadBalancingAsyncClient
    }

    AmazonElasticLoadBalancingClient getElb(region = '') {
        getServiceClient('elasticloadbalancing', region) as AmazonElasticLoadBalancingClient
    }

    AmazonElasticMapReduceAsyncClient getElasticMapReduceAsync(region = '') {
        getServiceClient('elasticmapreduce', region, true) as AmazonElasticMapReduceAsyncClient
    }

    AmazonElasticMapReduceClient getElasticMapReduce(region = '') {
        getServiceClient('elasticmapreduce', region) as AmazonElasticMapReduceClient
    }

    AmazonIdentityManagementAsyncClient getIamAsync() {
        getServiceClient('iam', '', true) as AmazonIdentityManagementAsyncClient
    }

    AmazonIdentityManagementClient getIam(region = '') {
        getServiceClient('iam') as AmazonIdentityManagementClient
    }

    AmazonImportExportAsyncClient getImportExportAsync() {
        getServiceClient('importexport', '', true) as AmazonImportExportAsyncClient
    }

    AmazonImportExportClient getImportExport(region = '') {
        getServiceClient('importexport') as AmazonImportExportClient
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

    AmazonSimpleEmailServiceAsyncClient getSesAsync(String region = '') {
        getServiceClient('ses', region) as AmazonSimpleEmailServiceAsyncClient
    }

    AmazonSimpleEmailServiceClient getSes(String region = '') {
        getServiceClient('ses', region) as AmazonSimpleEmailServiceClient
    }

    AmazonSimpleDBAsyncClient getSimpleDBAsync(String region = '') {
        getServiceClient('simpledb', region, true) as AmazonSimpleDBAsyncClient
    }

    AmazonSimpleDBClient getSimpleDB(String region = '') {
        getServiceClient('simpledb', region) as AmazonSimpleDBClient
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
        getServiceClient('storagegateway', region, true) as AWSStorageGatewayAsyncClient
    }

    AWSStorageGatewayClient getStorageGateway(String region = '') {
        getServiceClient('storagegateway', region) as AWSStorageGatewayClient
    }

    // PRIVATE

    private def getAwsConfig() {
        grailsApplication.config.grails?.plugins?.awssdk
    }

    private BasicAWSCredentials buildCredentials(defaultConfig, serviceConfig) {
        Map config = [accessKey: defaultConfig.accessKey ?: '',
                      secretKey: defaultConfig.secretKey ?: '']
        if (serviceConfig) {
            if (serviceConfig.accessKey) config.accessKey = serviceConfig.accessKey
            if (serviceConfig.secretKey) config.secretKey = serviceConfig.secretKey
        }
        new BasicAWSCredentials(config.accessKey, config.secretKey)
    }

    private ClientConfiguration buildClientConfiguration(defaultConfig, serviceConfig) {
        Map config = [connectionTimeout: defaultConfig.connectionTimeout ?: 0,
                      maxConnections: defaultConfig.maxConnections ?: 0,
                      maxErrorRetry: defaultConfig.maxErrorRetry ?: 0,
                      protocol : defaultConfig.protocol ?: '',

                      socketTimeout: defaultConfig.socketTimeout ?: 0,
                      userAgent: defaultConfig.userAgent ?: '']
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
            if (config.protocol.toUpperCase() == 'HTTP' ) clientConfiguration.protocol = Protocol.HTTP
            else clientConfiguration.protocol = Protocol.HTTPS
        }
        if (config.socketTimeout) clientConfiguration.socketTimeout = config.socketTimeout
        if (config.userAgent) clientConfiguration.userAgent = config.userAgent
        clientConfiguration
    }

    private AmazonWebServiceClient getServiceClient(String service, String region = '', Boolean async = false){
        if (!region) {
            if (awsConfig[service]?.region) region = awsConfig[service].region
            else if (awsConfig?.region) region = awsConfig.region
            else region = defaultRegion
        }

        if (async && !asyncClients[service]) asyncClients[service] = [:]
        else if (!async && !clients[service]) clients[service] = [:]
        if ((async && !asyncClients[service].hasProperty(region))
            || (!async && !clients[service].hasProperty(region))) {
            AmazonWebServiceClient client
            switch(service) {
                case 'autoscaling':
                    if (async) {
                        client = new AmazonAutoScalingAsyncClient(buildCredentials(awsConfig, awsConfig.ses), buildClientConfiguration(awsConfig, awsConfig.ses))
                    } else {
                        client = new AmazonAutoScalingClient(buildCredentials(awsConfig, awsConfig.ses), buildClientConfiguration(awsConfig, awsConfig.ses))
                    }
                    client.endpoint = "autoscaling.${region}.amazonaws.com"
                    break
                case 'cloudformation':
                    if (async) {
                        client = new AmazonCloudFormationAsyncClient(buildCredentials(awsConfig, awsConfig.ses), buildClientConfiguration(awsConfig, awsConfig.ses))
                    } else {
                        client = new AmazonCloudFormationClient(buildCredentials(awsConfig, awsConfig.ses), buildClientConfiguration(awsConfig, awsConfig.ses))
                    }
                    client.endpoint = "autoscaling.${region}.amazonaws.com"
                    break
                case 'cloudfront':
                    if (async) {
                        client = new AmazonCloudFrontAsyncClient(buildCredentials(awsConfig, awsConfig.ses), buildClientConfiguration(awsConfig, awsConfig.ses))
                    } else {
                        client = new AmazonCloudFrontClient(buildCredentials(awsConfig, awsConfig.ses), buildClientConfiguration(awsConfig, awsConfig.ses))
                    }
                    client.endpoint = "cloudfront.amazonaws.com"
                    break
                case 'cloudsearch':
                    if (async) {
                        client = new AmazonCloudSearchAsyncClient(buildCredentials(awsConfig, awsConfig.ses), buildClientConfiguration(awsConfig, awsConfig.ses))
                    } else {
                        client = new AmazonCloudSearchClient(buildCredentials(awsConfig, awsConfig.ses), buildClientConfiguration(awsConfig, awsConfig.ses))
                    }
                    client.endpoint = "cloudsearch.${region}.amazonaws.com"
                    break
                case 'cloudwatch':
                    if (async) {
                        client = new AmazonCloudWatchAsyncClient(buildCredentials(awsConfig, awsConfig.ses), buildClientConfiguration(awsConfig, awsConfig.ses))
                    } else {
                        client = new AmazonCloudWatchClient(buildCredentials(awsConfig, awsConfig.ses), buildClientConfiguration(awsConfig, awsConfig.ses))
                    }
                    client.endpoint = "monitoring.${region}.amazonaws.com"
                    break
                case 'dynamodb':
                    if (async) {
                        client = new AmazonDynamoDBAsyncClient(buildCredentials(awsConfig, awsConfig.ses), buildClientConfiguration(awsConfig, awsConfig.ses))
                    } else {
                        client = new AmazonDynamoDBClient(buildCredentials(awsConfig, awsConfig.ses), buildClientConfiguration(awsConfig, awsConfig.ses))
                    }
                    client.endpoint = "dynamodb.${region}.amazonaws.com"
                    break
                case 'ec2':
                    if (async) {
                        client = new AmazonEC2AsyncClient(buildCredentials(awsConfig, awsConfig.ses), buildClientConfiguration(awsConfig, awsConfig.ses))
                    } else {
                        client = new AmazonEC2Client(buildCredentials(awsConfig, awsConfig.ses), buildClientConfiguration(awsConfig, awsConfig.ses))
                    }
                    client.endpoint = "ec2.${region}.amazonaws.com"
                    break
                case 'elasticbeanstalk':
                    if (async) {
                        client = new AWSElasticBeanstalkAsyncClient(buildCredentials(awsConfig, awsConfig.ses), buildClientConfiguration(awsConfig, awsConfig.ses))
                    } else {
                        client = new AWSElasticBeanstalkClient(buildCredentials(awsConfig, awsConfig.ses), buildClientConfiguration(awsConfig, awsConfig.ses))
                    }
                    client.endpoint = "elasticbeanstalk.${region}.amazonaws.com"
                    break
                case 'elasticache':
                    if (async) {
                        client = new AmazonElastiCacheAsyncClient(buildCredentials(awsConfig, awsConfig.ses), buildClientConfiguration(awsConfig, awsConfig.ses))
                    } else {
                        client = new AmazonElastiCacheClient(buildCredentials(awsConfig, awsConfig.ses), buildClientConfiguration(awsConfig, awsConfig.ses))
                    }
                    client.endpoint = "elasticache.${region}.amazonaws.com"
                    break
                case 'elasticloadbalancing':
                    if (async) {
                        client = new AmazonElasticLoadBalancingAsyncClient(buildCredentials(awsConfig, awsConfig.ses), buildClientConfiguration(awsConfig, awsConfig.ses))
                    } else {
                        client = new AmazonElasticLoadBalancingClient(buildCredentials(awsConfig, awsConfig.ses), buildClientConfiguration(awsConfig, awsConfig.ses))
                    }
                    client.endpoint = "elasticloadbalancing.${region}.amazonaws.com"
                    break
                case 'elasticmapreduce':
                    if (async) {
                        client = new AmazonElasticMapReduceAsyncClient(buildCredentials(awsConfig, awsConfig.ses), buildClientConfiguration(awsConfig, awsConfig.ses))
                    } else {
                        client = new AmazonElasticMapReduceClient(buildCredentials(awsConfig, awsConfig.ses), buildClientConfiguration(awsConfig, awsConfig.ses))
                    }
                    client.endpoint = "elasticmapreduce.${region}.amazonaws.com"
                    break
                case 'iam':
                    if (async) {
                        client = new AmazonIdentityManagementAsyncClient(buildCredentials(awsConfig, awsConfig.ses), buildClientConfiguration(awsConfig, awsConfig.ses))
                    } else {
                        client = new AmazonIdentityManagementClient(buildCredentials(awsConfig, awsConfig.ses), buildClientConfiguration(awsConfig, awsConfig.ses))
                    }
                    client.endpoint = "iam.amazonaws.com"
                    break
                case 'importexport':
                    if (async) {
                        client = new AmazonImportExportAsyncClient(buildCredentials(awsConfig, awsConfig.ses), buildClientConfiguration(awsConfig, awsConfig.ses))
                    } else {
                        client = new AmazonImportExportClient(buildCredentials(awsConfig, awsConfig.ses), buildClientConfiguration(awsConfig, awsConfig.ses))
                    }
                    client.endpoint = "importexport.amazonaws.com"
                    break
                case 'rds':
                    if (async) {
                        client = new AmazonRDSAsyncClient(buildCredentials(awsConfig, awsConfig.ses), buildClientConfiguration(awsConfig, awsConfig.ses))
                    } else {
                        client = new AmazonRDSClient(buildCredentials(awsConfig, awsConfig.ses), buildClientConfiguration(awsConfig, awsConfig.ses))
                    }
                    client.endpoint = "rds.${region}.amazonaws.com"
                    break
                case 'route53':
                    if (async) {
                        client = new AmazonRoute53AsyncClient(buildCredentials(awsConfig, awsConfig.ses), buildClientConfiguration(awsConfig, awsConfig.ses))
                    } else {
                        client = new AmazonRoute53Client(buildCredentials(awsConfig, awsConfig.ses), buildClientConfiguration(awsConfig, awsConfig.ses))
                    }
                    client.endpoint = "route53.amazonaws.com"
                    break
                case 's3':
                    if (async) throw new Exception("Sorry, there is no async client for AmazonS3")
                    client = new AmazonS3Client(buildCredentials(awsConfig, awsConfig[service]), buildClientConfiguration(awsConfig, awsConfig[service]))
                    if (region == 'us' || region == defaultRegion) {
                        client.endpoint = "s3.amazonaws.com"
                    } else {
                        client.endpoint = "s3-${region}.amazonaws.com"
                    }
                    break
                case 'ses':
                    if (async) {
                        client = new AmazonSimpleEmailServiceAsyncClient(buildCredentials(awsConfig, awsConfig.ses), buildClientConfiguration(awsConfig, awsConfig.ses))
                    } else {
                        client = new AmazonSimpleEmailServiceClient(buildCredentials(awsConfig, awsConfig.ses), buildClientConfiguration(awsConfig, awsConfig.ses))
                    }
                    client.endpoint = "email.${region}.amazonaws.com"
                    break
                case 'simpledb':
                    if (async) {
                        client = new AmazonSimpleDBAsyncClient(buildCredentials(awsConfig, awsConfig.sqs), buildClientConfiguration(awsConfig, awsConfig.sqs))
                    } else {
                        client = new AmazonSimpleDBClient(buildCredentials(awsConfig, awsConfig.sqs), buildClientConfiguration(awsConfig, awsConfig.sqs))
                    }
                    if (region == 'us-east-1') {
                        client.endpoint = "sdb.amazonaws.com"
                    } else {
                        client.endpoint = "sdb.${region}.amazonaws.com"
                    }
                    break
                case 'sns':
                    if (async) {
                        client = new AmazonSNSAsyncClient(buildCredentials(awsConfig, awsConfig.sqs), buildClientConfiguration(awsConfig, awsConfig.sqs))
                    } else {
                        client = new AmazonSNSClient(buildCredentials(awsConfig, awsConfig.sqs), buildClientConfiguration(awsConfig, awsConfig.sqs))
                    }
                    client.endpoint = "sns.${region}.amazonaws.com"
                    break
                case 'sqs':
                    if (async) {
                        client = new AmazonSQSAsyncClient(buildCredentials(awsConfig, awsConfig.sqs), buildClientConfiguration(awsConfig, awsConfig.sqs))
                    } else {
                        client = new AmazonSQSClient(buildCredentials(awsConfig, awsConfig.sqs), buildClientConfiguration(awsConfig, awsConfig.sqs))
                    }
                    client.endpoint = "sqs.${region}.amazonaws.com"
                    break
                case 'storagegateway':
                    if (async) {
                        client = new AWSStorageGatewayAsyncClient(buildCredentials(awsConfig, awsConfig.ses), buildClientConfiguration(awsConfig, awsConfig.ses))
                    } else {
                        client = new AWSStorageGatewayClient(buildCredentials(awsConfig, awsConfig.ses), buildClientConfiguration(awsConfig, awsConfig.ses))
                    }
                    client.endpoint = "storagegateway.${region}.amazonaws.com"
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
