package grails.plugin.awssdk

import grails.plugin.awssdk.dynamodb.AbstractDBService
import grails.plugin.awssdk.test.FooItem
import grails.plugin.awssdk.test.FooItemDBService
import grails.test.mixin.TestFor
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.services.ServiceUnitTestMixin} for usage instructions
 */
@TestFor(FooItemDBService)
class FooItemDBServiceSpec extends Specification {

    def setup() {
    }

    def cleanup() {
    }

    void "test nullify empty hashset"() {
        given:
        def item = new FooItem();

        when:
        AbstractDBService.nullifyHashSets(item);

        then:
        item.getTags() == null
    }

    void "test should not nullify not empty hashset"() {
        given:
        def item = new FooItem();
        item.tags.add("tag")

        when:
        AbstractDBService.nullifyHashSets(item);

        then:
        item.getTags() != null
        item.getTags().contains("tag")
    }
}
