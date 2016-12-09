Grails AWS SDK SQS Plugin
=========================

[![Build Status](https://travis-ci.org/agorapulse/grails-aws-sdk-sqs.svg?branch=master)](https://travis-ci.org/agorapulse/grails-aws-sdk-sqs)
[![Download](https://api.bintray.com/packages/agorapulse/plugins/aws-sdk-sqs/images/download.svg)](https://bintray.com/agorapulse/plugins/aws-sdk-sqs/_latestVersion)

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

This plugin adds support for [Amazon Simple Queue Service (SQS)](https://aws.amazon.com/sqs/), a fast, reliable, scalable, fully managed message queuing service. 
SQS makes it simple and cost-effective to decouple the components of a cloud application.


# Installation

Add plugin dependency to your `build.gradle`:

```groovy
dependencies {
  ...
  compile 'org.grails.plugins:aws-sdk-sqs:2.0.9'
  ...
```


# Config

Create an AWS account [Amazon Web Services](http://aws.amazon.com/), in order to get your own credentials accessKey and secretKey.


## AWS SDK for Java version

You can override the default AWS SDK for Java version by setting it in your _gradle.properties_:

```
awsJavaSdkVersion=1.10.66
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
            sqs:
                accessKey: {ACCESS_KEY} # (optional)
                secretKey: {SECRET_KEY} # (optional)
                region: eu-west-1       # (optional)
                queue: my-queue         # (optional)
                queueNamePrefix: ben_   # (optional)
                delaySeconds: 262144    # (optional)
                maximumMessageSize: 262144 # (optional)
                messageRetentionPeriod: 345600 # (optional), default to 4 days
                visibilityTimeout: 30   # (optional), default to 30 seconds when receiving messages
            
```

**queue**: default queue to use when calling methods without `queueName`.

**queueNamePrefix**: automatically prefix all your queue names (for example, to get different env or scopes for each developer running their app locally).

**delaySeconds**, **maximumMessageSize**, **messageRetentionPeriod** and **visibilityTimeout**: default settings used when creating a queue.

TIP: if you use multiple queues, you can create a new service for each queue that inherits from **AmazonSQSService**.

```groovy
class MyQueueService extends AmazonSQSService {

    static final QUEUE_NAME = 'my-queue'

    @PostConstruct
    def init() {
        init(QUEUE_NAME)
    }

}
```


# Usage

The plugin provides the following Grails artefact:

* **AmazonSQSService**

## Queue management

```groovy
// Create queue
amazonSQSService.createQueue(queueName)

// List queue names
amazonSQSService.listQueueNames()

// List queue URLs
amazonSQSService.listQueueUrls()

// Get queue attribute
Map attributes = amazonSQSService.getQueueAttributes(queueName)
println "ApproximateNumberOfMessages=${attributes['ApproximateNumberOfMessages']}"

// Delete queue
amazonSQSService.deleteQueue(queueName)
```

## Message management

```groovy
// Send message
amazonSQSService.sendMessage(queueName, messageBody)
// Or if you have define default queue
amazonSQSService.sendMessage(messageBody)

// Receive and delete messages
messages = amazonSQSService.receiveMessages(queueName, maxNumberOfMessages, visibilityTimeout, waitTimeSeconds)
// Or if you have define default queue
messages = amazonSQSService.receiveMessages(maxNumberOfMessages, visibilityTimeout, waitTimeSeconds)

messages.each { message ->
    String body = message.body
    // Put your business logic here and then delete the message if successfully handled
    amazonSQSService.deleteMessage(queueName, message.receiptHandle)    
}
```

You will probably use `receiveMessages` in a [Quartz](https://github.com/grails-plugins/grails-quartz) job running periodically.

Some interesting settings when receiving messages:
* `maxNumberOfMessages`, how many messages do you cant to get at once (up to 10 messages, default is 1)
* `visibilityTimeout`, how long the messages will stay not visible before going back to the queue (default depending on queue setting, usually 30s)
* `waitTimeSeconds`, how long do you want to wait to get a message (up to 20 seconds, default depending on queue setting, usually 0s) 

## Advanced usage

If required, you can also directly use **AmazonSQSClient** instance available at **amazonSQSService.client**.

For more info, AWS SDK for Java documentation is located here:

* [AWS SDK for Java](http://docs.amazonwebservices.com/AWSJavaSDK/latest/javadoc/index.html)


# Bugs

To report any bug, please use the project [Issues](http://github.com/agorapulse/grails-aws-sdk-sqs/issues) section on GitHub.

Feedback and pull requests are welcome!