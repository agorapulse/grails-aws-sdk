package grails.plugin.awssdk.kinesis

abstract class AbstractEvent {

    String consumerFilterKey = '' // Ex.: 'ben', 'flo' (to share Streams between different environment, for example devs)
    String getPartitionKey() {
        // To be overridden
    }
    Date timestamp = new Date()

    void setPartitionKey(partitionKey) {
        // Do nothing (method required for JSON marshaller)
    }

}
