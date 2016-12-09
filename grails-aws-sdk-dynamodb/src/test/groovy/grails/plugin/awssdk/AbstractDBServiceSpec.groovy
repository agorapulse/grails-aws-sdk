package grails.plugin.awssdk

import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper
import spock.lang.Specification

abstract class AbstractDBServiceSpec extends Specification {

    def mockCollaborators() {
        // Mock collaborators
        service.client >> Mock(AmazonDynamoDBClient)
        service.mapper >> Mock(DynamoDBMapper)
        service.init()
    }

    void "Getting item"() {
        when:
        def item = service.get('hash-key', 'range-key')

        then:
        1 * service.mapper.load(_, 'hash-key', 'range-key')
    }

}
