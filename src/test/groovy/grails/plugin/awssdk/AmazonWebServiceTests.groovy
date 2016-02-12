package grails.plugin.awssdk

import com.amazonaws.services.apigateway.AmazonApiGatewayAsyncClient
import com.amazonaws.services.apigateway.AmazonApiGatewayClient
import com.amazonaws.services.autoscaling.AmazonAutoScalingAsyncClient
import com.amazonaws.services.autoscaling.AmazonAutoScalingClient
import com.amazonaws.services.cloudformation.AmazonCloudFormationAsyncClient
import com.amazonaws.services.cloudformation.AmazonCloudFormationClient
import com.amazonaws.services.cloudfront.AmazonCloudFrontAsyncClient
import com.amazonaws.services.cloudfront.AmazonCloudFrontClient
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
import grails.test.mixin.TestFor
import grails.test.mixin.TestMixin
import grails.test.mixin.support.GrailsUnitTestMixin

import java.security.KeyPair
import java.security.KeyPairGenerator
import java.security.SecureRandom

/**
 * See the API for {@link grails.test.mixin.support.GrailsUnitTestMixin} for usage instructions
 */
@TestMixin(GrailsUnitTestMixin)
@TestFor(AmazonWebService)
class AmazonWebServiceTests {

    void setUp() {
    }

    void tearDown() {
    }

    AmazonWebService getServiceWithCredentials() {
        def amazonWebService = new AmazonWebService()

        grailsApplication.config.grails.plugin.awssdk.accessKey = "abcdefghi"
        grailsApplication.config.grails.plugin.awssdk.secretKey = "123456789"

        amazonWebService.grailsApplication = grailsApplication

        amazonWebService
    }

    AmazonWebService getServiceWithCredentialsAndEncryptionMaterials() {
        def amazonWebService = serviceWithCredentials

        KeyPairGenerator keyGenerator = KeyPairGenerator.getInstance("RSA")
        keyGenerator.initialize(1024, new SecureRandom())
        KeyPair myKeyPair = keyGenerator.generateKeyPair()
        EncryptionMaterials encryptionMaterials = new EncryptionMaterials(myKeyPair)

        amazonWebService.grailsApplication.config.grails.plugin.awssdk.encryptionMaterials = encryptionMaterials

        amazonWebService
    }

    AmazonWebService getServiceWithoutCredentials() {
        def amazonWebService = new AmazonWebService()

        amazonWebService.grailsApplication = grailsApplication

        amazonWebService
    }

    void testApiGatewayClientWithCredentials() {
        def amazonWebService = getServiceWithCredentials()

        assert amazonWebService.getApiGatewayAsync().class == AmazonApiGatewayAsyncClient
        assert amazonWebService.getApiGatewayAsync('eu-west-1').class == AmazonApiGatewayAsyncClient
        assert amazonWebService.getApiGateway().class == AmazonApiGatewayClient
        assert amazonWebService.getApiGateway('eu-west-1').class == AmazonApiGatewayClient
        assert amazonWebService.getApiGateway('eu-west-1').endpoint.toString() == 'https://apigateway.eu-west-1.amazonaws.com' // Not supported by eu-west-1
    }

    void testApiGatewayClientWithoutCredentials() {
        def amazonWebService = getServiceWithoutCredentials()

        assert amazonWebService.getApiGatewayAsync().class == AmazonApiGatewayAsyncClient
        assert amazonWebService.getApiGatewayAsync('eu-west-1').class == AmazonApiGatewayAsyncClient
        assert amazonWebService.getApiGateway().class == AmazonApiGatewayClient
        assert amazonWebService.getApiGateway('eu-west-1').class == AmazonApiGatewayClient
        assert amazonWebService.getApiGateway('eu-west-1').endpoint.toString() == 'https://apigateway.eu-west-1.amazonaws.com' // Not supported by eu-west-1
    }

    void testAutoScalingClientWithCredentials() {
        def amazonWebService = getServiceWithCredentials()

        assert amazonWebService.getAutoScalingAsync().class == AmazonAutoScalingAsyncClient
        assert amazonWebService.getAutoScalingAsync('eu-west-1').class == AmazonAutoScalingAsyncClient
        assert amazonWebService.getAutoScaling().class == AmazonAutoScalingClient
        assert amazonWebService.getAutoScaling('eu-west-1').class == AmazonAutoScalingClient
        assert amazonWebService.getAutoScaling('eu-west-1').endpoint.toString() == 'https://autoscaling.eu-west-1.amazonaws.com'
    }

    void testAutoScalingClientWithoutCredentials() {
        def amazonWebService = getServiceWithoutCredentials()

        assert amazonWebService.getAutoScalingAsync().class == AmazonAutoScalingAsyncClient
        assert amazonWebService.getAutoScalingAsync('eu-west-1').class == AmazonAutoScalingAsyncClient
        assert amazonWebService.getAutoScaling().class == AmazonAutoScalingClient
        assert amazonWebService.getAutoScaling('eu-west-1').class == AmazonAutoScalingClient
        assert amazonWebService.getAutoScaling('eu-west-1').endpoint.toString() == 'https://autoscaling.eu-west-1.amazonaws.com'
    }

    void testCloudFormationClientWithCredentials() {
        def amazonWebService = getServiceWithCredentials()

        assert amazonWebService.getCloudFormationAsync().class == AmazonCloudFormationAsyncClient
        assert amazonWebService.getCloudFormationAsync('eu-west-1').class == AmazonCloudFormationAsyncClient
        assert amazonWebService.getCloudFormation().class == AmazonCloudFormationClient
        assert amazonWebService.getCloudFormation('eu-west-1').class == AmazonCloudFormationClient
        assert amazonWebService.getCloudFormation('eu-west-1').endpoint.toString() == 'https://cloudformation.eu-west-1.amazonaws.com'
    }

    void testCloudFormationClientWithoutCredentials() {
        def amazonWebService = getServiceWithoutCredentials()

        assert amazonWebService.getCloudFormationAsync().class == AmazonCloudFormationAsyncClient
        assert amazonWebService.getCloudFormationAsync('eu-west-1').class == AmazonCloudFormationAsyncClient
        assert amazonWebService.getCloudFormation().class == AmazonCloudFormationClient
        assert amazonWebService.getCloudFormation('eu-west-1').class == AmazonCloudFormationClient
        assert amazonWebService.getCloudFormation('eu-west-1').endpoint.toString() == 'https://cloudformation.eu-west-1.amazonaws.com'
    }

