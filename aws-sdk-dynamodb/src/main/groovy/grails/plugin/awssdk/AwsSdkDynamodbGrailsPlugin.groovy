package grails.plugin.awssdk

import grails.plugins.Plugin

class AwsSdkDynamodbGrailsPlugin extends Plugin {

    def grailsVersion = "3.0.0 > *"
    def pluginExcludes = [
            "grails-app/i18n/mail.properties",
            "grails-app/services/grails/plugin/awssdk/test/FooItemDBService.groovy",
            "src/main/groovy/grails/plugin/awssdk/test/FooItem.groovy"
    ]

    def title = "AWS SDK DynamoDB Plugin"
    def author = "Benoit Hediard"
    def authorEmail = "ben@agorapulse.com"
    def description = "Amazon DynamoDB related dependencies and artefacts"
    def developers = [[name: "Jean-Vincent Drean", email: "jv@agorapulse.com"]]

    def documentation = "https://github.com/agorapulse/grails-aws-sdk-dynamodb"
    def license = "APACHE"
    def organization = [ name: "AgoraPulse", url: "http://www.agorapulse.com/" ]
    def issueManagement = [ system: "github", url: "https://github.com/agorapulse/grails-aws-sdk-dynamodb/issues" ]
    def scm = [  url: "https://github.com/agorapulse/grails-aws-sdk-dynamodb" ]

}
