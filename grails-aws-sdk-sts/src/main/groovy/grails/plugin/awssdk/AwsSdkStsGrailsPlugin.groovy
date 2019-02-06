package grails.plugin.awssdk

import grails.plugins.Plugin

class AwsSdkStsGrailsPlugin extends Plugin {

    def grailsVersion = "3.0.0 > *"

    def title = "AWS SDK STS Plugin"
    def author = "Benoit Hediard"
    def authorEmail = "ben@agorapulse.com"
    def description = "Amazon STS related dependencies and artefacts"
    def developers = [[ name: "Florian Ernoult", email: "flo@agorapulse.com" ]]

    def documentation = "https://github.com/agorapulse/grails-aws-sdk-sts"
    def license = "APACHE"
    def organization = [ name: "AgoraPulse", url: "http://www.agorapulse.com/" ]
    def issueManagement = [ system: "github", url: "https://github.com/agorapulse/grails-aws-sdk-sts/issues" ]
    def scm = [  url: "https://github.com/agorapulse/grails-aws-sdk-sts" ]

}
