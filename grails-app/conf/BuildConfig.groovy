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
        compile('com.amazonaws:aws-java-sdk:1.3.12') {
            excludes 'jackson-core-asl', 'jackson-mapper-asl' // Workaround for V1.3.12 jackson dependency bug (version 1.8 does not exists, it should be 1.8.0)
        }
        compile('org.codehaus.jackson:jackson-core-asl:1.8.0',
                'org.codehaus.jackson:jackson-mapper-asl:1.8.0')
    }

    plugins {
        build(":release:latest.integration") {
            export = false
        }
    }
}
