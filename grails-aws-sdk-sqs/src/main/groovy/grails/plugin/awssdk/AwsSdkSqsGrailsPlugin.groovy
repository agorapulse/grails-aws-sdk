package grails.plugin.awssdk

import grails.plugins.Plugin

class AwsSdkSqsGrailsPlugin extends Plugin {

    def grailsVersion = "3.0.0 > *"

    def title = "AWS SDK SQS Plugin"
    def author = "Benoit Hediard"
    def authorEmail = "ben@agorapulse.com"
    def description = "Amazon SQS related dependencies and artefacts"

    def documentation = "https://github.com/agorapulse/grails-aws-sdk-sqs"
    def license = "APACHE"
    def organization = [ name: "AgoraPulse", url: "http://www.agorapulse.com/" ]
    def issueManagement = [ system: "github", url: "https://github.com/agorapulse/grails-aws-sdk-sqs/issues" ]
    def scm = [  url: "https://github.com/agorapulse/grails-aws-sdk-sqs" ]

}
