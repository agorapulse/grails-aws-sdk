Grails AWS SDK SES Plugin
=========================

[![Build Status](https://travis-ci.org/agorapulse/grails-aws-sdk.svg)](https://travis-ci.org/agorapulse/grails-aws-sdk)
[![Download](https://api.bintray.com/packages/agorapulse/plugins/aws-sdk-ses/images/download.svg)](https://bintray.com/agorapulse/plugins/aws-sdk-ses/_latestVersion)

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

This plugin adds support for [Amazon Simple Email Service (Amazon SES)](https://aws.amazon.com/ses/), a cost-effective email service built on the reliable and scalable infrastructure that Amazon.com developed to serve its own customer base.


# Installation

Add plugin dependency to your `build.gradle`:

```groovy
repositories {
    ...
    maven { url 'http://dl.bintray.com/agorapulse/libs' }
    ...
}

dependencies {
  ...
  compile 'org.grails.plugins:aws-sdk-ses:2.2.11'
  ...
}
```

If not provided (for example with `rest-api` profile), you might have to had GSP plugin dependency.

```groovy
...
apply plugin:"org.grails.grails-gsp"
...

dependencies {
    ...
    compile "org.grails:grails-plugin-gsp"
    ...
}
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
            ses:
                accessKey: {ACCESS_KEY} # (optional)
                secretKey: {SECRET_KEY} # (optional)
                region: eu-west-1       # (optional)
                sourceEmail: notification@foo.com # (optional)
                subjectPrefix: [BETA]   # (optional)
                templatePath: /templates/email # (optional), default to /templates/email
            
```

**sourceEmail** allows you define global from/source email.

**subjectPrefix** allows you to automatically prefix all your email subjects (for example, to get a specific env).
By default, in environments other than PROD, subject are prefixed by "[ENV_NAME] ..." (ex: "[DEVELOPMENT] Some test subject")

**templatePath** allows to specify the location of your email templates, relative to the `/views` folder.


# Usage

The plugin provides the following Grails artefacts:

* **AmazonSESService**
* **AmazonSESTemplateService**

And the following interface:

* **MailRecipient**

## Sending a raw HTML email

To send a custom HTML email.
The send method returns:
 
* 1 if successful, 
* 0 if not sent, 
* -1 if blacklisted

```groovy
String htmlBody = '''
<html>
<body>
The email content
</body>
</html>
'''

int statusId = amazonSESService.send(
    'ben@foo.com',
    'Some subject',
    htmlBody,
    'notification@foo.com',  	// Optional, default to sourceEmail from config
    'reply@foo.com'             // Optional, reply to email
)
```

You can use a method which takes a closure as an argument

```groovy
int statusId = amazonSESService.mail {
    to 'recipient@foo.com',
    subject 'Some subject'
    from 'sender@foo.com'
}
```
## Sending an email based on a GSP template

## Send Emails with Attachments

You can send an email with an attachment as illustrated below:

```groovy
int statusId = amazonSESService.mailWithAttachment {
    to 'recipient@foo.com',
    subject 'Some subject'
    from 'sender@foo.com'
    htmlBody '<p>Find your ticket attached</p>'
    attachment {
        filepath '/tmp/ticket.pdf'
    }
}
```

If you want to have more control about the attachment filename, mime type .. you can use:

```groovy
int statusId = amazonSESService.mailWithAttachment {
    to 'recipient@foo.com',
    subject 'Some subject'
    from 'sender@foo.com'
    htmlBody '<p>Find your ticket attached</p>'
    attachment {
        filename 'ticket-2344.pdf'
        filepath '/tmp/ticket.pdf'
        mimeType 'application/pdf'
        description 'Your concert ticket'
    }
}
```


**Note**. AWS SES has a list of [unsupported attachment types](http://docs.aws.amazon.com/ses/latest/DeveloperGuide/mime-types.html)

## Send Emails with Templates

To send an email from an GSP template with i18n support.

```groovy
int statusId = amazonSESTemplateService.sendTemplate(
    'ben@foo.com',
    'email.test.subject',
    [],             // Subject variables, if required
    [
        foo: 'Some value to use in the template',
        bar: 'Another value'
    ],
    'test'          // GSP located in '/views/template/emails/_test.gsp'
)
```

Example template _test.gsp:

```html
<html>
<body>
The template content with some foo=${foo} and bar=${bar}<br/>
Sent date: ${sentDate}
</body>
</html>
```

You can send a template using a method which takes a closure as illustrated here:

```groovy
def m = [foo: 'Some value to use in the template',bar: 'Another value']
def statusId = amazonSESTemplateService.mailTemplate {
    to 'ben@foo.com',
    subjectCode 'email.test.subject' // defined in mail.properties
    model m
    templateName 'test' // GSP located in '/views/template/emails/_test.gsp'
}
```

To send an email to a recipient (with a class that implements [MailRecipient](/src/main/groovy/grails/plugins/awssdk/ses/MailRecipient.groovy) interface).

```groovy
int statusId = amazonSESTemplateService.sendTemplateToRecipient(
    recipient,
    'email.test.subject',
    [],             // Subject variables, if required
    [
        foo: 'Some value to use in the template',
        bar: 'Another value'
    ],
    'test'          // GSP located in '/views/template/emails/_test.gsp'
)
```

## Advanced usage

If required, you can also directly use **AmazonSESClient** instance available at **amazonSESService.client**.

For more info, AWS SDK for Java documentation is located here:

* [AWS SDK for Java](http://docs.amazonwebservices.com/AWSJavaSDK/latest/javadoc/index.html)


# Download and Run Tests

Download the repository and create a file named _grails-app/conf/application-TEST.yml_ It will be merged with the application.yml when you execute your tests. Note: this file is included in .gitignore

```groovy
grails:
    plugin:
        awssdk:
            ses:
                accessKey: {ACCESS_KEY}
                secretKey: {SECRET_KEY}
                region: eu-west-1 // change this to your region
                sourceEmail: notification@foo.com
```

The integration tests connect to a real email server, fetches the emails of the inbox and asserts the email was actually delivered. You will need to setup the next environment variables (values will depend on your email account): 

```groovy
TEST_INBOX_EMAIL={your_verified_email_address@gmx.es}
TEST_INBOX_HOST=pop.gmx.com
TEST_INBOX_PASSWORD={youpass}
TEST_INBOX_FOLDER=INBOX
TEST_INBOX_PROVIDER=pop3
```

If you don't setup the previous environment variables, the integration tests will be ignored. 

Note: **If you SES account is still sandboxed. You can only send emails to verified email accounts.**


# Bugs

To report any bug, please use the project [Issues](http://github.com/agorapulse/grails-aws-sdk/issues) section on GitHub.

Feedback and pull requests are welcome!


# Thanks

Thanks to [Sergio del Amo](https://github.com/sdelamo) for the contributions!
