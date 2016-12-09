package grails.plugin.awssdk

import grails.plugins.Plugin

class AwsSdkSnsGrailsPlugin extends Plugin {

    def grailsVersion = "3.0.0 > *"

    def title = "AWS SDK SNS Plugin"
    def author = "Benoit Hediard"
    def authorEmail = "ben@agorapulse.com"
    def description = "Amazon SNS related dependencies and artefacts"
    def developers = [ [ name: "Florian Ernoult", email: "flo@agorapulse.com" ]]

    def documentation = "https://github.com/agorapulse/grails-aws-sdk-sns"
    def license = "APACHE"
    def organization = [ name: "AgoraPulse", url: "http://www.agorapulse.com/" ]
    def issueManagement = [ system: "github", url: "https://github.com/agorapulse/grails-aws-sdk-sns/issues" ]
    def scm = [  url: "https://github.com/agorapulse/grails-aws-sdk-sns" ]

}