    void testCloudFrontClientWithCredentials() {
        def amazonWebService = getServiceWithCredentials()

        assert amazonWebService.getCloudFrontAsync().class == AmazonCloudFrontAsyncClient
        assert amazonWebService.getCloudFrontAsync().endpoint.toString() == 'https://cloudfront.amazonaws.com'
        shouldFail(MissingMethodException) {
            amazonWebService.getCloudFrontAsync('eu-west-1')
        }
        assert amazonWebService.getCloudFront().class == AmazonCloudFrontClient
        assert amazonWebService.getCloudFront().endpoint.toString() == 'https://cloudfront.amazonaws.com'
        shouldFail(MissingMethodException) {
            amazonWebService.getCloudFront('eu-west-1')
        }
    }

    void testCloudFrontClientWithoutCredentials() {
        def amazonWebService = getServiceWithoutCredentials()

        assert amazonWebService.getCloudFrontAsync().class == AmazonCloudFrontAsyncClient
        assert amazonWebService.getCloudFrontAsync().endpoint.toString() == 'https://cloudfront.amazonaws.com'
        shouldFail(MissingMethodException) {
            amazonWebService.getCloudFrontAsync('eu-west-1')
        }
        assert amazonWebService.getCloudFront().class == AmazonCloudFrontClient
        assert amazonWebService.getCloudFront().endpoint.toString() == 'https://cloudfront.amazonaws.com'
        shouldFail(MissingMethodException) {
            amazonWebService.getCloudFront('eu-west-1')
        }
    }

    void testCloudSearchClientWithCredentials() {
        def amazonWebService = getServiceWithCredentials()

        assert amazonWebService.getCloudSearchAsync().class == AmazonCloudSearchAsyncClient
        assert amazonWebService.getCloudSearchAsync('eu-west-1').class == AmazonCloudSearchAsyncClient
        assert amazonWebService.getCloudSearch().class == AmazonCloudSearchClient
        assert amazonWebService.getCloudSearch('eu-west-1').class == AmazonCloudSearchClient
        assert amazonWebService.getCloudSearch('eu-west-1').endpoint.toString() == 'https://cloudsearch.eu-west-1.amazonaws.com'
    }

    void testCloudSearchClientWithoutCredentials() {
        def amazonWebService = getServiceWithoutCredentials()

        assert amazonWebService.getCloudSearchAsync().class == AmazonCloudSearchAsyncClient
        assert amazonWebService.getCloudSearchAsync('eu-west-1').class == AmazonCloudSearchAsyncClient
        assert amazonWebService.getCloudSearch().class == AmazonCloudSearchClient
        assert amazonWebService.getCloudSearch('eu-west-1').class == AmazonCloudSearchClient
        assert amazonWebService.getCloudSearch('eu-west-1').endpoint.toString() == 'https://cloudsearch.eu-west-1.amazonaws.com'
    }

    void testCloudTrailClientWithCredentials() {
        def amazonWebService = getServiceWithCredentials()

        assert amazonWebService.getCloudTrailAsync().class == AWSCloudTrailAsyncClient
        assert amazonWebService.getCloudTrailAsync().endpoint.toString() == 'https://cloudtrail.us-east-1.amazonaws.com'
        /*shouldFail(MissingMethodException) {
           amazonWebService.getCloudTrailAsync('eu-west-1')
        }*/
        assert amazonWebService.getCloudTrail().class == AWSCloudTrailClient
        assert amazonWebService.getCloudTrail().endpoint.toString() == 'https://cloudtrail.us-east-1.amazonaws.com'
        /*shouldFail(MissingMethodException) {
            amazonWebService.getCloudTrail('eu-west-1')
        }*/
    }

    void testCloudTrailClientWithoutCredentials() {
        def amazonWebService = getServiceWithoutCredentials()

        assert amazonWebService.getCloudTrailAsync().class == AWSCloudTrailAsyncClient
        assert amazonWebService.getCloudTrailAsync().endpoint.toString() == 'https://cloudtrail.us-east-1.amazonaws.com'
        /*shouldFail(MissingMethodException) {
            amazonWebService.getCloudTrailAsync('eu-west-1')
        }*/
        assert amazonWebService.getCloudTrail().class == AWSCloudTrailClient
        assert amazonWebService.getCloudTrail().endpoint.toString() == 'https://cloudtrail.us-east-1.amazonaws.com'
        /*shouldFail(MissingMethodException) {
            amazonWebService.getCloudTrail('eu-west-1')
        }*/
    }

    /**
     * CloudWatch
     */
    void testCloudWatchClientWithCredentials() {
        def amazonWebService = getServiceWithCredentials()

        assert amazonWebService.getCloudWatchAsync().class == AmazonCloudWatchAsyncClient
        assert amazonWebService.getCloudWatchAsync('eu-west-1').class == AmazonCloudWatchAsyncClient
        assert amazonWebService.getCloudWatch().class == AmazonCloudWatchClient
        assert amazonWebService.getCloudWatch('eu-west-1').class == AmazonCloudWatchClient
        assert amazonWebService.getCloudWatch('eu-west-1').endpoint.toString() == 'https://monitoring.eu-west-1.amazonaws.com'
    }

    void testCloudWatchClientWithoutCredentials() {
        def amazonWebService = getServiceWithoutCredentials()

        assert amazonWebService.getCloudWatchAsync().class == AmazonCloudWatchAsyncClient
        assert amazonWebService.getCloudWatchAsync('eu-west-1').class == AmazonCloudWatchAsyncClient
        assert amazonWebService.getCloudWatch().class == AmazonCloudWatchClient
        assert amazonWebService.getCloudWatch('eu-west-1').class == AmazonCloudWatchClient
        assert amazonWebService.getCloudWatch('eu-west-1').endpoint.toString() == 'https://monitoring.eu-west-1.amazonaws.com'
    }

    /**
     * CodeDeploy
     */
    void testCodeDeployClientWithCredentials() {
        def amazonWebService = getServiceWithCredentials()

        assert amazonWebService.getCodeDeployAsync().class == AmazonCodeDeployAsyncClient
        assert amazonWebService.getCodeDeployAsync('eu-west-1').class == AmazonCodeDeployAsyncClient
        assert amazonWebService.getCodeDeploy().class == AmazonCodeDeployClient
        assert amazonWebService.getCodeDeploy('eu-west-1').class == AmazonCodeDeployClient
        assert amazonWebService.getCodeDeploy('eu-west-1').endpoint.toString() == 'https://codedeploy.eu-west-1.amazonaws.com'
    }

    void testCodeDeployClientWithoutCredentials() {
        def amazonWebService = getServiceWithoutCredentials()

        assert amazonWebService.getCodeDeployAsync().class == AmazonCodeDeployAsyncClient
        assert amazonWebService.getCodeDeployAsync('eu-west-1').class == AmazonCodeDeployAsyncClient
        assert amazonWebService.getCodeDeploy().class == AmazonCodeDeployClient
        assert amazonWebService.getCodeDeploy('eu-west-1').class == AmazonCodeDeployClient
        assert amazonWebService.getCodeDeploy('eu-west-1').endpoint.toString() == 'https://codedeploy.eu-west-1.amazonaws.com'
    }

