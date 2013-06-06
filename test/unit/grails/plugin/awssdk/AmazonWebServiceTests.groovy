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
import com.amazonaws.services.securitytoken.AWSSecurityTokenServiceAsyncClient
import com.amazonaws.services.securitytoken.AWSSecurityTokenServiceClient

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

        amazonWebService.grailsApplication = grailsApplication

        amazonWebService
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

    void testSesClientWithCredentials() {
        def amazonWebService = getServiceWithCredentials()

        assert amazonWebService.getSesAsync().class == AmazonSimpleEmailServiceAsyncClient
        assert amazonWebService.getSesAsync('eu-west-1').class == AmazonSimpleEmailServiceAsyncClient
        assert amazonWebService.getSes().class == AmazonSimpleEmailServiceClient
        assert amazonWebService.getSes('eu-west-1').class == AmazonSimpleEmailServiceClient
        assert amazonWebService.getSes('eu-west-1').endpoint.toString() == 'https://email.us-east-1.amazonaws.com'
    }

    void testSesClientWithoutCredentials() {
        def amazonWebService = getServiceWithoutCredentials()

        assert amazonWebService.getSesAsync().class == AmazonSimpleEmailServiceAsyncClient
        assert amazonWebService.getSesAsync('eu-west-1').class == AmazonSimpleEmailServiceAsyncClient
        assert amazonWebService.getSes().class == AmazonSimpleEmailServiceClient
        assert amazonWebService.getSes('eu-west-1').class == AmazonSimpleEmailServiceClient
        assert amazonWebService.getSes('eu-west-1').endpoint.toString() == 'https://email.us-east-1.amazonaws.com'
    }

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

    void testTransferManager() {
        def amazonWebService = getServiceWithCredentials()

        assert amazonWebService.getTransferManager().class == TransferManager
        assert amazonWebService.getTransferManager('eu-west-1').class == TransferManager
    }

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
