package awssdk

import grails.boot.*
import grails.boot.config.GrailsAutoConfiguration

class Application extends GrailsAutoConfiguration implements GrailsPluginApplication {
    static void main(String[] args) {
        GrailsApp.run(Application, args)
    }
}