    /**
     * CognitoIdentity
     */
    void testCognitoIdentityClientWithCredentials() {
        def amazonWebService = getServiceWithCredentials()

        assert amazonWebService.getCognitoIdentityAsync().class == AmazonCognitoIdentityAsyncClient
        assert amazonWebService.getCognitoIdentityAsync('eu-west-1').class == AmazonCognitoIdentityAsyncClient
        assert amazonWebService.getCognitoIdentity().class == AmazonCognitoIdentityClient
        assert amazonWebService.getCognitoIdentity('eu-west-1').class == AmazonCognitoIdentityClient
        assert amazonWebService.getCognitoIdentity('eu-west-1').endpoint.toString() == 'https://cognito-identity.eu-west-1.amazonaws.com'
    }

    void testCognitoIdentityClientWithoutCredentials() {
        def amazonWebService = getServiceWithoutCredentials()

        assert amazonWebService.getCognitoIdentityAsync().class == AmazonCognitoIdentityAsyncClient
        assert amazonWebService.getCognitoIdentityAsync('eu-west-1').class == AmazonCognitoIdentityAsyncClient
        assert amazonWebService.getCognitoIdentity().class == AmazonCognitoIdentityClient
        assert amazonWebService.getCognitoIdentity('eu-west-1').class == AmazonCognitoIdentityClient
        assert amazonWebService.getCognitoIdentity('eu-west-1').endpoint.toString() == 'https://cognito-identity.eu-west-1.amazonaws.com'
    }

    /**
     * CognitoSync
     */
    void testCognitoSyncClientWithCredentials() {
        def amazonWebService = getServiceWithCredentials()

        assert amazonWebService.getCognitoSyncAsync().class == AmazonCognitoSyncAsyncClient
        assert amazonWebService.getCognitoSyncAsync('eu-west-1').class == AmazonCognitoSyncAsyncClient
        assert amazonWebService.getCognitoSync().class == AmazonCognitoSyncClient
        assert amazonWebService.getCognitoSync('eu-west-1').class == AmazonCognitoSyncClient
        assert amazonWebService.getCognitoSync('eu-west-1').endpoint.toString() == 'https://cognito-sync.eu-west-1.amazonaws.com'
    }

    void testCognitoSyncClientWithoutCredentials() {
        def amazonWebService = getServiceWithoutCredentials()

        assert amazonWebService.getCognitoSyncAsync().class == AmazonCognitoSyncAsyncClient
        assert amazonWebService.getCognitoSyncAsync('eu-west-1').class == AmazonCognitoSyncAsyncClient
        assert amazonWebService.getCognitoSync().class == AmazonCognitoSyncClient
        assert amazonWebService.getCognitoSync('eu-west-1').class == AmazonCognitoSyncClient
        assert amazonWebService.getCognitoSync('eu-west-1').endpoint.toString() == 'https://cognito-sync.eu-west-1.amazonaws.com'
    }

    /**
     * Config
     */
    void testConfigClientWithCredentials() {
        def amazonWebService = getServiceWithCredentials()

        assert amazonWebService.getConfigAsync().class == AmazonConfigAsyncClient
        assert amazonWebService.getConfigAsync('eu-west-1').class == AmazonConfigAsyncClient
        assert amazonWebService.getConfig().class == AmazonConfigClient
        assert amazonWebService.getConfig('eu-west-1').class == AmazonConfigClient
        assert amazonWebService.getConfig('eu-west-1').endpoint.toString() == 'https://config.eu-west-1.amazonaws.com'
    }

    void testConfigClientWithoutCredentials() {
        def amazonWebService = getServiceWithoutCredentials()

        assert amazonWebService.getConfigAsync().class == AmazonConfigAsyncClient
        assert amazonWebService.getConfigAsync('eu-west-1').class == AmazonConfigAsyncClient
        assert amazonWebService.getConfig().class == AmazonConfigClient
        assert amazonWebService.getConfig('eu-west-1').class == AmazonConfigClient
        assert amazonWebService.getConfig('eu-west-1').endpoint.toString() == 'https://config.eu-west-1.amazonaws.com'
    }

    /**
     * DynamoDB
     */
    void testDynamoDBClientWithCredentials() {
        def amazonWebService = getServiceWithCredentials()

        assert amazonWebService.getDynamoDBAsync().class == AmazonDynamoDBAsyncClient
        assert amazonWebService.getDynamoDBAsync('eu-west-1').class == AmazonDynamoDBAsyncClient
        assert amazonWebService.getDynamoDB().class == AmazonDynamoDBClient
        assert amazonWebService.getDynamoDB('eu-west-1').class == AmazonDynamoDBClient
        assert amazonWebService.getDynamoDB('eu-west-1').endpoint.toString() == 'https://dynamodb.eu-west-1.amazonaws.com'
    }

    void testDynamoDBClientWithoutCredentials() {
        def amazonWebService = getServiceWithoutCredentials()

        assert amazonWebService.getDynamoDBAsync().class == AmazonDynamoDBAsyncClient
        assert amazonWebService.getDynamoDBAsync('eu-west-1').class == AmazonDynamoDBAsyncClient
        assert amazonWebService.getDynamoDB().class == AmazonDynamoDBClient
        assert amazonWebService.getDynamoDB('eu-west-1').class == AmazonDynamoDBClient
        assert amazonWebService.getDynamoDB('eu-west-1').endpoint.toString() == 'https://dynamodb.eu-west-1.amazonaws.com'
    }

    /**
     * Ec2
     */
    void testEc2ClientWithCredentials() {
        def amazonWebService = getServiceWithCredentials()

        assert amazonWebService.getEc2Async().class == AmazonEC2AsyncClient
        assert amazonWebService.getEc2Async('eu-west-1').class == AmazonEC2AsyncClient
        assert amazonWebService.getEc2().class == AmazonEC2Client
        assert amazonWebService.getEc2('eu-west-1').class == AmazonEC2Client
        assert amazonWebService.getEc2('eu-west-1').endpoint.toString() == 'https://ec2.eu-west-1.amazonaws.com'
    }

    void testEc2ClientWithoutCredentials() {
        def amazonWebService = getServiceWithoutCredentials()

        assert amazonWebService.getEc2Async().class == AmazonEC2AsyncClient
        assert amazonWebService.getEc2Async('eu-west-1').class == AmazonEC2AsyncClient
        assert amazonWebService.getEc2().class == AmazonEC2Client
        assert amazonWebService.getEc2('eu-west-1').class == AmazonEC2Client
        assert amazonWebService.getEc2('eu-west-1').endpoint.toString() == 'https://ec2.eu-west-1.amazonaws.com'
    }

