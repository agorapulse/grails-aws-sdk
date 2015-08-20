class AwsSdkGrailsPlugin {

    def version = "1.10.11"
    def grailsVersion = "2.0 > *"
    //def loadAfter = ['services', 'controllers']

    def title = "AWS SDK Plugin"
    def author = "Benoit Hediard"
    def authorEmail = "ben@benorama.com"
    def description = '''The AWS SDK Plugin allows your Grails application to use the Amazon Web Services infrastructure services.
It provides simple wrapper service around the official AWS SDK for Java.

Using the SDK, developers can build solutions for Amazon Simple Storage Service (Amazon S3), Amazon Elastic Compute Cloud (Amazon EC2), Amazon SimpleDB, and more.
'''
    def documentation = "http://agorapulse.github.io/grails-aws-sdk/guide/"
    def license = "APACHE"
    def organization = [ name: "AgoraPulse", url: "http://www.agorapulse.com/" ]
    def issueManagement = [ url: "https://github.com/agorapulse/grails-aws-sdk/issues" ]
    def scm = [ url: "https://github.com/agorapulse/grails-aws-sdk" ]
}
