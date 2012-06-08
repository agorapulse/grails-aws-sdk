AWS SDK Grails Plugin
================================

# Introduction

The AWS SDK Plugin allows your [Grails](http://grails.org) application to use the [Amazon Web Services](http://aws.amazon.com/) infrastructure services.

It uses the official [AWS SDK for Java](http://aws.amazon.com/sdkforjava/), which provides a Java API for AWS infrastructure services, making it even easier for developers to build applications that tap into the cost-effective, scalable, and reliable AWS cloud.

Using the SDK, developers can build solutions for Amazon Simple Storage Service (Amazon S3), Amazon Elastic Compute Cloud (Amazon EC2), Amazon SimpleDB, and more.

**AWS SDK Grails Plugin** provides :

* **amazonWebService** - A lightweight service wrapper around the [AWS SDK for Java](http://aws.amazon.com/sdkforjava/) to easily access AWS services clients.


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
				runtime ':aws-sdk:0.1.0'
		}
}
```


# Config

Create an AWS account [Amazon Web Services](http://aws.amazon.com/), in order to get your own credentials accessKey and secretKey.

Add your AWS credentials parameters to your _grails-app/conf/Config.groovy_:

```groovy
grails.plugins.awssdk.accessKey = {ACCESS_KEY}
grails.plugins.awssdk.secretKey = {SECRET_KEY}
```

# Documentation

Project documentation is located here:

* [Reference Documentation (Page per chapter)](http://benorama.github.com/aws-sdk-grails-plugin/guide)
* [Reference Documentation (Single page)](http://benorama.github.com/aws-sdk-grails-plugin/guide/single.html)
* [Groovy API docs](http://benorama.github.com/aws-sdk-grails-plugin/gapi/)

AWS SDK for Java documentation is located here:

* [AWS SDK for Java](http://docs.amazonwebservices.com/AWSJavaSDK/latest/javadoc/index.html)

# Supported AWS Services

All services supported by [AWS SDK for Java](http://aws.amazon.com/sdkforjava/) are available:

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
* Amazon Simple Email Service (SES)
* Amazon SimpleDB
* Amazon Simple Notification Service (SNS)
* Amazon Simple Queue Service (SQS)
* Amazon Simple Storage Service (S3)
* Auto Scaling
* AWS Elastic Beanstalk
* AWS Identity & Access Management (IAM)
* AWS Import/Export
* AWS Storage Gateway
* Elastic Load Balancing

# Bugs

To report any bug, please use the project [Issues](http://github.com/benorama/aws-sdk-grails-plugin/issues) section on GitHub.

# Alpha status

This is an **alpha release**.
The underlying APIs are generally stable, however we may make changes to the library in response to developer feedback.

# Other AWS plugin

There is another [AWS Grails plugin](http://grails.org/plugin/aws) which provides groovy DSL to use SES and adds methods to easily use S3 (based on JetS3 java lib).
If you just need basic SES or S3 features, you might give it a try.

