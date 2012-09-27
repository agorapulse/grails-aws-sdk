grails.project.class.dir = "target/classes"
grails.project.test.class.dir = "target/test-classes"
grails.project.test.reports.dir = "target/test-reports"
grails.project.target.level = 1.6

grails.project.dependency.resolution = {
    inherits 'global'
    log 'warn'
    repositories {
        grailsCentral()
        mavenCentral()
    }
    dependencies {
        compile 'com.amazonaws:aws-java-sdk:1.3.21.1'
    }

    plugins {
        build(":release:latest.integration") {
            export = false
        }
    }
}
