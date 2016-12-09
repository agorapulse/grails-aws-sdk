Grails AWS SDK Kinesis Plugin
=============================

[![Build Status](https://travis-ci.org/agorapulse/grails-aws-sdk-kinesis.svg?branch=master)](https://travis-ci.org/agorapulse/grails-aws-sdk-kinesis)
[![Download](https://api.bintray.com/packages/agorapulse/plugins/aws-sdk-kinesis/images/download.svg)](https://bintray.com/agorapulse/plugins/aws-sdk-kinesis/_latestVersion)

The [AWS SDK Plugins for Grails3](https://medium.com/@benorama/aws-sdk-plugins-for-grails-3-cc7f910fdc0d#.5gdwdxei3) are a suite of plugins that adds support for the [Amazon Web Services](http://aws.amazon.com/) infrastructure services.

The aim is to to get you started quickly by providing friendly lightweight utility [Grails](http://grails.org) service wrappers, around the official [AWS SDK for Java](http://aws.amazon.com/sdkforjava/) (which is great but very “java-esque”).
See [this article](https://medium.com/@benorama/aws-sdk-plugins-for-grails-3-cc7f910fdc0d#.5gdwdxei3) for more info.

The following services are currently supported:

* [AWS SDK DynamoDB Grails Plugin](http://github.com/agorapulse/grails-aws-sdk-dynamodb)
* [AWS SDK Kinesis Grails Plugin](http://github.com/agorapulse/grails-aws-sdk-kinesis)
* [AWS SDK S3 Grails Plugin](http://github.com/agorapulse/grails-aws-sdk-s3)
* [AWS SDK SES Grails Plugin](http://github.com/agorapulse/grails-aws-sdk-ses)
* [AWS SDK SNS Grails Plugin](http://github.com/agorapulse/grails-aws-sdk-sns)
* [AWS SDK SQS Grails Plugin](http://github.com/agorapulse/grails-aws-sdk-sqs)

# Introduction

This plugin adds support for [Amazon Kinesis](https://aws.amazon.com/kinesis/), a platform for streaming data on AWS, offering powerful services to make it easy to load and analyze streaming data, and also providing the ability for you to build custom streaming data applications for specialized needs.

It's a Grails wrapper around the officieal [Amazon Kinesis Client](https://github.com/awslabs/amazon-kinesis-client).


# Installation

Add plugin dependency to your `build.gradle`:

```groovy
dependencies {
  ...
  compile 'org.grails.plugins:aws-sdk-kinesis:2.0.5'
  ...
```

# Config

Create an AWS account [Amazon Web Services](http://aws.amazon.com/), in order to get your own credentials accessKey and secretKey.


## AWS SDK for Java version

You can override the default AWS SDK for Java and Kinesis Client version by setting it in your _gradle.properties_:

```
awsJavaSdkVersion=1.10.66
awsKinesisClientVersion=1.6.2
```

## Credentials

Add your AWS credentials parameters to your _grails-app/conf/application.yml_:

```yml
grails:
    plugin:
        awssdk:
            accessKey: {ACCESS_KEY}
            secretKey: {SECRET_KEY}
```

If you do not provide credentials, a credentials provider chain will be used that searches for credentials in this order:

* Environment Variables - `AWS_ACCESS_KEY_ID` and `AWS_SECRET_KEY`
* Java System Properties - `aws.accessKeyId and `aws.secretKey`
* Instance profile credentials delivered through the Amazon EC2 metadata service (IAM role)

## Region

The default region used is **us-east-1**. You might override it in your config:

```yml
grails:
    plugin:
        awssdk:
            region: eu-west-1
```

If you're using multiple AWS SDK Grails plugins, you can define specific settings for each services.

```yml
grails:
    plugin:
        awssdk:
            accessKey: {ACCESS_KEY} # Global default setting
            secretKey: {SECRET_KEY} # Global default setting
            region: us-east-1       # Global default setting
            kinesis:
                accessKey: {ACCESS_KEY} # (optional)
                secretKey: {SECRET_KEY} # (optional)
                region: eu-west-1       # (optional)
                stream: MyStream        # (optional)
                consumerFilterKey: ben  # (optional)
                enabled: false          # (optional, default to tru)
            
```

**stream**: default stream to use when calling methods without `streamName`.

**consumerFilterKey**: to filter record processing by environment and share a stream between different apps/teams (ex.: devs).

TIP: if you use multiple streams, you can create a new service for each stream that inherits from **AmazonKinesisService**.

```groovy
class MyStreamService extends AmazonKinesisService {

    static final STREAM_NAME = 'MyStream'

    @PostConstruct
    def init() {
        init(STREAM_NAME)
    }

}
```


# Usage

The plugin provides the following Grails artefacts:

* **AmazonKinesisService**

And some utility abstract classes to create a stream consumer based on [Kinesis Client Library](http://docs.aws.amazon.com/kinesis/latest/dev/developing-consumers-with-kcl.html):

* **AbstractClientService**
* **AbstractEvent**
* **AbstractEventService**
* **AbstractRecordProcessor**

## Stream management

```groovy
// List streams
amazonKinesisService.listStreamNames().each {
    println it
}

// Describe stream
stream = amazonKinesisService.describeStream('MyStream')
// Or if you have define default stream
stream = amazonKinesisService.describeStream()

// List shards
shards = amazonKinesisService.listShards('MyStream')
// Or if you have define default stream
shards = amazonKinesisService.listShards()
shards.each {
    println it
}

// Get a shard
shard = amazonKinesisService.getShard('MyStream', 'shardId-000000000002')
// Or if you have define default stream
shard = amazonKinesisService.getShard('shardId-000000000002')
```

## Putting records

```groovy
// Put a single record
amazonKinesisService.putRecord('MyStream', 'some-partition-key', data, '')
// Or if you have define default stream
amazonKinesisService.putRecord('some-partition-key', data, '')

// Put multiple records
amazonKinesisService.putRecords('MyStream', 'some-partition-key', recordEntries, '')
// Or if you have define default stream
amazonKinesisService.putRecords('some-partition-key', recordEntries, '')
```

## Listing records

Here are some utility methods to debug streams, but you will probably use a consumer app based on [Kinesis Client Library](http://docs.aws.amazon.com/kinesis/latest/dev/developing-consumers-with-kcl.html).

```groovy
// Get oldest record
record = amazonKinesisService.getShardOldestRecord('MyStream', shard)
// Or if you have define default stream
record = amazonKinesisService.getShardOldestRecord(shard)
println record
println amazonKinesisService.decodeRecordData(record)

// Get record at sequence number
record = amazonKinesisService.getShardRecordAtSequenceNumber('MyStream' ,shard, '49547417246700595154645683227660734817370104972359761954')
// Or if you have define default stream
record = amazonKinesisService.getShardRecordAtSequenceNumber(shard, '49547417246700595154645683227660734817370104972359761954')
println record
println amazonKinesisService.decodeRecordData(record)

// List oldest records
records = amazonKinesisService.listShardRecordsFromHorizon('MyStream', shard, 100)
// Or if you have define default stream
records = amazonKinesisService.listShardRecordsFromHorizon(shard, 100)
records.each { record ->
    println record
    println amazonKinesisService.decodeRecordData(record)
}

// List records after sequence number
amazonKinesisService.listShardRecordsAfterSequenceNumber('MyStream', shard, '49547417246700595154645683227660734817370104972359761954', 100)
// Or if you have define default stream
amazonKinesisService.listShardRecordsAfterSequenceNumber(shard, '49547417246700595154645683227660734817370104972359761954', 100)
records.each { record ->
    println record
    println amazonKinesisService.decodeRecordData(record)
}
```

## Defining stream events

The plugin provides some utility to handle JSON based events, on top of Kinesis records.

You must first define your event class with its custom JSON unmarshaller, if required.

```groovy
import grails.converters.JSON
import grails.plugins.awssdk.kinesis.AbstractEvent
import grails.plugins.awssdk.util.JsonDateUtils
import org.grails.web.json.JSONObject

class MyEvent extends AbstractEvent {

    long accountId
    Date someDate
    String foo
    
    // Define the partition logic for Kinesis Stream sharding
    String getPartitionKey() {
        accountId.toString() 
    }
    
    static MyEvent unmarshall(String json) {
        JSONObject jsonObject = JSON.parse(json) as JSONObject
        MyEvent event = new MyEvent(
            consumerFilterKey: jsonObject.consumerFilterKey,
            partitionKey: jsonObject.partitionKey,
            accountId: jsonObject.accountId.toLong(),
            foo: jsonObject.foo,
            someDate: JsonDateUtils.parseJsonDate(jsonObject.someDate as String)
        )
        event
    }

}
```

## Recording a stream event

```groovy
event = new MyEvent(
            accountId: 123456789,
            someDate: new Date() + 7,
            foo: 'foo'
)

// Put a single event
sequenceNumber = amazonKinesisService.putEvent('MyStream', event)
// Or if you have define default stream
sequenceNumber = amazonKinesisService.putEvent(event)

// Put multiple events
sequenceNumber = amazonKinesisService.putEvents('MyStream', events)
// Or if you have define default stream
sequenceNumber = amazonKinesisService.putEvents(events)
```

## Consuming stream events

To create a client based on [Kinesis Client Library](http://docs.aws.amazon.com/kinesis/latest/dev/developing-consumers-with-kcl.html), you must implements 3 classes by extending **AmazonKinesisEventService**, **AmazonKinesisRecordProcessor** and implementing **IRecordProcessorFactory**.

### Event consumer service

The event consumer service handles and processes each event. This is where you put all your business logic.

```groovy
import grails.plugins.awssdk.kinesis.AbstractEventService

class MyStreamEventService extends AbstractEventService {
    
    def handleEvent(MyEvent event) {
        log.debug "Handling event ${event}"
        // Put you consumer business logic here
    }
    
}
```

### Record processor

The record processor unmarshalls a single record, create the corresponding event and pass it to the event consumer service.

```groovy
import com.amazonaws.services.kinesis.model.Record
import grails.plugins.awssdk.kinesis.AbstractRecordProcessor
import org.grails.web.converters.exceptions.ConverterException

class MyStreamRecordProcessor extends AbstractRecordProcessor {

    MyStreamEventService myStreamEventService

    @Override
    void processRecordData(String data, Record record) {
        log.debug "[${shardId}] ${record.sequenceNumber}, ${record.partitionKey}, ${data}"
        MyEvent event = null
        try {
            event = MyEvent.unmarshall(data)
        } catch (ConverterException exception) {
            log.error "Ignoring event: invalid JSON"
        }
        if (event?.accountId) {
            myStreamEventService.handleEvent(event)
        } else {
            log.debug("BAD EVENT ${event}")
        }
    }

}
```

### Record processor factory

The record processor factory creates the record processor with its event consumer service.

```groovy
import com.amazonaws.services.kinesis.clientlibrary.interfaces.IRecordProcessor
import com.amazonaws.services.kinesis.clientlibrary.interfaces.IRecordProcessorFactory

class MyStreamRecordProcessorFactory implements IRecordProcessorFactory {

    MyStreamEventService myStreamEventService

    @Override
    IRecordProcessor createProcessor() {
        new MyStreamRecordProcessor(myStreamEventService: myStreamEventService)
    }

}
```

### Stream client service

Boostrap everything by creating the record processor factory and initializing the client.

If required, during bootstrap, it will automatically create the corresponding Kinesis stream and DynamoDB table for consumer checkpoints.

Once initialized, the client will check every `IDLETIME_BETWEEN_READS_MILLIS` to see if there is new records to process.

```groovy
import grails.plugins.awssdk.kinesis.AbstractClientService
import javax.annotation.PostConstruct

class MyStreamClientService extends AbstractClientService {

    static String STREAM_NAME = 'MyStream'
    static long IDLETIME_BETWEEN_READS_MILLIS = 1000L
    
    boolean lazyInit = false
    
    MyStreamEventService myStreamEventService

    @PostConstruct
    def bootstrap() {
        init(
            STREAM_NAME, 
            new MyStreamRecordProcessorFactory(myStreamEventService: myStreamEventService), 
            IDLETIME_BETWEEN_READS_MILLIS
        )
    }

}
```

## Advanced usage

If required, you can also directly use **AmazonKinesisClient** instance available at **amazonKinesisService.client**.

For more info, AWS SDK for Java documentation is located here:

* [AWS SDK for Java](http://docs.amazonwebservices.com/AWSJavaSDK/latest/javadoc/index.html)


# Bugs

To report any bug, please use the project [Issues](http://github.com/agorapulse/grails-aws-sdk-kinesis/issues) section on GitHub.

Feedback and pull requests are welcome!