Grails AWS SDK S3 Plugin
========================

# Introduction

The [AWS SDK Plugins for Grails3](https://medium.com/@benorama/aws-sdk-plugins-for-grails-3-cc7f910fdc0d#.5gdwdxei3) are a suite of plugins that adds support for the [Amazon Web Services](http://aws.amazon.com/) infrastructure services.

The aim is to to get you started quickly by providing friendly lightweight utility [Grails](http://grails.org) service wrappers, around the official [AWS SDK for Java](http://aws.amazon.com/sdkforjava/) (which is great but very “java-esque”).
See [this article](https://medium.com/@benorama/aws-sdk-plugins-for-grails-3-cc7f910fdc0d#.5gdwdxei3) for more info.

The following services are currently supported:

* [AWS SDK DynamoDB Grails Plugin](https://github.com/agorapulse/grails-aws-sdk/tree/master/grails-aws-sdk-dynamodb)
* [AWS SDK Kinesis Grails Plugin](https://github.com/agorapulse/grails-aws-sdk/tree/master/grails-aws-sdk-kinesis)
* [AWS SDK S3 Grails Plugin](https://github.com/agorapulse/grails-aws-sdk/tree/master/grails-aws-sdk-s3)
* [AWS SDK SES Grails Plugin](https://github.com/agorapulse/grails-aws-sdk/tree/master/grails-aws-sdk-ses)
* [AWS SDK SNS Grails Plugin](https://github.com/agorapulse/grails-aws-sdk/tree/master/grails-aws-sdk-sns)
* [AWS SDK SQS Grails Plugin](https://github.com/agorapulse/grails-aws-sdk/tree/master/grails-aws-sdk-sqs)

This plugin adds support for [Amazon Simple Storage Service (Amazon S3)](https://aws.amazon.com/s3/), which provides developers and IT teams with secure, durable, highly-scalable cloud storage. 
S3 is easy to use object storage, with a simple web service interface to store and retrieve any amount of data from anywhere on the web.

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
  compile 'org.grails.plugins:aws-sdk-s3:2.1.5'
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
    plugins:
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
            s3:
                accessKey: {ACCESS_KEY} # (optional)
                secretKey: {SECRET_KEY} # (optional)
                region: eu-west-1       # (optional)
                bucket: my-bucket       # (optional)
            
```

**bucket**: default bucket to use when calling methods without `bucketName`.

TIP: if you use multiple buckets, you can create a new service for each bucket that inherits from **AmazonS3Service**.

```groovy
class MyBucketService extends AmazonS3Service {

    static final BUCKET_NAME = 'my-bucket'

    @PostConstruct
    def init() {
        init(BUCKET_NAME)
    }

}
```


# Usage

The plugin provides the following Grails artefact:

* **AmazonS3Service**

## Bucket management

```groovy 
// Create bucket
amazonS3Service.createBucket(bucketName)

// List bucket names
amazonS3Service.listBucketNames()

// List objects
amazonS3Service.listObjects('my-bucket', 'some-prefix')

// Delete bucket
amazonS3Service.deleteBucket(bucketName)
```

## File management

```groovy 
// Store a file (PublicRead by default, so last parameter could be omitted)
amazonS3Service.storeFile('my-bucket', 'asset/foo/someKey.jpg', new File('/Users/ben/Desktop/photo.jpg'), CannedAccessControlList.PublicRead)
// Or if you have defined default bucket
amazonS3Service.storeFile('asset/foo/someKey.jpg', new File('/Users/ben/Desktop/photo.jpg'), CannedAccessControlList.PublicRead)
    
// Store an uploaded file
MultipartFile multipartFile = request.getFile('file')
if (multipartFile && !multipartFile.empty) {
    amazonS3Service.storeMultipartFile('my-bucket', 'asset/foo/' + multipartFile.originalFilename, multipartFile)
    // Or if you have defined default bucket
    amazonS3Service.storeMultipartFile('asset/foo/' + multipartFile.originalFilename, multipartFile)
}

// Store an input stream
ObjectMetadata metadata = amazonS3Service.buildMetadataFromType('image', 'jpg', CannedAccessControlList.PublicRead)
amazonS3Service.storeInputStream('asset/foo/someKey.jpg', thumbnailInputStream, metadata)

// Store a file asynchronously with transfer manager (https://java.awsblog.com/post/Tx2Q9SGR6OKSVYX/Amazon-S3-TransferManager)
upload = amazonS3Service.transferFile('my-bucket', 'asset/foo/someKey.jpg', new File('/Users/ben/Desktop/photo.jpg'), CannedAccessControlList.PublicRead)
// Or if you have defined default bucket
upload = amazonS3Service.transferFile('asset/foo/someKey.jpg', new File('/Users/ben/Desktop/photo.jpg'), CannedAccessControlList.PublicRead)
// While the transfer is processing, you can work with the transfer object
while (!upload.done) {
    println("${upload.progress.percentTransferred}%")
}

// Check if an object exists in bucket
found = amazonS3Service.exists('my-bucket', 'asset/foo/someKey.jpg')
// Or if you have defined default bucket
found = amazonS3Service.exists('asset/foo/someKey.jpg')

// Generate a pre-signed URL valid during 24h
url = amazonS3Service.generatePresignedUrl('my-bucket', 'asset/foo/someKey.jpg', new Date() + 1)
// Or if you have defined default bucket
url = amazonS3Service.generatePresignedUrl('asset/foo/someKey.jpg', new Date() + 1)

// Get a file
file = amazonS3Servuce.getFile('asset/foo/someKey.jpg', '/Users/ben/Desktop/photo.jpg')

// Delete a file
deleted = amazonS3Service.deleteFile('my-bucket', 'asset/foo/someKey.jpg')
// Or if you have defined default bucket
deleted = amazonS3Service.deleteFile('asset/foo/someKey.jpg') 
```

Supported content types when storing a file:

* audio,
* csv,
* excel,
* flash,
* image,
* pdf,
* file,
* video

If contentType param is empty or blank, S3 will try to set the appropriate content type.

## Advanced usage

If required, you can also directly use **AmazonS3Client** instance available at **amazonS3Service.client**.

For more info, AWS SDK for Java documentation is located here:

* [AWS SDK for Java](http://docs.amazonwebservices.com/AWSJavaSDK/latest/javadoc/index.html)


# Bugs

To report any bug, please use the project [Issues](http://github.com/agorapulse/grails-aws-sdk/issues) section on GitHub.

Feedback and pull requests are welcome!