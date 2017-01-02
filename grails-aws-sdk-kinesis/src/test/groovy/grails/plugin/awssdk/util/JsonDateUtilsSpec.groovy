package grails.plugin.awssdk.util

import spock.lang.Specification

class JsonDateUtilsSpec extends Specification {

    def "Parsing Json date"() {
        when:
        Date date = JsonDateUtils.parseJsonDate('2014-10-03T13:01:16Z')

        then:
        date.time == 1412341276000
    }

}
