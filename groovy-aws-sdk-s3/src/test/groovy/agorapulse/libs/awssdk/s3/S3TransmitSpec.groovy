package agorapulse.libs.awssdk.s3

import agorapulse.libs.awssdk.AwsSdkUtils
import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.regions.RegionUtils
import com.amazonaws.services.s3.AmazonS3Client
import spock.lang.IgnoreIf
import spock.lang.Specification
import spock.lang.Unroll

class S3TransmitSpec extends Specification {

    @IgnoreIf({ !System.getProperty('AWS_ACCESS_KEY_ID') || !System.getProperty('AWS_SECRET_KEY') || !System.getProperty('AWS_REGION_NAME') })
    def "test listBucketNames"() {
        when:
        String accessKey = System.getProperty('AWS_ACCESS_KEY_ID')
        String secretKey = System.getProperty('AWS_SECRET_KEY')
        String regionName = System.getProperty('AWS_REGION_NAME')

        def s3transmit = new S3Transmit()
        def credentials = new BasicAWSCredentials(accessKey, secretKey)
        def clientConfiguration = AwsSdkUtils.clientConfigurationWithMap([:])

        s3transmit.client = new AmazonS3Client(credentials, clientConfiguration).withRegion(regionName) as AmazonS3Client
        def result = s3transmit.listBucketNames()

        then:
        result
        !result.isEmpty()
    }

    @Unroll
    def "test buildAbsoluteUrl"() {
        when:
        def result = S3Transmit.buildAbsoluteUrl(regionName, bucketName, path)

        then:
        result == expected

        where:
        regionName     | bucketName     | path            || expected
        'eu-west-1'    | 'tests3groovy' | null            || 'https://s3-eu-west-1.amazonaws.com/tests3groovy/'
        'eu-west-1'    | 'tests3groovy' | 'rainbow.jpg'   || 'https://s3-eu-west-1.amazonaws.com/tests3groovy/rainbow.jpg'

    }

    @Unroll
    def "test rootAbsoluteUrl"() {
        when:
        def result = S3Transmit.rootAbsoluteUrl(regionName, bucketName)

        then:
        result == expected

        where:
        regionName     | bucketName     || expected
        'eu-west-1'    | 'tests3groovy' || 'https://s3-eu-west-1.amazonaws.com/tests3groovy'

    }
}
