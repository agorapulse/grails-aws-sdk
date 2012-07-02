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
import com.amazonaws.services.simpleworkflow.AmazonSimpleWorkflowAsyncClient
import com.amazonaws.services.simpleworkflow.AmazonSimpleWorkflowClient
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

    // PRIVATE

    private def getAwsConfig() {
        grailsApplication.config.grails?.plugin?.awssdk
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
            BasicAWSCredentials credentials = buildCredentials(awsConfig, awsConfig[service])
            ClientConfiguration clientConfiguration = buildClientConfiguration(awsConfig, awsConfig[service])
            switch(service) {
                case 'autoScaling':
                   if (async) {
                        if (credentials.AWSAccessKeyId && credentials.AWSSecretKey) client = new AmazonAutoScalingAsyncClient(credentials, clientConfiguration)
                        else client = new AmazonAutoScalingAsyncClient(clientConfiguration)
                    } else {
                       if (credentials.AWSAccessKeyId && credentials.AWSSecretKey) client = new AmazonAutoScalingClient(credentials, clientConfiguration)
                       else client = new AmazonAutoScalingClient(clientConfiguration)
                    }
                    client.endpoint = "autoscaling.${region}.amazonaws.com"
                    break
                case 'cloudFormation':
                    if (async) {
                        if (credentials.AWSAccessKeyId && credentials.AWSSecretKey) client = new AmazonCloudFormationAsyncClient(credentials, clientConfiguration)
                        else client = new AmazonCloudFormationAsyncClient(clientConfiguration)
                    } else {
                        if (credentials.AWSAccessKeyId && credentials.AWSSecretKey) client = new AmazonCloudFormationClient(credentials, clientConfiguration)
                        else client = new AmazonCloudFormationClient(clientConfiguration)
                    }
                    client.endpoint = "cloudformation.${region}.amazonaws.com"
                    break
                case 'cloudFront':
                    if (async) {
                        if (credentials.AWSAccessKeyId && credentials.AWSSecretKey) client = new AmazonCloudFrontAsyncClient(credentials, clientConfiguration)
                        else client = new AmazonCloudFrontAsyncClient(clientConfiguration)
                    } else {
                        if (credentials.AWSAccessKeyId && credentials.AWSSecretKey) client = new AmazonCloudFrontClient(credentials, clientConfiguration)
                        else  client = new AmazonCloudFrontClient(clientConfiguration)
                    }
                    client.endpoint = "cloudfront.amazonaws.com"
                    break
                case 'cloudSearch':
                    if (async) {
                        if (credentials.AWSAccessKeyId && credentials.AWSSecretKey) client = new AmazonCloudSearchAsyncClient(credentials, clientConfiguration)
                        else client = new AmazonCloudSearchAsyncClient(clientConfiguration)
                    } else {
                        if (credentials.AWSAccessKeyId && credentials.AWSSecretKey) client = new AmazonCloudSearchClient(credentials, clientConfiguration)
                        else client = new AmazonCloudSearchClient(clientConfiguration)
                    }
                    client.endpoint = "cloudsearch.${region}.amazonaws.com"
                    break
                case 'cloudWatch':
                    if (async) {
                        if (credentials.AWSAccessKeyId && credentials.AWSSecretKey) client = new AmazonCloudWatchAsyncClient(credentials, clientConfiguration)
                        else client = new AmazonCloudWatchAsyncClient(clientConfiguration)
                    } else {
                        if (credentials.AWSAccessKeyId && credentials.AWSSecretKey) client = new AmazonCloudWatchClient(credentials, clientConfiguration)
                        else client = new AmazonCloudWatchClient(clientConfiguration)
                    }
                    client.endpoint = "monitoring.${region}.amazonaws.com"
                    break
                case 'dynamoDB':
                    if (async) {
                        if (credentials.AWSAccessKeyId && credentials.AWSSecretKey) client = new AmazonDynamoDBAsyncClient(credentials, clientConfiguration)
                        else client = new AmazonDynamoDBAsyncClient(clientConfiguration)
                    } else {
                        if (credentials.AWSAccessKeyId && credentials.AWSSecretKey) client = new AmazonDynamoDBClient(credentials, clientConfiguration)
                        else client = new AmazonDynamoDBClient(clientConfiguration)
                    }
                    client.endpoint = "dynamodb.${region}.amazonaws.com"
                    break
                case 'ec2':
                    if (async) {
                        if (credentials.AWSAccessKeyId && credentials.AWSSecretKey) client = new AmazonEC2AsyncClient(credentials, clientConfiguration)
                        else client = new AmazonEC2AsyncClient(clientConfiguration)
                    } else {
                        if (credentials.AWSAccessKeyId && credentials.AWSSecretKey) client = new AmazonEC2Client(credentials, clientConfiguration)
                        else client = new AmazonEC2Client(clientConfiguration)
                    }
                    client.endpoint = "ec2.${region}.amazonaws.com"
                    break
                case 'elasticBeanstalk':
                    if (async) {
                        if (credentials.AWSAccessKeyId && credentials.AWSSecretKey) client = new AWSElasticBeanstalkAsyncClient(credentials, clientConfiguration)
                        else client = new AWSElasticBeanstalkAsyncClient(clientConfiguration)
                    } else {
                        if (credentials.AWSAccessKeyId && credentials.AWSSecretKey) client = new AWSElasticBeanstalkClient(credentials, clientConfiguration)
                        else client = new AWSElasticBeanstalkClient(clientConfiguration)
                    }
                    client.endpoint = "elasticbeanstalk.${region}.amazonaws.com"
                    break
                case 'elastiCache':
                    if (async) {
                        if (credentials.AWSAccessKeyId && credentials.AWSSecretKey) client = new AmazonElastiCacheAsyncClient(credentials, clientConfiguration)
                        else client = new AmazonElastiCacheAsyncClient(clientConfiguration)
                    } else {
                        if (credentials.AWSAccessKeyId && credentials.AWSSecretKey) client = new AmazonElastiCacheClient(credentials, clientConfiguration)
                        else client = new AmazonElastiCacheClient(clientConfiguration)
                    }
                    client.endpoint = "elasticache.${region}.amazonaws.com"
                    break
                case 'elasticLoadBalancing':
                    if (async) {
                        if (credentials.AWSAccessKeyId && credentials.AWSSecretKey) client = new AmazonElasticLoadBalancingAsyncClient(credentials, clientConfiguratio)
                    } else {
                        if (credentials.AWSAccessKeyId && credentials.AWSSecretKey) client = new AmazonElasticLoadBalancingClient(credentials, clientConfiguration)
                        else client = new AmazonElasticLoadBalancingClient(clientConfiguration)
                    }
                    client.endpoint = "elasticloadbalancing.${region}.amazonaws.com"
                    break
                case 'elasticMapReduce':
                    if (async) {
                        if (credentials.AWSAccessKeyId && credentials.AWSSecretKey) client = new AmazonElasticMapReduceAsyncClient(credentials, clientConfiguration)
                        else client = new AmazonElasticMapReduceAsyncClient(clientConfiguration)
                    } else {
                        if (credentials.AWSAccessKeyId && credentials.AWSSecretKey) client = new AmazonElasticMapReduceClient(credentials, clientConfiguration)
                        else client = new AmazonElasticMapReduceClient(clientConfiguration)
                    }
                    client.endpoint = "elasticmapreduce.${region}.amazonaws.com"
                    break
                case 'iam':
                    if (async) {
                        if (credentials.AWSAccessKeyId && credentials.AWSSecretKey) client = new AmazonIdentityManagementAsyncClient(credentials, clientConfiguration)
                        else client = new AmazonIdentityManagementAsyncClient(clientConfiguration)
                    } else {
                        if (credentials.AWSAccessKeyId && credentials.AWSSecretKey) client = new AmazonIdentityManagementClient(credentials, clientConfiguration)
                        else client = new AmazonIdentityManagementClient(clientConfiguration)
                    }
                    client.endpoint = "iam.amazonaws.com"
                    break
                case 'importExport':
                    if (async) {
                        if (credentials.AWSAccessKeyId && credentials.AWSSecretKey) client = new AmazonImportExportAsyncClient(credentials, clientConfiguration)
                        else client = new AmazonImportExportAsyncClient(clientConfiguration)
                    } else {
                        if (credentials.AWSAccessKeyId && credentials.AWSSecretKey) client = new AmazonImportExportClient(credentials, clientConfiguration)
                        else client = new AmazonImportExportClient(clientConfiguration)
                    }
                    client.endpoint = "importexport.amazonaws.com"
                    break
                case 'rds':
                    if (async) {
                        if (credentials.AWSAccessKeyId && credentials.AWSSecretKey) client = new AmazonRDSAsyncClient(credentials, clientConfiguration)
                        else client = new AmazonRDSAsyncClient(clientConfiguration)
                    } else {
                        if (credentials.AWSAccessKeyId && credentials.AWSSecretKey) client = new AmazonRDSClient(credentials, clientConfiguration)
                        else client = new AmazonRDSClient(clientConfiguration)
                    }
                    client.endpoint = "rds.${region}.amazonaws.com"
                    break
                case 'route53':
                    if (async) {
                        if (credentials.AWSAccessKeyId && credentials.AWSSecretKey) client = new AmazonRoute53AsyncClient(credentials, clientConfiguration)
                        else client = new AmazonRoute53AsyncClient(clientConfiguration)
                    } else {
                        if (credentials.AWSAccessKeyId && credentials.AWSSecretKey) client = new AmazonRoute53Client(credentials, clientConfiguration)
                        else client = new AmazonRoute53Client(clientConfiguration)
                    }
                    client.endpoint = "route53.amazonaws.com"
                    break
                case 's3':
                    if (async) throw new Exception("Sorry, there is no async client for AmazonS3")
                    if (credentials.AWSAccessKeyId && credentials.AWSSecretKey) client = new AmazonS3Client(credentials, clientConfiguration)
                    else client = new AmazonS3Client(clientConfiguration)
                    if (region == 'us' || region == defaultRegion) {
                        client.endpoint = "s3.amazonaws.com"
                    } else {
                        client.endpoint = "s3-${region}.amazonaws.com"
                    }
                    break
                case 'sdb':
                    if (async) {
                        if (credentials.AWSAccessKeyId && credentials.AWSSecretKey) client = new AmazonSimpleDBAsyncClient(credentials, clientConfiguration)
                        else client = new AmazonSimpleDBAsyncClient(clientConfiguration)
                    } else {
                        if (credentials.AWSAccessKeyId && credentials.AWSSecretKey) client = new AmazonSimpleDBClient(credentials, clientConfiguration)
                        else client = new AmazonSimpleDBClient(clientConfiguration)
                    }
                    if (region == 'us-east-1') {
                        client.endpoint = "sdb.amazonaws.com"
                    } else {
                        client.endpoint = "sdb.${region}.amazonaws.com"
                    }
                    break
                case 'ses':
                    if (async) {
                        if (credentials.AWSAccessKeyId && credentials.AWSSecretKey) client = new AmazonSimpleEmailServiceAsyncClient(credentials, clientConfiguration)
                        else client = new AmazonSimpleEmailServiceAsyncClient(clientConfiguration)
                    } else {
                        if (credentials.AWSAccessKeyId && credentials.AWSSecretKey) client = new AmazonSimpleEmailServiceClient(credentials, clientConfiguration)
                        else client = new AmazonSimpleEmailServiceClient(clientConfiguration)
                    }
                    client.endpoint = "email.${region}.amazonaws.com"
                    break
                case 'sns':
                    if (async) {
                        if (credentials.AWSAccessKeyId && credentials.AWSSecretKey) client = new AmazonSNSAsyncClient(credentials, clientConfiguration)
                        else client = new AmazonSNSAsyncClient(clientConfiguration)
                    } else {
                        if (credentials.AWSAccessKeyId && credentials.AWSSecretKey) client = new AmazonSNSClient(credentials, clientConfiguration)
                        else client = new AmazonSNSClient(clientConfiguration)
                    }
                    client.endpoint = "sns.${region}.amazonaws.com"
                    break
                case 'sqs':
                    if (async) {
                        if (credentials.AWSAccessKeyId && credentials.AWSSecretKey) client = new AmazonSQSAsyncClient(credentials, clientConfiguration)
                        else client = new AmazonSQSAsyncClient(clientConfiguration)
                    } else {
                        if (credentials.AWSAccessKeyId && credentials.AWSSecretKey) client = new AmazonSQSClient(credentials, clientConfiguration)
                        else client = new AmazonSQSClient(clientConfiguration)
                    }
                    client.endpoint = "sqs.${region}.amazonaws.com"
                    break
                case 'storageGateway':
                    if (async) {
                        if (credentials.AWSAccessKeyId && credentials.AWSSecretKey) client = new AWSStorageGatewayAsyncClient(credentials, clientConfiguration)
                        else client = new AWSStorageGatewayAsyncClient(clientConfiguration)
                    } else {
                        if (credentials.AWSAccessKeyId && credentials.AWSSecretKey) client = new AWSStorageGatewayClient(credentials, clientConfiguration)
                        else client = new AWSStorageGatewayClient(clientConfiguration)
                    }
                    client.endpoint = "storagegateway.${region}.amazonaws.com"
                    break
                case 'swf':
                    if (async) {
                        if (credentials.AWSAccessKeyId && credentials.AWSSecretKey) client = new AmazonSimpleWorkflowAsyncClient(credentials, clientConfiguration)
                        else client = new AmazonSimpleWorkflowAsyncClient(clientConfiguration)
                    } else {
                        if (credentials.AWSAccessKeyId && credentials.AWSSecretKey) client = new AmazonSimpleWorkflowClient(credentials, clientConfiguration)
                        else client = new AmazonSimpleWorkflowClient(clientConfiguration)
                    }
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
