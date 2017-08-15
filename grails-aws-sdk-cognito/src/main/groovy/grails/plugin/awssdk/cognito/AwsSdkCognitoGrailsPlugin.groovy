package grails.plugin.awssdk.cognito

import grails.plugins.Plugin

class AwsSdkCognitoGrailsPlugin extends Plugin {

    def grailsVersion = "3.0.0 > *"

    def pluginExcludes = []

    def title = "AWS SDK Cognito Plugin"
    def author = "Benoit Hediard"
    def authorEmail = "ben@agorapulse.com"
    def description = "Amazon Cognito related dependencies and artefacts"
    def developers = [[name: "Jean-Vincent Drean", email: "jv@agorapulse.com"], [name: "Vladimir Orany", email: "vlad@agorapulse.com"]]

    def documentation = "https://github.com/agorapulse/grails-aws-sdk"
    def license = "APACHE"
    def organization = [ name: "AgoraPulse", url: "http://www.agorapulse.com/" ]
    def issueManagement = [ system: "github", url: "https://github.com/agorapulse/grails-aws-sdk/issues" ]
    def scm = [  url: "https://github.com/agorapulse/grails-aws-sdk" ]

}