    /**
     * ElasticBeanstalk
     */
    void testElasticBeanstalkClientWithCredentials() {
        def amazonWebService = getServiceWithCredentials()

        assert amazonWebService.getElasticBeanstalkAsync().class == AWSElasticBeanstalkAsyncClient
        assert amazonWebService.getElasticBeanstalkAsync('eu-west-1').class == AWSElasticBeanstalkAsyncClient
        assert amazonWebService.getElasticBeanstalk().class == AWSElasticBeanstalkClient
        assert amazonWebService.getElasticBeanstalk('eu-west-1').class == AWSElasticBeanstalkClient
        assert amazonWebService.getElasticBeanstalk('eu-west-1').endpoint.toString() == 'https://elasticbeanstalk.eu-west-1.amazonaws.com'
    }

    void testElasticBeanstalkClientWithoutCredentials() {
        def amazonWebService = getServiceWithoutCredentials()

        assert amazonWebService.getElasticBeanstalkAsync().class == AWSElasticBeanstalkAsyncClient
        assert amazonWebService.getElasticBeanstalkAsync('eu-west-1').class == AWSElasticBeanstalkAsyncClient
        assert amazonWebService.getElasticBeanstalk().class == AWSElasticBeanstalkClient
        assert amazonWebService.getElasticBeanstalk('eu-west-1').class == AWSElasticBeanstalkClient
        assert amazonWebService.getElasticBeanstalk('eu-west-1').endpoint.toString() == 'https://elasticbeanstalk.eu-west-1.amazonaws.com'
    }

    /**
     * ElastiCache
     */
    void testElastiCacheClientWithCredentials() {
        def amazonWebService = getServiceWithCredentials()

        assert amazonWebService.getElastiCacheAsync().class == AmazonElastiCacheAsyncClient
        assert amazonWebService.getElastiCacheAsync('eu-west-1').class == AmazonElastiCacheAsyncClient
        assert amazonWebService.getElastiCache().class == AmazonElastiCacheClient
        assert amazonWebService.getElastiCache('eu-west-1').class == AmazonElastiCacheClient
        assert amazonWebService.getElastiCache('eu-west-1').endpoint.toString() == 'https://elasticache.eu-west-1.amazonaws.com'
    }

    void testElastiCacheClientWithoutCredentials() {
        def amazonWebService = getServiceWithoutCredentials()

        assert amazonWebService.getElastiCacheAsync().class == AmazonElastiCacheAsyncClient
        assert amazonWebService.getElastiCacheAsync('eu-west-1').class == AmazonElastiCacheAsyncClient
        assert amazonWebService.getElastiCache().class == AmazonElastiCacheClient
        assert amazonWebService.getElastiCache('eu-west-1').class == AmazonElastiCacheClient
        assert amazonWebService.getElastiCache('eu-west-1').endpoint.toString() == 'https://elasticache.eu-west-1.amazonaws.com'
    }

    /**
     * ElasticLoadBalancing
     */
    void testElasticLoadBalancingClientWithCredentials() {
        def amazonWebService = getServiceWithCredentials()

        assert amazonWebService.getElasticLoadBalancingAsync().class == AmazonElasticLoadBalancingAsyncClient
        assert amazonWebService.getElasticLoadBalancingAsync('eu-west-1').class == AmazonElasticLoadBalancingAsyncClient
        assert amazonWebService.getElasticLoadBalancing().class == AmazonElasticLoadBalancingClient
        assert amazonWebService.getElasticLoadBalancing('eu-west-1').class == AmazonElasticLoadBalancingClient
        assert amazonWebService.getElasticLoadBalancing('eu-west-1').endpoint.toString() == 'https://elasticloadbalancing.eu-west-1.amazonaws.com'
    }

    void testElasticLoadBalancingClientWithoutCredentials() {
        def amazonWebService = getServiceWithoutCredentials()

        assert amazonWebService.getElasticLoadBalancingAsync().class == AmazonElasticLoadBalancingAsyncClient
        assert amazonWebService.getElasticLoadBalancingAsync('eu-west-1').class == AmazonElasticLoadBalancingAsyncClient
        assert amazonWebService.getElasticLoadBalancing().class == AmazonElasticLoadBalancingClient
        assert amazonWebService.getElasticLoadBalancing('eu-west-1').class == AmazonElasticLoadBalancingClient
        assert amazonWebService.getElasticLoadBalancing('eu-west-1').endpoint.toString() == 'https://elasticloadbalancing.eu-west-1.amazonaws.com'
    }

    /**
     * ElasticMapReduce
     */
    void testElasticMapReduceClientWithCredentials() {
        def amazonWebService = getServiceWithCredentials()

        assert amazonWebService.getElasticMapReduceAsync().class == AmazonElasticMapReduceAsyncClient
        assert amazonWebService.getElasticMapReduceAsync('eu-west-1').class == AmazonElasticMapReduceAsyncClient
        assert amazonWebService.getElasticMapReduce().class == AmazonElasticMapReduceClient
        assert amazonWebService.getElasticMapReduce('eu-west-1').class == AmazonElasticMapReduceClient
        assert amazonWebService.getElasticMapReduce('eu-west-1').endpoint.toString() == 'https://elasticmapreduce.eu-west-1.amazonaws.com'
    }

    void testElasticMapReduceClientWithoutCredentials() {
        def amazonWebService = getServiceWithoutCredentials()

        assert amazonWebService.getElasticMapReduceAsync().class == AmazonElasticMapReduceAsyncClient
        assert amazonWebService.getElasticMapReduceAsync('eu-west-1').class == AmazonElasticMapReduceAsyncClient
        assert amazonWebService.getElasticMapReduce().class == AmazonElasticMapReduceClient
        assert amazonWebService.getElasticMapReduce('eu-west-1').class == AmazonElasticMapReduceClient
        assert amazonWebService.getElasticMapReduce('eu-west-1').endpoint.toString() == 'https://elasticmapreduce.eu-west-1.amazonaws.com'
    }

    /**
     * ElasticTranscoder
     */
    void testElasticTranscoderClientWithCredentials() {
        def amazonWebService = getServiceWithCredentials()

        assert amazonWebService.getElasticTranscoderAsync().class == AmazonElasticTranscoderAsyncClient
        assert amazonWebService.getElasticTranscoderAsync('eu-west-1').class == AmazonElasticTranscoderAsyncClient
        assert amazonWebService.getElasticTranscoder().class == AmazonElasticTranscoderClient
        assert amazonWebService.getElasticTranscoder('eu-west-1').class == AmazonElasticTranscoderClient
        assert amazonWebService.getElasticTranscoder('eu-west-1').endpoint.toString() == 'https://elastictranscoder.eu-west-1.amazonaws.com'
    }

