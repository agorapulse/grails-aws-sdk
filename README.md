AWS SDK Grails Plugin
=====================

[![Build Status](https://travis-ci.org/agorapulse/grails-aws-sdk.svg)](https://travis-ci.org/agorapulse/grails-aws-sdk)  [ ![Download](https://api.bintray.com/packages/agorapulse/plugins/aws-sdk-dynamodb/images/download.svg?version=2.2.12) ](https://bintray.com/agorapulse/plugins/aws-sdk-dynamodb/2.2.12/link)

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

# Testing

[How to Unit Test AWS Services with LocalStack and Testcontainers](https://medium.com/agorapulse-stories/how-to-unit-test-aws-services-with-localstack-and-testcontainers-1d39fe5dc6c2)

# Bugs

To report any bug, please use the project [Issues](http://github.com/agorapulse/grails-aws-sdk/issues) section on GitHub.

# Compatibilty 

NOTE: For Grails 4 you should consider migrating to [Micronaut AWS SDK](https://agorapulse.github.io/micronaut-aws-sdk/) as Miconaut is now the first class citizen in Grails. Use `-micronaut-1.2` releases for Grails `4.0.x`.

| Grails        | Plugin        |
| ------------- |---------------|
| 3.3.x, 4.x    | 2.2.x         |
| 3.2.x         | 2.1.x         |
| 2.x           | 1.x           |

