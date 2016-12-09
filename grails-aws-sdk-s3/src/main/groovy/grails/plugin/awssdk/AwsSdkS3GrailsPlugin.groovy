package grails.plugin.awssdk

import grails.plugins.*

class AwsSdkS3GrailsPlugin extends Plugin {

    def grailsVersion = "3.0.0 > *"

    def title = "AWS SDK S3 Plugin"
    def author = "Benoit Hediard"
    def authorEmail = "ben@agorapulse.com"
    def description = "Amazon S3 related dependencies and artefacts"

    def documentation = "https://github.com/agorapulse/grails-aws-sdk-s3"
    def license = "APACHE"
    def organization = [ name: "AgoraPulse", url: "http://www.agorapulse.com/" ]
    def issueManagement = [ system: "github", url: "https://github.com/agorapulse/grails-aws-sdk-s3/issues" ]
    def scm = [  url: "https://github.com/agorapulse/grails-aws-sdk-s3" ]

}
