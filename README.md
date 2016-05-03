AWS SDK Grails Plugin
=====================

[![Coverage Status](https://img.shields.io/coveralls/agorapulse/grails-aws-sdk.svg)](https://coveralls.io/r/agorapulse/grails-aws-sdk?branch=master)
[![Build Status](https://travis-ci.org/agorapulse/grails-aws-sdk.svg)](https://travis-ci.org/agorapulse/grails-aws-sdk)

# LEGACY PLUGIN

This plugin has been split into several ones in order to benefit from AWS java SDK modular approach.

See this [article](https://medium.com/@benorama/aws-sdk-plugins-for-grails-3-cc7f910fdc0d#.ce4eh6tne) for more info.


# Introduction

The AWS SDK Plugin allows your [Grails](http://grails.org) application to use the [Amazon Web Services](http://aws.amazon.com/) infrastructure services.

The aim is to provide a lightweight **amazonWebService** Grails service wrapper around the official [AWS SDK for Java](http://aws.amazon.com/sdkforjava/).

The [AWS SDK for Java](http://aws.amazon.com/sdkforjava/) provides a Java API for AWS infrastructure services, making it even easier for developers to build applications that tap into the cost-effective, scalable, and reliable AWS cloud.

The Grails plugin handles :

* convenient Grails configuration/management of all AWS API clients for each AWS region,
* easy access to all AWS API java clients through the **amazonWebService** Grails service wrapper.


# Installation

Declare the plugin dependency in the _build.gradle_ file, as shown here:

```groovy
repositories {
    ...
    maven { url "http://dl.bintray.com/agorapulse/plugins" }
}
dependencies {
    ...
    compile "org.grails.plugins:aws-sdk:1.10.74"
}
```


# Config

Create an AWS account [Amazon Web Services](http://aws.amazon.com/), in order to get your own credentials accessKey and secretKey.

Add your AWS credentials parameters to your _grails-app/conf/application.yml_:

```yml
grails.plugin.awssdk.accessKey = {ACCESS_KEY}
grails.plugin.awssdk.secretKey = {SECRET_KEY}
```

# Documentation

Project documentation is located here:

* [Reference Documentation (Page per chapter)](http://agorapulse.github.io/grails-aws-sdk/guide)
* [Reference Documentation (Single page)](http://agorapulse.github.io/grails-aws-sdk/guide/single.html)
* [Groovy API docs](http://agorapulse.github.io/grails-aws-sdk/gapi/)

AWS SDK for Java documentation is located here:

* [AWS SDK for Java](http://docs.amazonwebservices.com/AWSJavaSDK/latest/javadoc/index.html)

# Supported AWS Services

AWS clients supported by [AWS SDK for Java](http://aws.amazon.com/sdkforjava/):

* Amazon ApiGateway
* Amazon CloudFormation
* Amazon CloudFront
* Amazon CloudSearch
* Amazon CloudWatch
* Amazon CodeDeploy
* Amazon Cognito Identity
* Amazon Cognito Sync
* Amazon Config
* Amazon DynamoDB
* Amazon Elastic Compute Cloud (EC2)
* Amazon Elastic MapReduce
* Amazon Elastic Transcoder
* Amazon ElastiCache
* Amazon Glacier
* Amazon Kinesis
* Amazon Key Management Service (KMS)
* Amazon Relational Database Service (RDS)
* Amazon Redshift
* Amazon Route 53
* Amazon Simple Storage Service (S3)
* Amazon Simple Email Service (SES)
* Amazon SimpleDB
* Amazon Simple Notification Service (SNS)
* Amazon Simple Queue Service (SQS)
* Amazon Simple Workflow (SWF)
* Auto Scaling
* AWS Elastic Beanstalk
* AWS Identity & Access Management (IAM)
* AWS Import/Export
* AWS OpsWorks
* AWS Security Token Service
* AWS Storage Gateway
* Elastic Load Balancing

# Bugs

To report any bug, please use the project [Issues](http://github.com/agorapulse/grails-aws-sdk/issues) section on GitHub.


# Other Grails AWS Plugin

FYI, there is another great [Grails AWS plugin](http://grails.org/plugin/aws) with a different approach: its aim is to provide an easy "groovy" access to SES (through a groovy DSL) and S3 (through methods injection), based on JetS3 java lib. If you just need basic SES or S3 features, you might give it a try.

We decided to write our own AWS plugin because it did not meet our requirements:

1. direct access to **ALL** [AWS SDK for Java](http://aws.amazon.com/sdkforjava/) features including **ALL** AWS services, with custom client configuration,
2. only [AWS SDK for Java](http://aws.amazon.com/sdkforjava/) with a consistent way to access **ALL** AWS services,
3. fast release update with 100% compatibility with [AWS SDK for Java](http://aws.amazon.com/sdkforjava/), as it is just a simple lightweight wrapper around the official java clients,
4. no need for additional DSL/methods injection, since we found the [AWS SDK for Java](http://aws.amazon.com/sdkforjava/) clients pretty straightforward to use
