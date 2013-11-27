AWS SDK Grails Plugin
=====================

[![Build Status](https://travis-ci.org/benorama/grails-aws-sdk.png)](https://travis-ci.org/benorama/grails-aws-sdk)

# Introduction

The AWS SDK Plugin allows your [Grails](http://grails.org) application to use the [Amazon Web Services](http://aws.amazon.com/) infrastructure services.

The aim is to provide a lightweight **amazonWebService** Grails service wrapper around the official [AWS SDK for Java](http://aws.amazon.com/sdkforjava/).

The [AWS SDK for Java](http://aws.amazon.com/sdkforjava/) provides a Java API for AWS infrastructure services, making it even easier for developers to build applications that tap into the cost-effective, scalable, and reliable AWS cloud.

The Grails plugin handles :

* convenient Grails configuration/management of all AWS API clients for each AWS region,
* easy access to all AWS API java clients through the **amazonWebService** Grails service wrapper.


# Installation

Declare the plugin dependency in the BuildConfig.groovvy file, as shown here:

```groovy
grails.project.dependency.resolution = {
		inherits("global") { }
		log "info"
		repositories {
				//your repositories
		}
		dependencies {
				// Workaround to resolve dependency issue with aws-java-sdk and http-builder (dependent on httpcore:4.0)
                build 'org.apache.httpcomponents:httpcore:4.2'
                build 'org.apache.httpcomponents:httpclient:4.2'
                runtime 'org.apache.httpcomponents:httpcore:4.2'
                runtime 'org.apache.httpcomponents:httpclient:4.2'
		}
		plugins {
				//here go your plugin dependencies
				runtime ':aws-sdk:1.6.7'
		}
}
```


# Config

Create an AWS account [Amazon Web Services](http://aws.amazon.com/), in order to get your own credentials accessKey and secretKey.

Add your AWS credentials parameters to your _grails-app/conf/Config.groovy_:

```groovy
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

* Amazon CloudFormation
* Amazon CloudFront
* Amazon CloudSearch
* Amazon CloudWatch
* Amazon DynamoDB
* Amazon Elastic Compute Cloud (EC2)
* Amazon Elastic MapReduce
* Amazon Elastic Transcoder
* Amazon ElastiCache
* Amazon Glacier
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

# Latest releases

**WARNING**: Breaking change, since V1.4.5, new **DynamoDB** API Version 2012-08-10 are used (package *com.amazonaws.services.dynamodbv2* instead of *com.amazonaws.services.dynamodb* ).
If you are using DynamoDB, please update your calls to the API.

* 2013-11-27 **V1.6.7** : AWS SDK for Java updated to V1.6.7
* 2013-11-07 **V1.6.4** : AWS SDK for Java updated to V1.6.4 + proxy settings added in config (thanks to pull request by Jeff Purser)
* 2013-10-25 **V1.6.3** : AWS SDK for Java updated to V1.6.3 + AmazonS3EncryptionClient added
* 2013-10-03 **V1.6.0** : AWS SDK for Java updated to V1.6.0
* 2013-09-11 **V1.5.6** : AWS SDK for Java updated to V1.5.6 + Grails 2.3.0 support
* 2013-08-27 **V1.5.5** : AWS SDK for Java updated to V1.5.5
* 2013-07-11 **V1.5.0** : AWS SDK for Java updated to V1.5.0
* 2013-06-20 **V1.4.7** : AWS SDK for Java updated to V1.4.7
* 2013-06-06 **V1.4.5** : AWS SDK for Java updated to V1.4.5 + DynamoDB V2 support by default
* 2013-05-11 **V1.4.3** : AWS SDK for Java updated to V1.4.3 + Security Token Service support added and bug fixes (thanks to pull requests by craigforster and jako512)
* 2013-04-22 **V1.4.2** : AWS SDK for Java updated to V1.4.2 (DynamoDB adds support for local secondary indexes)
* 2013-03-19 **V1.4.1** : AWS SDK for Java updated to V1.4.1 + Better endpoints management with new region utils
* 2013-03-05 **V1.3.33** : AWS SDK for Java updated to V1.3.33 + Unit tests added + refactor/bug fixes (thanks to Martin Gotink)
* 2013-02-21 **V1.3.32** : AWS SDK for Java updated to V1.3.32 + Amazon OpsWorks and Redshift support added + bug fix
* 2013-02-05 **V1.3.30** : AWS SDK for Java updated to V1.3.30 + Amazon Elastic Transcoder support added
* 2012-12-20 **V1.3.26** : AWS SDK for Java updated to V1.3.26 + S3 Resources scripts (to use with CDN Resources plugin)
* 2012-11-12 **V1.3.24** : AWS SDK for Java updated to V1.3.24 (adds support for long polling in SQS)
* 2012-10-06 **V1.3.22** : AWS SDK for Java updated to V1.3.22 + S3 transfer manager support added (for async upload/download and multiple files upload/download)
* 2012-09-27 **V1.3.21** : AWS SDK for Java updated to V1.3.21.1 + Amazon Glacier support added
* 2012-07-04 **V1.3.12** : AWS SDK for Java updated to V1.3.12
* 2012-07-02 **V1.3.11** : initial release

# Bugs

To report any bug, please use the project [Issues](http://github.com/agorapulse/grails-aws-sdk/issues) section on GitHub.


# Other Grails AWS Plugin

FYI, there is another great [Grails AWS plugin](http://grails.org/plugin/aws) with a different approach: its aim is to provide an easy "groovy" access to SES (through a groovy DSL) and S3 (through methods injection), based on JetS3 java lib. If you just need basic SES or S3 features, you might give it a try.

We decided to write our own AWS plugin because it did not meet our requirements:

1. direct access to **ALL** [AWS SDK for Java](http://aws.amazon.com/sdkforjava/) features including **ALL** AWS services, with custom client configuration,
2. only [AWS SDK for Java](http://aws.amazon.com/sdkforjava/) with a consistent way to access **ALL** AWS services,
3. fast release update with 100% compatibility with [AWS SDK for Java](http://aws.amazon.com/sdkforjava/), as it is just a simple lightweight wrapper around the official java clients,
4. no need for additional DSL/methods injection, since we found the [AWS SDK for Java](http://aws.amazon.com/sdkforjava/) clients pretty straightforward to use