    void testElasticTranscoderClientWithoutCredentials() {
        def amazonWebService = getServiceWithoutCredentials()

        assert amazonWebService.getElasticTranscoderAsync().class == AmazonElasticTranscoderAsyncClient
        assert amazonWebService.getElasticTranscoderAsync('eu-west-1').class == AmazonElasticTranscoderAsyncClient
        assert amazonWebService.getElasticTranscoder().class == AmazonElasticTranscoderClient
        assert amazonWebService.getElasticTranscoder('eu-west-1').class == AmazonElasticTranscoderClient
        assert amazonWebService.getElasticTranscoder('eu-west-1').endpoint.toString() == 'https://elastictranscoder.eu-west-1.amazonaws.com'
    }

    /**
     * Glacier
     */
    void testGlacierClientWithCredentials() {
        def amazonWebService = getServiceWithCredentials()

        assert amazonWebService.getGlacierAsync().class == AmazonGlacierAsyncClient
        assert amazonWebService.getGlacierAsync('eu-west-1').class == AmazonGlacierAsyncClient
        assert amazonWebService.getGlacier().class == AmazonGlacierClient
        assert amazonWebService.getGlacier('eu-west-1').class == AmazonGlacierClient
        assert amazonWebService.getGlacier('eu-west-1').endpoint.toString() == 'https://glacier.eu-west-1.amazonaws.com'
    }

    void testGlacierClientWithoutCredentials() {
        def amazonWebService = getServiceWithoutCredentials()

        assert amazonWebService.getGlacierAsync().class == AmazonGlacierAsyncClient
        assert amazonWebService.getGlacierAsync('eu-west-1').class == AmazonGlacierAsyncClient
        assert amazonWebService.getGlacier().class == AmazonGlacierClient
        assert amazonWebService.getGlacier('eu-west-1').class == AmazonGlacierClient
        assert amazonWebService.getGlacier('eu-west-1').endpoint.toString() == 'https://glacier.eu-west-1.amazonaws.com'
    }

    /**
     * Iam
     */
    void testIamClientWithCredentials() {
        def amazonWebService = getServiceWithCredentials()

        assert amazonWebService.getIamAsync().class == AmazonIdentityManagementAsyncClient
        shouldFail(MissingMethodException) {
            amazonWebService.getIamAsync('eu-west-1')
        }
        assert amazonWebService.getIam().class == AmazonIdentityManagementClient
        assert amazonWebService.getIam().endpoint.toString() == 'https://iam.amazonaws.com'
        shouldFail(MissingMethodException) {
            amazonWebService.getIam('eu-west-1')
        }
    }

    void testIamClientWithoutCredentials() {
        def amazonWebService = getServiceWithoutCredentials()

        assert amazonWebService.getIamAsync().class == AmazonIdentityManagementAsyncClient
        shouldFail(MissingMethodException) {
            amazonWebService.getIamAsync('eu-west-1')
        }
        assert amazonWebService.getIam().class == AmazonIdentityManagementClient
        assert amazonWebService.getIam().endpoint.toString() == 'https://iam.amazonaws.com'
        shouldFail(MissingMethodException) {
            amazonWebService.getIam('eu-west-1')
        }
    }

    /**
     * ImportExport
     */
    void testImportExportClientWithCredentials() {
        def amazonWebService = getServiceWithCredentials()

        assert amazonWebService.getImportExportAsync().class == AmazonImportExportAsyncClient
        shouldFail(MissingMethodException) {
            amazonWebService.getImportExportAsync('eu-west-1')
        }
        assert amazonWebService.getImportExport().class == AmazonImportExportClient
        assert amazonWebService.getImportExport().endpoint.toString() == 'https://importexport.amazonaws.com'
        shouldFail(MissingMethodException) {
            amazonWebService.getImportExport('eu-west-1')
        }
    }

    void testImportExportClientWithoutCredentials() {
        def amazonWebService = getServiceWithoutCredentials()

        assert amazonWebService.getImportExportAsync().class == AmazonImportExportAsyncClient
        shouldFail(MissingMethodException) {
            amazonWebService.getImportExportAsync('eu-west-1')
        }
        assert amazonWebService.getImportExport().class == AmazonImportExportClient
        assert amazonWebService.getImportExport().endpoint.toString() == 'https://importexport.amazonaws.com'
        shouldFail(MissingMethodException) {
            amazonWebService.getImportExport('eu-west-1')
        }
    }

    /**
     * Kinesis
     */
    void testKinesisAsyncWithCredentials() {
        def amazonWebService = getServiceWithCredentials()

        assert amazonWebService.getKinesisAsync().class == AmazonKinesisAsyncClient
        assert amazonWebService.getKinesisAsync().endpoint.toString() == 'https://kinesis.us-east-1.amazonaws.com'
        /*shouldFail(MissingMethodException) {
            amazonWebService.getKinesisAsync('eu-west-1')
        }*/
        assert amazonWebService.getKinesis().class == AmazonKinesisClient
        assert amazonWebService.getKinesis().endpoint.toString() == 'https://kinesis.us-east-1.amazonaws.com'
        /*shouldFail(MissingMethodException) {
            amazonWebService.getKinesis('eu-west-1')
        }*/
    }

    void testKinesisAsyncWithoutCredentials() {
        def amazonWebService = getServiceWithoutCredentials()

        assert amazonWebService.getKinesisAsync().class == AmazonKinesisAsyncClient
        assert amazonWebService.getKinesisAsync().endpoint.toString() == 'https://kinesis.us-east-1.amazonaws.com'
        /*shouldFail(MissingMethodException) {
            amazonWebService.getKinesisAsync('eu-west-1')
        }*/
        assert amazonWebService.getKinesis().class == AmazonKinesisClient
        assert amazonWebService.getKinesis().endpoint.toString() == 'https://kinesis.us-east-1.amazonaws.com'
        /*shouldFail(MissingMethodException) {
            amazonWebService.getKinesis('eu-west-1')
        }*/
    }

    /**
     * KMS
     */
    void testKMSClientWithCredentials() {
        def amazonWebService = getServiceWithCredentials()

        assert amazonWebService.getKMSAsync().class == AWSKMSAsyncClient
        assert amazonWebService.getKMSAsync('eu-west-1').class == AWSKMSAsyncClient
        assert amazonWebService.getKMS().class == AWSKMSClient
        assert amazonWebService.getKMS('eu-west-1').class == AWSKMSClient
        assert amazonWebService.getKMS('eu-west-1').endpoint.toString() == 'https://kms.eu-west-1.amazonaws.com'
    }

