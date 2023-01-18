AWS SDK Grails Plugin
=====================

IMPORTANT: Project retirement
-------------------------------

**This project is retired. As Micronaut become core of the Grails starting at Grails 4, please, use [Micronaut AWS SDK](https://agorapulse.github.io/micronaut-aws-sdk) instead.**

# Introduction

The [AWS SDK Plugins for Grails3](https://medium.com/@benorama/aws-sdk-plugins-for-grails-3-cc7f910fdc0d#.5gdwdxei3) are a suite of plugins that adds support for the [Amazon Web Services](http://aws.amazon.com/) infrastructure services.

The aim is to to get you started quickly by providing friendly lightweight utility [Grails](http://grails.org) service wrappers, around the official [AWS SDK for Java](http://aws.amazon.com/sdkforjava/) (which is great but very “java-esque”).
See [this article](https://medium.com/@benorama/aws-sdk-plugins-for-grails-3-cc7f910fdc0d#.5gdwdxei3) for more info.

The following services are currently supported:

* [AWS SDK Cognito Grails Plugin](https://github.com/agorapulse/grails-aws-sdk/tree/master/grails-aws-sdk-cognito)
* [AWS SDK DynamoDB Grails Plugin](https://github.com/agorapulse/grails-aws-sdk/tree/master/grails-aws-sdk-dynamodb)
* [AWS SDK Kinesis Grails Plugin](https://github.com/agorapulse/grails-aws-sdk/tree/master/grails-aws-sdk-kinesis)
* [AWS SDK S3 Grails Plugin](https://github.com/agorapulse/grails-aws-sdk/tree/master/grails-aws-sdk-s3)
* [AWS SDK SES Grails Plugin](https://github.com/agorapulse/grails-aws-sdk/tree/master/grails-aws-sdk-ses)
* [AWS SDK SNS Grails Plugin](https://github.com/agorapulse/grails-aws-sdk/tree/master/grails-aws-sdk-sns)
* [AWS SDK SQS Grails Plugin](https://github.com/agorapulse/grails-aws-sdk/tree/master/grails-aws-sdk-sqs)

Please check each README for usage info.

# Installation

The plugins have switched to Jitpack:

```groovy
repositories {
    maven { url 'https://jitpack.io' }
}

dependencies {
    // example dependency
    compile 'com.github.agorapulse.grails-aws-sdk:aws-sdk-s3:2.4.14'
}

```

# Testing

[How to Unit Test AWS Services with LocalStack and Testcontainers](https://medium.com/agorapulse-stories/how-to-unit-test-aws-services-with-localstack-and-testcontainers-1d39fe5dc6c2)

# Bugs

To report any bug, please use the project [Issues](http://github.com/agorapulse/grails-aws-sdk/issues) section on GitHub.

# Compatibilty 

NOTE: For Grails 4 and later you should migrate to [Micronaut AWS SDK](https://agorapulse.github.io/micronaut-aws-sdk/) as Miconaut is now the first class citizen in Grails.

| Grails        | Plugin        |
| ------------- |---------------|
| 3.3.x, 4.x    | 2.2.x         |
| 3.2.x         | 2.1.x         |
| 2.x           | 1.x           |

