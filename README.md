AWS SDK Grails Plugin
=====================

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
				//your regular dependencies
		}
		plugins {
				//here go your plugin dependencies
				runtime ':aws-sdk:1.3.12'
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

* [Reference Documentation (Page per chapter)](http://benorama.github.com/aws-sdk-grails-plugin/guide)
* [Reference Documentation (Single page)](http://benorama.github.com/aws-sdk-grails-plugin/guide/single.html)
* [Groovy API docs](http://benorama.github.com/aws-sdk-grails-plugin/gapi/)

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
* Amazon ElastiCache
* Amazon Relational Database Service (RDS)
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
* AWS Storage Gateway
* Elastic Load Balancing

# Latest releases

* 2012-07-04 **V1.3.12** : AWS SDK for Java updated to V1.3.12
* 2012-07-02 **V1.3.11** : initial release

# Bugs

To report any bug, please use the project [Issues](http://github.com/benorama/aws-sdk-grails-plugin/issues) section on GitHub.


# Alpha status

This is an **alpha release**.


# Other Grails AWS Plugin

FYI, there is another great [Grails AWS plugin](http://grails.org/plugin/aws) with a different approach: its aim is to provide an easy "groovy" access to SES (through a groovy DSL) and S3 (through methods injection), based on JetS3 java lib. If you just need basic SES or S3 features, you might give it a try.

We decided to write our own AWS plugin because it did not meet our requirements:

1. direct access to **ALL** [AWS SDK for Java](http://aws.amazon.com/sdkforjava/) features including **ALL** AWS services, with custom client configuration,
2. only [AWS SDK for Java](http://aws.amazon.com/sdkforjava/) with a consistent way to access **ALL** AWS services,
3. fast release update with 100% compatibility with [AWS SDK for Java](http://aws.amazon.com/sdkforjava/), as it is just a simple lightweight wrapper around the official java clients,
4. no need for additional DSL/methods injection, since we found the [AWS SDK for Java](http://aws.amazon.com/sdkforjava/) clients pretty straightforward to use

