grails.project.class.dir = "target/classes"
grails.project.test.class.dir = "target/test-classes"
grails.project.test.reports.dir = "target/test-reports"
grails.project.target.level = 1.6
//grails.project.war.file = "target/${appName}-${appVersion}.war"

grails.project.dependency.resolution = {
    // inherit Grails' default dependencies
    inherits("global") {
        // uncomment to disable ehcache
        // excludes 'ehcache'
    }
    log "warn" // log level of Ivy resolver, either 'error', 'warn', 'info', 'debug' or 'verbose'
    repositories {
        grailsCentral()
        // uncomment the below to enable remote dependency resolution
        // from public Maven repositories
        //mavenCentral()
        //mavenLocal()
        //mavenRepo "http://snapshots.repository.codehaus.org"
        //mavenRepo "http://repository.codehaus.org"
        //mavenRepo "http://download.java.net/maven/2/"
        //mavenRepo "http://repository.jboss.com/maven2/"
    }
    dependencies {
        compile('com.amazonaws:aws-java-sdk:1.3.12') {
            excludes 'jackson-core-asl', 'jackson-mapper-asl' // Workaround for V1.3.12 jackson dependency bug (version 1.8 does not exists, it should be 1.8.0)
        }
        compile('org.codehaus.jackson:jackson-core-asl:1.8.0',
                'org.codehaus.jackson:jackson-mapper-asl:1.8.0')
    }

    plugins {
        build(":tomcat:$grailsVersion",
              ":release:latest.integration") {
            export = false
        }
    }
}
