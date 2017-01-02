package grails.plugin.awssdk

import grails.plugins.Plugin

class AwsSdkKinesisGrailsPlugin extends Plugin {

    def grailsVersion = "3.0.0 > *"

    def title = "AWS SDK Kinesis Plugin"
    def author = "Benoit Hediard"
    def authorEmail = "ben@agorapulse.com"
    def description = "Amazon Kinesis related dependencies and artefacts"

    def documentation = "https://github.com/agorapulse/grails-aws-sdk-kinesis"
    def license = "APACHE"
    def organization = [ name: "AgoraPulse", url: "http://www.agorapulse.com/" ]
    def issueManagement = [ system: "github", url: "https://github.com/agorapulse/grails-aws-sdk-kinesis/issues" ]
    def scm = [  url: "https://github.com/agorapulse/grails-aws-sdk-kinesis" ]

}