    void testKMSClientWithoutCredentials() {
        def amazonWebService = getServiceWithoutCredentials()

        assert amazonWebService.getKMSAsync().class == AWSKMSAsyncClient
        assert amazonWebService.getKMSAsync('eu-west-1').class == AWSKMSAsyncClient
        assert amazonWebService.getKMS().class == AWSKMSClient
        assert amazonWebService.getKMS('eu-west-1').class == AWSKMSClient
        assert amazonWebService.getKMS('eu-west-1').endpoint.toString() == 'https://kms.eu-west-1.amazonaws.com'
    }

    /**
     * OpsWorks
     */
    void testOpsWorksAsyncWithCredentials() {
        def amazonWebService = getServiceWithCredentials()

        assert amazonWebService.getOpsWorksAsync().class == AWSOpsWorksAsyncClient
        shouldFail(MissingMethodException) {
            amazonWebService.getOpsWorksAsync('eu-west-1')
        }
        assert amazonWebService.getOpsWorks().class == AWSOpsWorksClient
        assert amazonWebService.getOpsWorks().endpoint.toString() == 'https://opsworks.us-east-1.amazonaws.com'
        shouldFail(MissingMethodException) {
            amazonWebService.getOpsWorks('eu-west-1')
        }
    }

    void testOpsWorksAsyncWithoutCredentials() {
        def amazonWebService = getServiceWithoutCredentials()

        assert amazonWebService.getOpsWorksAsync().class == AWSOpsWorksAsyncClient
        shouldFail(MissingMethodException) {
            amazonWebService.getOpsWorksAsync('eu-west-1')
        }
        assert amazonWebService.getOpsWorks().class == AWSOpsWorksClient
        assert amazonWebService.getOpsWorks().endpoint.toString() == 'https://opsworks.us-east-1.amazonaws.com'
        shouldFail(MissingMethodException) {
            amazonWebService.getOpsWorks('eu-west-1')
        }
    }

    /**
     * Rds
     */
    void testRdsClientWithCredentials() {
        def amazonWebService = getServiceWithCredentials()

        assert amazonWebService.getRdsAsync().class == AmazonRDSAsyncClient
        assert amazonWebService.getRdsAsync('eu-west-1').class == AmazonRDSAsyncClient
        assert amazonWebService.getRds().class == AmazonRDSClient
        assert amazonWebService.getRds('eu-west-1').class == AmazonRDSClient
        assert amazonWebService.getRds('eu-west-1').endpoint.toString() == 'https://rds.eu-west-1.amazonaws.com'
    }

    void testRdsClientWithoutCredentials() {
        def amazonWebService = getServiceWithoutCredentials()

        assert amazonWebService.getRdsAsync().class == AmazonRDSAsyncClient
        assert amazonWebService.getRdsAsync('eu-west-1').class == AmazonRDSAsyncClient
        assert amazonWebService.getRds().class == AmazonRDSClient
        assert amazonWebService.getRds('eu-west-1').class == AmazonRDSClient
        assert amazonWebService.getRds('eu-west-1').endpoint.toString() == 'https://rds.eu-west-1.amazonaws.com'
    }

    /**
     * Redshift
     */
    void testRedshiftClientWithCredentials() {
        def amazonWebService = getServiceWithCredentials()

        assert amazonWebService.getRedshiftAsync().class == AmazonRedshiftAsyncClient
        shouldFail(MissingMethodException) {
            amazonWebService.getRedshiftAsync('eu-west-1')
        }
        assert amazonWebService.getRedshift().class == AmazonRedshiftClient
        assert amazonWebService.getRedshift().endpoint.toString() == 'https://redshift.us-east-1.amazonaws.com'
        shouldFail(MissingMethodException) {
            amazonWebService.getRedshift('eu-west-1')
        }
    }

    void testRedshiftClientWithoutCredentials() {
        def amazonWebService = getServiceWithoutCredentials()

        assert amazonWebService.getRedshiftAsync().class == AmazonRedshiftAsyncClient
        shouldFail(MissingMethodException) {
            amazonWebService.getRedshiftAsync('eu-west-1')
        }
        assert amazonWebService.getRedshift().class == AmazonRedshiftClient
        assert amazonWebService.getRedshift().endpoint.toString() == 'https://redshift.us-east-1.amazonaws.com'
        shouldFail(MissingMethodException) {
            amazonWebService.getRedshift('eu-west-1')
        }
    }

    /**
     * Route53
     */
    void testRoute53ClientWithCredentials() {
        def amazonWebService = getServiceWithCredentials()

        assert amazonWebService.getRoute53Async().class == AmazonRoute53AsyncClient
        shouldFail(MissingMethodException) {
            amazonWebService.getRoute53Async('eu-west-1')
        }
        assert amazonWebService.getRoute53().class == AmazonRoute53Client
        assert amazonWebService.getRoute53().endpoint.toString() == 'https://route53.amazonaws.com'
        shouldFail(MissingMethodException) {
            amazonWebService.getRoute53('eu-west-1')
        }
    }

    void testRoute53ClientWithoutCredentials() {
        def amazonWebService = getServiceWithoutCredentials()

        assert amazonWebService.getRoute53Async().class == AmazonRoute53AsyncClient
        shouldFail(MissingMethodException) {
            amazonWebService.getRoute53Async('eu-west-1')
        }
        assert amazonWebService.getRoute53().class == AmazonRoute53Client
        assert amazonWebService.getRoute53().endpoint.toString() == 'https://route53.amazonaws.com'
        shouldFail(MissingMethodException) {
            amazonWebService.getRoute53('eu-west-1')
        }
    }

    /**
     * S3
     */
    void testS3ClientWithCredentials() {
        def amazonWebService = getServiceWithCredentials()

        shouldFail(MissingMethodException) {
            amazonWebService.getS3Async()
        }
        shouldFail(MissingMethodException) {
            amazonWebService.getS3Async('eu-west-1')
        }
        assert amazonWebService.getS3().class == AmazonS3Client
        assert amazonWebService.getS3().endpoint.toString() == 'https://s3.amazonaws.com'
        assert amazonWebService.getS3('eu-west-1').class == AmazonS3Client
        assert amazonWebService.getS3('eu-west-1').endpoint.toString() == 'https://s3-eu-west-1.amazonaws.com'
    }

    void testS3ClientWithoutCredentials() {
        def amazonWebService = getServiceWithoutCredentials()

        shouldFail(MissingMethodException) {
            amazonWebService.getS3Async()
        }
        shouldFail(MissingMethodException) {
            amazonWebService.getS3Async('eu-west-1')
        }
        assert amazonWebService.getS3().class == AmazonS3Client
        assert amazonWebService.getS3().endpoint.toString() == 'https://s3.amazonaws.com'
        assert amazonWebService.getS3('eu-west-1').class == AmazonS3Client
        assert amazonWebService.getS3('eu-west-1').endpoint.toString() == 'https://s3-eu-west-1.amazonaws.com'
    }

