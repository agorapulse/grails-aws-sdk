package grails.plugin.awssdk

import static org.junit.Assert.*

import grails.test.mixin.*
import grails.test.mixin.support.*
import org.junit.*

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
    
    AmazonWebService getServiceWithoutCredentials() {
        def amazonWebService = new AmazonWebService()
        
        grailsApplication.config.grails.plugin.awssdk.accessKey = "abcdefghi"
        grailsApplication.config.grails.plugin.awssdk.secretKey = "123456789"
        
        amazonWebService.grailsApplication = grailsApplication
        
        amazonWebService
    }

    void testAutoScalingClient() {
        def amazonWebService = getServiceWithCredentials()
        
        assert amazonWebService.getAutoScalingAsync().class == AmazonAutoScalingAsyncClient
        assert amazonWebService.getAutoScaling().class == AmazonAutoScalingClient
        
        amazonWebService = getServiceWithoutCredentials()
        
        assert amazonWebService.getAutoScalingAsync().class == AmazonAutoScalingAsyncClient
        assert amazonWebService.getAutoScaling().class == AmazonAutoScalingClient
    }
    
    void testCloudFormationClient() {
        def amazonWebService = getServiceWithCredentials()
        
        assert amazonWebService.getCloudFormationAsync().class == AmazonCloudFormationAsyncClient
        assert amazonWebService.getCloudFormation().class == AmazonCloudFormationClient
        
        amazonWebService = getServiceWithoutCredentials()
        
        assert amazonWebService.getCloudFormationAsync().class == AmazonCloudFormationAsyncClient
        assert amazonWebService.getCloudFormation().class == AmazonCloudFormationClient
    }
    
    void testCloudFrontClient() {
        def amazonWebService = getServiceWithCredentials()
        
        assert amazonWebService.getCloudFrontAsync().class == AmazonCloudFrontAsyncClient
        assert amazonWebService.getCloudFront().class == AmazonCloudFrontClient
        
        amazonWebService = getServiceWithoutCredentials()
        
        assert amazonWebService.getCloudFrontAsync().class == AmazonCloudFrontAsyncClient
        assert amazonWebService.getCloudFront().class == AmazonCloudFrontClient
    }
    
    void testCloudSearchClient() {
        def amazonWebService = getServiceWithCredentials()
        
        assert amazonWebService.getCloudSearchAsync().class == AmazonCloudSearchAsyncClient
        assert amazonWebService.getCloudSearch().class == AmazonCloudSearchClient
        
        amazonWebService = getServiceWithoutCredentials()
        
        assert amazonWebService.getCloudSearchAsync().class == AmazonCloudSearchAsyncClient
        assert amazonWebService.getCloudSearch().class == AmazonCloudSearchClient
    }
    
    void testCloudWatchClient() {
        def amazonWebService = getServiceWithCredentials()
        
        assert amazonWebService.getCloudWatchAsync().class == AmazonCloudWatchAsyncClient
        assert amazonWebService.getCloudWatch().class == AmazonCloudWatchClient
        
        amazonWebService = getServiceWithoutCredentials()
        
        assert amazonWebService.getCloudWatchAsync().class == AmazonCloudWatchAsyncClient
        assert amazonWebService.getCloudWatch().class == AmazonCloudWatchClient
    }
    
    void testDynamoDBClient() {
        def amazonWebService = getServiceWithCredentials()
        
        assert amazonWebService.getDynamoDBAsync().class == AmazonDynamoDBAsyncClient
        assert amazonWebService.getDynamoDB().class == AmazonDynamoDBClient
        
        amazonWebService = getServiceWithoutCredentials()
        
        assert amazonWebService.getDynamoDBAsync().class == AmazonDynamoDBAsyncClient
        assert amazonWebService.getDynamoDB().class == AmazonDynamoDBClient
    }
    
    void testEc2Client() {
        def amazonWebService = getServiceWithCredentials()
        
        assert amazonWebService.getEc2Async().class == AmazonEC2AsyncClient
        assert amazonWebService.getEc2().class == AmazonEC2Client
        
        amazonWebService = getServiceWithoutCredentials()
        
        assert amazonWebService.getEc2Async().class == AmazonEC2AsyncClient
        assert amazonWebService.getEc2().class == AmazonEC2Client
    }
    
    void testElasticBeanstalkClient() {
        def amazonWebService = getServiceWithCredentials()

        assert amazonWebService.getElasticBeanstalkAsync().class == AWSElasticBeanstalkAsyncClient
        assert amazonWebService.getElasticBeanstalk().class == AWSElasticBeanstalkClient
        
        amazonWebService = getServiceWithoutCredentials()
        
        assert amazonWebService.getElasticBeanstalkAsync().class == AWSElasticBeanstalkAsyncClient
        assert amazonWebService.getElasticBeanstalk().class == AWSElasticBeanstalkClient
    }
    
    void testElastiCacheClient() {
        def amazonWebService = getServiceWithCredentials()

        assert amazonWebService.getElastiCacheAsync().class == AmazonElastiCacheAsyncClient
        assert amazonWebService.getElastiCache().class == AmazonElastiCacheClient
        
        amazonWebService = getServiceWithoutCredentials()
        
        assert amazonWebService.getElastiCacheAsync().class == AmazonElastiCacheAsyncClient
        assert amazonWebService.getElastiCache().class == AmazonElastiCacheClient
    }
    
    void testElbClient() {
        def amazonWebService = getServiceWithCredentials()

        assert amazonWebService.getElbAsync().class == AmazonElasticLoadBalancingAsyncClient
        assert amazonWebService.getElb().class == AmazonElasticLoadBalancingClient
        
        amazonWebService = getServiceWithoutCredentials()
        
        assert amazonWebService.getElbAsync().class == AmazonElasticLoadBalancingAsyncClient
        assert amazonWebService.getElb().class == AmazonElasticLoadBalancingClient
    }
    
    void testElasticMapReduceClient() {
        def amazonWebService = getServiceWithCredentials()

        assert amazonWebService.getElasticMapReduceAsync().class == AmazonElasticMapReduceAsyncClient
        assert amazonWebService.getElasticMapReduce().class == AmazonElasticMapReduceClient
        
        amazonWebService = getServiceWithoutCredentials()
        
        assert amazonWebService.getElasticMapReduceAsync().class == AmazonElasticMapReduceAsyncClient
        assert amazonWebService.getElasticMapReduce().class == AmazonElasticMapReduceClient
    }
    
    void testElasticTranscoderClient() {
        def amazonWebService = getServiceWithCredentials()

        assert amazonWebService.getElasticTranscoderAsync().class == AmazonElasticTranscoderAsyncClient
        assert amazonWebService.getElasticTranscoder().class == AmazonElasticTranscoderClient
        
        amazonWebService = getServiceWithoutCredentials()
        
        assert amazonWebService.getElasticTranscoderAsync().class == AmazonElasticTranscoderAsyncClient
        assert amazonWebService.getElasticTranscoder().class == AmazonElasticTranscoderClient
    }
    
    void testGlacierClient() {
        def amazonWebService = getServiceWithCredentials()

        assert amazonWebService.getGlacierAsync().class == AmazonGlacierAsyncClient
        assert amazonWebService.getGlacier().class == AmazonGlacierClient
        
        amazonWebService = getServiceWithoutCredentials()
        
        assert amazonWebService.getGlacierAsync().class == AmazonGlacierAsyncClient
        assert amazonWebService.getGlacier().class == AmazonGlacierClient
    }
    
    void testIamClient() {
        def amazonWebService = getServiceWithCredentials()

        assert amazonWebService.getIamAsync().class == AmazonIdentityManagementAsyncClient
        assert amazonWebService.getIam().class == AmazonIdentityManagementClient
        
        amazonWebService = getServiceWithoutCredentials()
        
        assert amazonWebService.getIamAsync().class == AmazonIdentityManagementAsyncClient
        assert amazonWebService.getIam().class == AmazonIdentityManagementClient
    }
    
    void testImportExportClient() {
        def amazonWebService = getServiceWithCredentials()

        assert amazonWebService.getImportExportAsync().class == AmazonImportExportAsyncClient
        assert amazonWebService.getImportExport().class == AmazonImportExportClient
        
        amazonWebService = getServiceWithoutCredentials()
        
        assert amazonWebService.getImportExportAsync().class == AmazonImportExportAsyncClient
        assert amazonWebService.getImportExport().class == AmazonImportExportClient
    }
    
    void testOpsWorksAsync() {
        def amazonWebService = getServiceWithCredentials()
        
        assert amazonWebService.getOpsWorksAsync().class == AWSOpsWorksAsyncClient
        assert amazonWebService.getOpsWorks().class == AWSOpsWorksClient
        
        amazonWebService = getServiceWithoutCredentials()
        
        assert amazonWebService.getOpsWorksAsync().class == AWSOpsWorksAsyncClient
        assert amazonWebService.getOpsWorks().class == AWSOpsWorksClient
    }
    
    void testRdsClient() {
        def amazonWebService = getServiceWithCredentials()

        assert amazonWebService.getRdsAsync().class == AmazonRDSAsyncClient
        assert amazonWebService.getRds().class == AmazonRDSClient
        
        amazonWebService = getServiceWithoutCredentials()
        
        assert amazonWebService.getRdsAsync().class == AmazonRDSAsyncClient
        assert amazonWebService.getRds().class == AmazonRDSClient
    }
    
    void testRedshiftClient() {
        def amazonWebService = getServiceWithCredentials()
        
        assert amazonWebService.getRedshiftAsync().class == AmazonRedshiftAsyncClient
        assert amazonWebService.getRedshift().class == AmazonRedshiftClient
        
        amazonWebService = getServiceWithoutCredentials()
        
        assert amazonWebService.getRedshiftAsync().class == AmazonRedshiftAsyncClient
        assert amazonWebService.getRedshift().class == AmazonRedshiftClient
    }
    
    void testRoute53Client() {
        def amazonWebService = getServiceWithCredentials()

        assert amazonWebService.getRoute53Async().class == AmazonRoute53AsyncClient
        assert amazonWebService.getRoute53().class == AmazonRoute53Client
        
        amazonWebService = getServiceWithoutCredentials()
        
        assert amazonWebService.getRoute53Async().class == AmazonRoute53AsyncClient
        assert amazonWebService.getRoute53().class == AmazonRoute53Client
    }
    
    void testS3Client() {
        def amazonWebService = getServiceWithCredentials()

        shouldFail(Exception) {
            amazonWebService.getS3Async()
        }
        assert amazonWebService.getS3().class == AmazonS3Client
        
        amazonWebService = getServiceWithoutCredentials()
        
        shouldFail(Exception) {
            amazonWebService.getS3Async()
        }
        assert amazonWebService.getS3().class == AmazonS3Client
    }
    
    void testSdbClient() {
        def amazonWebService = getServiceWithCredentials()

        assert amazonWebService.getSdbAsync().class == AmazonSimpleDBAsyncClient
        assert amazonWebService.getSdb().class == AmazonSimpleDBClient
        
        amazonWebService = getServiceWithoutCredentials()
        
        assert amazonWebService.getSdbAsync().class == AmazonSimpleDBAsyncClient
        assert amazonWebService.getSdb().class == AmazonSimpleDBClient
    }
    
    void testSesClient() {
        def amazonWebService = getServiceWithCredentials()

        assert amazonWebService.getSesAsync().class == AmazonSimpleEmailServiceAsyncClient
        assert amazonWebService.getSes().class == AmazonSimpleEmailServiceClient
        
        amazonWebService = getServiceWithoutCredentials()
        
        assert amazonWebService.getSesAsync().class == AmazonSimpleEmailServiceAsyncClient
        assert amazonWebService.getSes().class == AmazonSimpleEmailServiceClient
    }
    
    void testSnsClient() {
        def amazonWebService = getServiceWithCredentials()

        assert amazonWebService.getSnsAsync().class == AmazonSNSAsyncClient
        assert amazonWebService.getSns().class == AmazonSNSClient
        
        amazonWebService = getServiceWithoutCredentials()
        
        assert amazonWebService.getSnsAsync().class == AmazonSNSAsyncClient
        assert amazonWebService.getSns().class == AmazonSNSClient
    }
    
    void testSqsClient() {
        def amazonWebService = getServiceWithCredentials()

        assert amazonWebService.getSqsAsync().class == AmazonSQSAsyncClient
        assert amazonWebService.getSqs().class == AmazonSQSClient
        
        amazonWebService = getServiceWithoutCredentials()
        
        assert amazonWebService.getSqsAsync().class == AmazonSQSAsyncClient
        assert amazonWebService.getSqs().class == AmazonSQSClient
    }
    
    void testStorageGatewayClient() {
        def amazonWebService = getServiceWithCredentials()

        assert amazonWebService.getStorageGatewayAsync().class == AWSStorageGatewayAsyncClient
        assert amazonWebService.getStorageGateway().class == AWSStorageGatewayClient
        
        amazonWebService = getServiceWithoutCredentials()
        
        assert amazonWebService.getStorageGatewayAsync().class == AWSStorageGatewayAsyncClient
        assert amazonWebService.getStorageGateway().class == AWSStorageGatewayClient
    }
    
    void testSwfClient() {
        def amazonWebService = getServiceWithCredentials()

        assert amazonWebService.getSwfAsync().class == AmazonSimpleWorkflowAsyncClient
        assert amazonWebService.getSwf().class == AmazonSimpleWorkflowClient
        
        amazonWebService = getServiceWithoutCredentials()
        
        assert amazonWebService.getSwfAsync().class == AmazonSimpleWorkflowAsyncClient
        assert amazonWebService.getSwf().class == AmazonSimpleWorkflowClient
    }
    
    void testTransferManager() {
        def amazonWebService = getServiceWithCredentials()

        assert amazonWebService.getTransferManager().class == TransferManager
    }
}
