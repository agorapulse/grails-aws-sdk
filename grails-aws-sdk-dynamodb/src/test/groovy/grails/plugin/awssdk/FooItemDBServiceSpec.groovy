package grails.plugin.awssdk

import grails.plugin.awssdk.dynamodb.AbstractDBService
import grails.plugin.awssdk.test.FooItem
import grails.plugin.awssdk.test.FooItemDBService
import grails.testing.services.ServiceUnitTest
import spock.lang.Specification

class FooItemDBServiceSpec extends Specification implements ServiceUnitTest<FooItemDBService>{

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
