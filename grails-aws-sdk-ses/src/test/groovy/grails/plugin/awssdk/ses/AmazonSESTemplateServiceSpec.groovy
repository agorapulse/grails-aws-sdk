package grails.plugin.awssdk.ses

import grails.test.mixin.TestFor
import org.springframework.context.MessageSource
import org.springframework.context.NoSuchMessageException
import spock.lang.Specification

@TestFor(AmazonSESTemplateService)
class AmazonSESTemplateServiceSpec extends Specification {

    def "test subjectWithSubjectKey"() {
        given:
            service.messageSource = Mock(MessageSource)
            service.messageSource.getMessage(_ as String, _ as Object[], _ as Locale) >> { String code, Object[] args, Locale locale ->
                throw new NoSuchMessageException('key not found')
            }
        expect: 'default subject should be return if the passed key is not found'
            service.subjectWithSubjectKey('notexistingkey', [], Locale.ENGLISH, 'Email Subject') == 'Email Subject'

    }
}
