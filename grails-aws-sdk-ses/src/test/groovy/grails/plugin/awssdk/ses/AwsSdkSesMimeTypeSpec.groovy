package grails.plugin.awssdk.ses

import com.agorapulse.awssdk.ses.AwsSdkSesMimeType
import spock.lang.Specification
import spock.lang.Unroll

@Unroll
class AwsSdkSesMimeTypeSpec extends Specification {

    def "test #filename extension should be supported: #expected"(String filename, boolean expected) {

        when:
        boolean result = AwsSdkSesMimeType.isFileExtensionSupported(filename)

        then:
        result == expected

        where:
        filename    | expected
        'virus.exe' | false
        'pic.png'   | true
    }

    def "test #filename mime type should be supported: #mimetype"(String filename, String mimetype) {

        when:
        String result = AwsSdkSesMimeType.mimeTypeFromFilename(filename)

        then:
        result == mimetype

        where:
        filename    | mimetype
        'virus.exe' | 'application/octet-stream'
        'pic.png'   | 'image/png'
        'report.pdf'| 'application/pdf'
    }

    def "test #filename extension should be: #ext"(String filename, String ext) {

        when:
        String result = AwsSdkSesMimeType.fileExtension(filename)

        then:
        result == ext

        where:
        filename    | ext
        'virus.exe' | '.exe'
        'pic.png'   | '.png'
        'report.pdf'| '.pdf'
    }
}