    /**
     * S3Entryption
     */
    void testS3EncryptionClientWithCredentials() {
        def amazonWebService = getServiceWithCredentialsAndEncryptionMaterials()

        shouldFail(MissingMethodException) {
            amazonWebService.getS3EncryptionAsync()
        }
        shouldFail(MissingMethodException) {
            amazonWebService.getS3EncryptionAsync('eu-west-1')
        }
        assert amazonWebService.getS3Encryption().class == AmazonS3EncryptionClient
        assert amazonWebService.getS3Encryption().endpoint.toString() == 'https://s3.amazonaws.com'
        assert amazonWebService.getS3Encryption('eu-west-1').class == AmazonS3EncryptionClient
        assert amazonWebService.getS3Encryption('eu-west-1').endpoint.toString() == 'https://s3-eu-west-1.amazonaws.com'
    }

    void testS3EncryptionClientWithoutCredentials() {
        def amazonWebService = getServiceWithoutCredentials()

        shouldFail(MissingMethodException) {
            amazonWebService.getS3EncryptionAsync()
        }
        shouldFail(MissingMethodException) {
            amazonWebService.getS3EncryptionAsync('eu-west-1')
        }
        assert amazonWebService.getS3Encryption().class == AmazonS3EncryptionClient
        assert amazonWebService.getS3Encryption().endpoint.toString() == 'https://s3.amazonaws.com'
        assert amazonWebService.getS3Encryption('eu-west-1').class == AmazonS3EncryptionClient
        assert amazonWebService.getS3Encryption('eu-west-1').endpoint.toString() == 'https://s3-eu-west-1.amazonaws.com'
    }

    /**
     * Sdb
     */
    void testSdbClientWithCredentials() {
        def amazonWebService = getServiceWithCredentials()

        assert amazonWebService.getSdbAsync().class == AmazonSimpleDBAsyncClient
        assert amazonWebService.getSdbAsync('eu-west-1').class == AmazonSimpleDBAsyncClient
        assert amazonWebService.getSdb().class == AmazonSimpleDBClient
        assert amazonWebService.getSdb('eu-west-1').class == AmazonSimpleDBClient
        assert amazonWebService.getSdb('eu-west-1').endpoint.toString() == 'https://sdb.eu-west-1.amazonaws.com'
    }

    void testSdbClientWithoutCredentials() {
        def amazonWebService = getServiceWithoutCredentials()

        assert amazonWebService.getSdbAsync().class == AmazonSimpleDBAsyncClient
        assert amazonWebService.getSdbAsync('eu-west-1').class == AmazonSimpleDBAsyncClient
        assert amazonWebService.getSdb().class == AmazonSimpleDBClient
        assert amazonWebService.getSdb('eu-west-1').class == AmazonSimpleDBClient
        assert amazonWebService.getSdb('eu-west-1').endpoint.toString() == 'https://sdb.eu-west-1.amazonaws.com'
    }

    /**
     * Ses
     */
    void testSesClientWithCredentials() {
        def amazonWebService = getServiceWithCredentials()

        assert amazonWebService.getSesAsync().class == AmazonSimpleEmailServiceAsyncClient
        assert amazonWebService.getSesAsync('eu-west-1').class == AmazonSimpleEmailServiceAsyncClient
        assert amazonWebService.getSes().class == AmazonSimpleEmailServiceClient
        assert amazonWebService.getSes('eu-west-1').class == AmazonSimpleEmailServiceClient
        assert amazonWebService.getSes('eu-west-1').endpoint.toString() == 'https://email.eu-west-1.amazonaws.com'
    }

    void testSesClientWithoutCredentials() {
        def amazonWebService = getServiceWithoutCredentials()

        assert amazonWebService.getSesAsync().class == AmazonSimpleEmailServiceAsyncClient
        assert amazonWebService.getSesAsync('eu-west-1').class == AmazonSimpleEmailServiceAsyncClient
        assert amazonWebService.getSes().class == AmazonSimpleEmailServiceClient
        assert amazonWebService.getSes('eu-west-1').class == AmazonSimpleEmailServiceClient
        assert amazonWebService.getSes('eu-west-1').endpoint.toString() == 'https://email.eu-west-1.amazonaws.com'
    }

    /**
     * Sns
     */
    void testSnsClientWithCredentials() {
        def amazonWebService = getServiceWithCredentials()

        assert amazonWebService.getSnsAsync().class == AmazonSNSAsyncClient
        assert amazonWebService.getSnsAsync('eu-west-1').class == AmazonSNSAsyncClient
        assert amazonWebService.getSns().class == AmazonSNSClient
        assert amazonWebService.getSns('eu-west-1').class == AmazonSNSClient
        assert amazonWebService.getSns('eu-west-1').endpoint.toString() == 'https://sns.eu-west-1.amazonaws.com'
    }

    void testSnsClientWithoutCredentials() {
        def amazonWebService = getServiceWithoutCredentials()

        assert amazonWebService.getSnsAsync().class == AmazonSNSAsyncClient
        assert amazonWebService.getSnsAsync('eu-west-1').class == AmazonSNSAsyncClient
        assert amazonWebService.getSns().class == AmazonSNSClient
        assert amazonWebService.getSns('eu-west-1').class == AmazonSNSClient
        assert amazonWebService.getSns('eu-west-1').endpoint.toString() == 'https://sns.eu-west-1.amazonaws.com'
    }

    /**
     * Sqs
     */
    void testSqsClientWithCredentials() {
        def amazonWebService = getServiceWithCredentials()

        assert amazonWebService.getSqsAsync().class == AmazonSQSAsyncClient
        assert amazonWebService.getSqsAsync('eu-west-1').class == AmazonSQSAsyncClient
        assert amazonWebService.getSqs().class == AmazonSQSClient
        assert amazonWebService.getSqs('eu-west-1').class == AmazonSQSClient
        assert amazonWebService.getSqs('eu-west-1').endpoint.toString() == 'https://sqs.eu-west-1.amazonaws.com'
    }

    void testSqsClientWithoutCredentials() {
        def amazonWebService = getServiceWithoutCredentials()

        assert amazonWebService.getSqsAsync().class == AmazonSQSAsyncClient
        assert amazonWebService.getSqsAsync('eu-west-1').class == AmazonSQSAsyncClient
        assert amazonWebService.getSqs().class == AmazonSQSClient
        assert amazonWebService.getSqs('eu-west-1').class == AmazonSQSClient
        assert amazonWebService.getSqs('eu-west-1').endpoint.toString() == 'https://sqs.eu-west-1.amazonaws.com'
    }

