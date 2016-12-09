package grails.plugin.awssdk

import grails.plugins.*

class AwsSdkSesGrailsPlugin extends Plugin {

    def grailsVersion = "3.0.0 > *"
    def pluginExcludes = [
            "grails-app/i18n/mail.properties",
            "grails-app/views/templates/email/_test.gsp"
    ]

    def title = "AWS SDK SES Plugin"
    def author = "Benoit Hediard"
    def authorEmail = "ben@agorapulse.com"
    def description = "AWS SES related dependencies and artefacts"
    def developers = [ [ name: "Sergio del Amo", email: "me@sergiodelamo.com" ]]

    def documentation = "https://github.com/agorapulse/grails-aws-sdk-ses"
    def license = "APACHE"
    def organization = [ name: "AgoraPulse", url: "http://www.agorapulse.com/" ]
    def issueManagement = [ system: "github", url: "https://github.com/agorapulse/grails-aws-sdk-ses/issues" ]
    def scm = [  url: "https://github.com/agorapulse/grails-aws-sdk-ses" ]

}
