grails.project.work.dir = 'target'
grails.project.source.level = 1.6

grails.project.dependency.resolution = {
    inherits 'global'
    log 'warn'
    repositories {
        grailsCentral()
        mavenLocal()
        mavenCentral()
    }
    dependencies {
        // Workaround to resolve dependency issue with aws-java-sdk and http-builder (dependent on httpcore:4.0)
        build 'org.apache.httpcomponents:httpcore:4.1'
        build 'org.apache.httpcomponents:httpclient:4.1'
        runtime 'org.apache.httpcomponents:httpcore:4.1'
        runtime 'org.apache.httpcomponents:httpclient:4.1'
        compile 'com.amazonaws:aws-java-sdk:1.3.33'
    }
    plugins {
        build(':release:2.2.1', ':rest-client-builder:1.0.3') {
            export = false
        }
    }
}