    /**
     * SqsBuffered
     */
    void testSqsBufferedClientWithCredentials() {
        def amazonWebService = getServiceWithCredentials()

        assert amazonWebService.getSqsBufferedAsync().class == AmazonSQSBufferedAsyncClient
        assert amazonWebService.getSqsBufferedAsync('eu-west-1').class == AmazonSQSBufferedAsyncClient
    }

    void testSqsBufferedClientWithoutCredentials() {
        def amazonWebService = getServiceWithoutCredentials()

        assert amazonWebService.getSqsBufferedAsync().class == AmazonSQSBufferedAsyncClient
        assert amazonWebService.getSqsBufferedAsync('eu-west-1').class == AmazonSQSBufferedAsyncClient
    }

    /**
     * StorageGateway
     */
    void testStorageGatewayClientWithCredentials() {
        def amazonWebService = getServiceWithCredentials()

        assert amazonWebService.getStorageGatewayAsync().class == AWSStorageGatewayAsyncClient
        assert amazonWebService.getStorageGatewayAsync('eu-west-1').class == AWSStorageGatewayAsyncClient
        assert amazonWebService.getStorageGateway().class == AWSStorageGatewayClient
        assert amazonWebService.getStorageGateway('eu-west-1').class == AWSStorageGatewayClient
        assert amazonWebService.getStorageGateway('eu-west-1').endpoint.toString() == 'https://storagegateway.eu-west-1.amazonaws.com'
    }

    void testStorageGatewayClientWithoutCredentials() {
        def amazonWebService = getServiceWithoutCredentials()

        assert amazonWebService.getStorageGatewayAsync().class == AWSStorageGatewayAsyncClient
        assert amazonWebService.getStorageGatewayAsync('eu-west-1').class == AWSStorageGatewayAsyncClient
        assert amazonWebService.getStorageGateway().class == AWSStorageGatewayClient
        assert amazonWebService.getStorageGateway('eu-west-1').class == AWSStorageGatewayClient
        assert amazonWebService.getStorageGateway('eu-west-1').endpoint.toString() == 'https://storagegateway.eu-west-1.amazonaws.com'
    }

    /**
     * Sts
     */
    void testStsClientWithCredentials() {
        def amazonWebService = getServiceWithCredentials()

        assert amazonWebService.getStsAsync().class == AWSSecurityTokenServiceAsyncClient
        assert amazonWebService.getStsAsync('eu-west-1').class == AWSSecurityTokenServiceAsyncClient
        assert amazonWebService.getSts().class == AWSSecurityTokenServiceClient
        assert amazonWebService.getSts('eu-west-1').class == AWSSecurityTokenServiceClient
        assert amazonWebService.getSts('eu-west-1').endpoint.toString() == 'https://sts.amazonaws.com'
    }

    void testStsClientWithoutCredentials() {
        def amazonWebService = getServiceWithoutCredentials()

        assert amazonWebService.getStsAsync().class == AWSSecurityTokenServiceAsyncClient
        assert amazonWebService.getStsAsync('eu-west-1').class == AWSSecurityTokenServiceAsyncClient
        assert amazonWebService.getSts().class == AWSSecurityTokenServiceClient
        assert amazonWebService.getSts('eu-west-1').class == AWSSecurityTokenServiceClient
        assert amazonWebService.getSts('eu-west-1').endpoint.toString() == 'https://sts.amazonaws.com'
    }

    /**
     * Swf
     */
    void testSwfClientWithCredentials() {
        def amazonWebService = getServiceWithCredentials()

        assert amazonWebService.getSwfAsync().class == AmazonSimpleWorkflowAsyncClient
        assert amazonWebService.getSwfAsync('eu-west-1').class == AmazonSimpleWorkflowAsyncClient
        assert amazonWebService.getSwf().class == AmazonSimpleWorkflowClient
        assert amazonWebService.getSwf('eu-west-1').class == AmazonSimpleWorkflowClient
        assert amazonWebService.getSwf('eu-west-1').endpoint.toString() == 'https://swf.eu-west-1.amazonaws.com'
    }

    void testSwfClientWithoutCredentials() {
        def amazonWebService = getServiceWithoutCredentials()

        assert amazonWebService.getSwfAsync().class == AmazonSimpleWorkflowAsyncClient
        assert amazonWebService.getSwfAsync('eu-west-1').class == AmazonSimpleWorkflowAsyncClient
        assert amazonWebService.getSwf().class == AmazonSimpleWorkflowClient
        assert amazonWebService.getSwf('eu-west-1').class == AmazonSimpleWorkflowClient
        assert amazonWebService.getSwf('eu-west-1').endpoint.toString() == 'https://swf.eu-west-1.amazonaws.com'
    }

    /**
     * DynamoDBMapper
     */
    void testDynamoDBMapper() {
        def amazonWebService = getServiceWithCredentials()

        assert amazonWebService.getDynamoDBMapper().class == DynamoDBMapper
        assert amazonWebService.getDynamoDBMapper('eu-west-1').class == DynamoDBMapper
    }

    /**
     * TransferManager
     */
    void testTransferManager() {
        def amazonWebService = getServiceWithCredentials()

        assert amazonWebService.getTransferManager().class == TransferManager
        assert amazonWebService.getTransferManager('eu-west-1').class == TransferManager
    }

    /**
     * Cache
     */
    void testAsyncClientCache() {
        def amazonWebService = getServiceWithCredentials()

        def service1
        def service2
        def otherService
        def otherServiceType
        def otherRegion1
        def otherRegion2

        // check if the cache returns the same object if requested twice,
        // also make sure other calls and regions don't corrupt the cache
        service1 = amazonWebService.getEc2Async()
        otherRegion1 = amazonWebService.getEc2Async('eu-west-1')
        otherService = amazonWebService.getSwfAsync()
        otherServiceType = amazonWebService.getSes()
        service2 = amazonWebService.getEc2Async()
        otherRegion2 = amazonWebService.getEc2Async('eu-west-1')
        assert service1 == service2
        assert otherRegion1 == otherRegion2
        assert service1 != otherService
        assert service1 != otherServiceType
        assert service1 != otherRegion1
    }

    void testSyncClientCache() {
        def amazonWebService = getServiceWithCredentials()

        def service1
        def service2
        def otherService
        def otherServiceType
        def otherRegion1
        def otherRegion2

        service1 = amazonWebService.getEc2()
        otherRegion1 = amazonWebService.getEc2('eu-west-1')
        otherService = amazonWebService.getSwf()
        otherServiceType = amazonWebService.getSesAsync()
        service2 = amazonWebService.getEc2()
        otherRegion2 = amazonWebService.getEc2('eu-west-1')
        assert service1 == service2
        assert otherRegion1 == otherRegion2
        assert service1 != otherService
        assert service1 != otherServiceType
        assert service1 != otherRegion1
    }
}
