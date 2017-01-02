package grails.plugin.awssdk.test

import grails.plugin.awssdk.dynamodb.AbstractDBService

class FooItemDBService extends AbstractDBService<FooItem> {

    FooItemDBService() {
        super(FooItem)
    }

}
