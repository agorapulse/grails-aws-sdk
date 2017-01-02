package grails.plugin.awssdk

import com.amazonaws.services.kinesis.AmazonKinesisClient
import grails.plugin.awssdk.kinesis.AmazonKinesisService
import grails.test.mixin.TestFor
import spock.lang.Specification

@TestFor(AmazonKinesisService)
class AmazonKinesisServiceSpec extends Specification {

    static String STREAM_NAME = 'streamName'
    static String SHARD_ID = '000001'

    static doWithConfig(conf) {
        conf.grails.plugin.awssdk.kinesis.stream = STREAM_NAME
        conf.grails.plugin.awssdk.kinesis.region = 'eu-west-1'
    }

    def setup() {
        // Mock collaborators
        service.client = Mock(AmazonKinesisClient)
        // Initialize
        service.init(STREAM_NAME)

    }

    def "Listing shards"() {
        when:
        List shards = service.listShards(STREAM_NAME)

        then:
        1 * service.client.describeStream(_) >> [
                streamDescription: [
                        shards: [[shardId: SHARD_ID]],
                        hasMoreShards: false
                ]
        ]
        shards
        shards.size() == 1
        shards.first().shardId == SHARD_ID
    }

    def "Listing stream names"() {
        when:
        List streamNames = service.listStreamNames()

        then:
        1 * service.client.listStreams() >> [streamNames: [STREAM_NAME]]
        streamNames
        streamNames.size() == 1
        streamNames.first() == STREAM_NAME
    }

    def "Putting record"() {
        when:
        String sequenceNumber = service.putRecord('partition-key', 'some-data', '')

        then:
        1 * service.client.putRecord(_) >> [sequenceNumber: 'sequence-number']
        sequenceNumber
        sequenceNumber == 'sequence-number'
    }

}
